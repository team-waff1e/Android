package com.waff1e.waffle.dto

import kotlinx.serialization.Serializable

@Serializable
data class Signup(
    val email: String,
    val name: String,
    val password: String,
    val nickname: String
)