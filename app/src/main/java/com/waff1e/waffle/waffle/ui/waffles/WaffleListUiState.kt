package com.waff1e.waffle.waffle.ui.waffles

import com.waff1e.waffle.waffle.dto.WaffleResponse

data class WaffleListUiState(
    val waffleList: List<WaffleResponse> = listOf(),
    val errorCode: Int? = null
)