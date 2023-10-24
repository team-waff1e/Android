package com.waff1e.waffle.dto

import kotlinx.serialization.Serializable

@Serializable
data class DefaultResponse(
    val errorCode: Int
)