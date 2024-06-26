package com.parking.jpa.entity

import com.parking.domain.entity.Member
import com.parking.domain.entity.MemberInfo
import com.parking.domain.entity.MemberStatus
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "member")
data class MemberEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "member_id")
    val memberId: String,

    val password: String,

    @Column(name = "member_name")
    val memberName: String,

    val email: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "member_status")
    val memberStatus: MemberStatus = MemberStatus.ACTIVATED,

    @Column(name="no_show_count")
    val noShowCount: Long = 0L,

    @Column(name = "start_no_show_time")
    val startNoShowTime: LocalDateTime? = null

) : BaseEntity() {
    fun to() = Member (
        id = this.id,
        password = password,
        memberInfo = MemberInfo(this.memberId, this.memberName, this.email),
        memberStatus = this.memberStatus
    )

    companion object {
        fun from(member: Member) = MemberEntity(
            id = member.id,
            memberId = member.memberInfo.memberId,
            password = member.password!!,
            memberName = member.memberInfo.name,
            email = member.memberInfo.email,
            memberStatus = member.memberStatus,
            noShowCount = member.noShowCount,
            startNoShowTime = member.startNoShowTime
        )
    }
}
