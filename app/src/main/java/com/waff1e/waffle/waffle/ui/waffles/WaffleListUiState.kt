package com.waff1e.waffle.waffle.ui.waffles

import com.waff1e.waffle.waffle.dto.Waffle

data class WaffleListUiState(
    val waffleList: List<Waffle> = listOf(),
    val errorCode: Int? = null
)