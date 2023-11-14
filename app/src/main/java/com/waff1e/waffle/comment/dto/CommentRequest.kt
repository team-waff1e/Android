package com.waff1e.waffle.comment.dto

import kotlinx.serialization.Serializable

@Serializable
data class CommentRequest(
    val content: String
)
