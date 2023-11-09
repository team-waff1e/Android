package com.waff1e.waffle.member.dto

import kotlinx.serialization.Serializable

@Serializable
data class NicknameRequest(
    val nickname: String
)
