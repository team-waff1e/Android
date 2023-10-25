package com.waff1e.waffle.auth.dto

import kotlinx.serialization.Serializable

@Serializable
data class CheckNickName(
    val nickname: String
)