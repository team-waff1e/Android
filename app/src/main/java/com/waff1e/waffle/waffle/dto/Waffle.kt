package com.waff1e.waffle.waffle.dto

import com.waff1e.waffle.member.dto.Member
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.serializers.LocalDateTimeIso8601Serializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Waffle(
    val id: Long,
    val content: String,
    @Serializable(with = LocalDateTimeIso8601Serializer::class)
    val createdAt: LocalDateTime,
    @Serializable(with = LocalDateTimeIso8601Serializer::class)
    val updatedAt: LocalDateTime,
    val likes: Long,
    val comments: Long, // TODO. 댓글 수를 담은 변수 (임시!!!!)
    @SerialName("Member") val member: Member,
    val isLike: Boolean,
)