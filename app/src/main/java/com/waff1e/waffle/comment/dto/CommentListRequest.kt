package com.waff1e.waffle.comment.dto

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.serializers.LocalDateTimeIso8601Serializer
import kotlinx.serialization.Serializable

@Serializable
data class CommentListRequest(
    val limit: Int
)
