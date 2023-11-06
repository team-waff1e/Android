package com.waff1e.waffle.member.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Member(
    val id: Long? = null,
    val email: String? = null,
    val nickname: String? = null,
    @SerialName("profile_url") val profileUrl: String? = null,
)
