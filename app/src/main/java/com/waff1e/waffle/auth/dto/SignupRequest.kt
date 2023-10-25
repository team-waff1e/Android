package com.waff1e.waffle.auth.dto

import kotlinx.serialization.Serializable

@Serializable
data class SignupRequest(
    val email: String,
    val name: String,
    val password: String,
    val nickname: String
)