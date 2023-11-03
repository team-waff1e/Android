package com.waff1e.waffle.waffle.dto

import kotlinx.serialization.Serializable

@Serializable
data class PostWaffleRequest(
    val memberId: Long,
    val content: String = ""
)