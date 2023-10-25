package com.waff1e.waffle.waffles.dto

data class WaffleListRequest(
    val limit: Int,
    val isUpdate: Boolean,
    val idx: Int
)