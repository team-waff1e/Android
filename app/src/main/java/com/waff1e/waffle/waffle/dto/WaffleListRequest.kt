package com.waff1e.waffle.waffle.dto

import kotlinx.serialization.Serializable

@Serializable
data class WaffleListRequest(
    val limit: Int = 20,
    val isUpdate: Boolean = true,
    val idx: Int? = null
)