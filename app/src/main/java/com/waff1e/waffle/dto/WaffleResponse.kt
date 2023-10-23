package com.waff1e.waffle.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class WaffleResponse(
    val id: Long,
    val content: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val likes: Long,
    val comments: Long, // TODO. 댓글 수를 담은 변수 (임시!!!!)
    @SerialName("Member") val member: Member
)

@Serializable
data class Member(
    val nickname: String,
    @SerialName("profile_url") val profileUrl: String
)