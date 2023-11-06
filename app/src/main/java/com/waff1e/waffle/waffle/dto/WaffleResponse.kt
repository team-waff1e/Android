package com.waff1e.waffle.waffle.dto

import kotlinx.serialization.Serializable

// TODO. Comment DTO 추가 필요
@Serializable
data class WaffleResponse(
    val waffle: Waffle,
    val commentList: List<String> = listOf()
)