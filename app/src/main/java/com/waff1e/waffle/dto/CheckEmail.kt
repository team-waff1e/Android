package com.waff1e.waffle.dto

import kotlinx.serialization.Serializable

@Serializable
data class CheckEmail(
    val email: String
)