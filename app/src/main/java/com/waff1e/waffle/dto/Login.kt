package com.waff1e.waffle.dto

import kotlinx.serialization.Serializable

@Serializable
data class Login(
    val email: String,
    val pwd: String
)
