package com.waff1e.waffle.waffle.dto

import kotlinx.serialization.Serializable

@Serializable
data class WaffleListRequest(
    val limit: Int,
    val isUpdate: Boolean,
    val idx: Int
)