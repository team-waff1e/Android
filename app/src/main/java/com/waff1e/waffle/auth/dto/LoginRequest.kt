package com.waff1e.waffle.auth.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    val pwd: String
)
