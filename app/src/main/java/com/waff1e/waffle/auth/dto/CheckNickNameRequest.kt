package com.waff1e.waffle.auth.dto

import kotlinx.serialization.Serializable

@Serializable
data class CheckNickNameRequest(
    val nickname: String
)