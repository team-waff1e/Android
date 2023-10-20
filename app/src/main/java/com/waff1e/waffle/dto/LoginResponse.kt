package com.waff1e.waffle.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val result: LoginResponseResult,
    val isSuccess: Boolean,
    val code: Int,
    val message: String
)

@Serializable
data class LoginResponseResult(
    val jwt: String
)
