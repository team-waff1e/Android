package com.waff1e.waffle.auth.dto

import kotlinx.serialization.Serializable

@Serializable
data class Login(
    val email: String,
    val pwd: String
)
