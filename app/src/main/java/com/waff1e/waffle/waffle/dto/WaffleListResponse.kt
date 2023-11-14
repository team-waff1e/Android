package com.waff1e.waffle.waffle.dto

import kotlinx.serialization.Serializable

@Serializable
data class WaffleListSuccessResponse(
    val contents: MutableList<Waffle> = mutableListOf(),
    val lastIdx: Long,
    val size: Int,
    val last: Boolean,
)