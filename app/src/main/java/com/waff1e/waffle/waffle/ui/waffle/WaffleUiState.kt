package com.waff1e.waffle.waffle.ui.waffle

import com.waff1e.waffle.waffle.dto.WaffleResponse

data class WaffleUiState(
    val waffle: WaffleResponse? = null,
    val errorCode: Int? = null
)