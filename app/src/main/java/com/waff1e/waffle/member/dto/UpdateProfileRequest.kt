package com.waff1e.waffle.member.dto

import kotlinx.serialization.Serializable

@Serializable
data class UpdateProfileRequest(
    val nickname: String,
    val pwd: String
)
