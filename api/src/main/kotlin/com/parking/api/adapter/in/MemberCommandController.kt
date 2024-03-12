package com.parking.api.adapter.`in`

import com.parking.api.adapter.`in`.dto.SignUpDTO
import com.parking.api.application.port.`in`.SaveMemberUseCase
import com.parking.api.common.dto.SuccessResponseDTO
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/member")
class MemberCommandController(
    private val saveMemberUseCase: SaveMemberUseCase
) {
    @PostMapping("/sign-up")
    fun signUp(
        @RequestBody signUpDTO: SignUpDTO
    ): SuccessResponseDTO<String> {
        saveMemberUseCase.saveMember(signUpDTO.toMemberInfoVO(), signUpDTO.password)

        return SuccessResponseDTO.success("가입 완료 페이지")
    }
}