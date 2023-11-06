package com.waff1e.waffle.waffle.dto

import kotlinx.serialization.Serializable

@Serializable
data class WaffleListSuccessResponse(
    val list: List<Waffle> = listOf(),
)

@Serializable
data class WaffleListFailResponse(
    val errorCode: Int
)