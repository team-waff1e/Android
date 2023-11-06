package com.waff1e.waffle.waffle.ui.waffle

import com.waff1e.waffle.waffle.dto.Waffle

data class WaffleUiState(
    val waffle: Waffle? = null,
    val commentList: List<String> = listOf(),
    val errorCode: Int? = null
)