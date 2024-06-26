package com.parking.api.application.service

import com.parking.api.application.port.`in`.member.FindMemberUseCase
import com.parking.api.application.port.`in`.member.RefreshAccessToken
import com.parking.api.application.port.`in`.member.SignInMemberUseCase
import com.parking.api.application.port.out.FindMemberPort
import com.parking.api.application.vo.MemberInfoVO
import com.parking.api.common.jwt.JwtTokenProvider
import com.parking.api.common.jwt.Token
import com.parking.domain.entity.Member
import com.parking.domain.exception.MemberException
import com.parking.domain.exception.enum.ExceptionCode.*
import com.parking.redis.service.RedisService
import mu.KLogging
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class MemberInquiryService(
    private val findMemberPort: FindMemberPort,
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenProvider: JwtTokenProvider,
    private val passwordEncoder: PasswordEncoder,
    private val redisService: RedisService<String>
) : FindMemberUseCase, SignInMemberUseCase,
    RefreshAccessToken {
    override fun findMemberInfoByMemberId(memberId: String): MemberInfoVO {
        val member = findMemberPort.findMemberInfoByMemberId(memberId) ?: throw MemberException(
            MEMBER_NOT_FOUND,
            MEMBER_NOT_FOUND.message
        )

        return MemberInfoVO.from(member)
    }

    override fun findIdByMemberId(memberId: String): Long {
        val id = findMemberPort.findIdByMemberId(memberId) ?: throw MemberException(
            MEMBER_NOT_FOUND,
            MEMBER_NOT_FOUND.message
        )

        return id
    }

    override fun signIn(memberId: String, password: String): Token {
        // 인증 실패 횟수 조회
        if (!checkPasswordTryCount(memberId)) {
            throw MemberException(LOGIN_TRY_COUNT_LIMIT, LOGIN_TRY_COUNT_LIMIT.message)
        }

        val member = findMemberPort.findMemberInfoByMemberId(memberId) ?: throw MemberException(
            MEMBER_NOT_FOUND,
            MEMBER_NOT_FOUND.message
        )

        //비밀번호 일치 여부 확인
        if (!passwordEncoder.matches(password, member.password!!)) {
            val uuid = UUID.randomUUID()
            redisService.set("${memberId}_${uuid}", LocalDateTime.now().toString())

            throw MemberException(PASSWORD_NOT_MATCH, PASSWORD_NOT_MATCH.message)
        }

        //입력한 비밀번호 일치할 경우 현재 redis 에 저장된 비밀번호가 틀린 기록을 모두 삭제
        redisService.deleteStringValues(String.format("%s_*", memberId))

        try {
            // 인증시도
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(memberId, password, null)
            )
        } catch (e: Exception) {
            // 인증 실패
            throw MemberException(AUTHENTICATION_FAIL, AUTHENTICATION_FAIL.message)
        }

        // Login이 성공한 경우 Token 생성
        val accessToken = jwtTokenProvider.createAccessToken(memberId)
        val refreshToken = jwtTokenProvider.createRefreshToken(memberId)

        return Token(accessToken, refreshToken)
    }

    private fun checkPasswordTryCount(memberId: String) : Boolean {
        val values = redisService.getStringValues(String.format("%s_*", memberId))

        return Member.LIMIT_PASSWORD_TRY_COUNT > values.size
    }

    override fun refreshAccessToken(refreshToken: String): Token {
        if (jwtTokenProvider.validateExpireToken(refreshToken)) {
            val memberId = jwtTokenProvider.parseUsername(refreshToken)

            return Token(
                accessToken = jwtTokenProvider.createAccessToken(memberId),
                refreshToken = refreshToken
            )
        }

        return Token()
    }

    companion object : KLogging()
}