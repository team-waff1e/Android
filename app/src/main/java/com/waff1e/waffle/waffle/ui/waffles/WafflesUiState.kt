package com.waff1e.waffle.waffle.ui.waffles

import com.waff1e.waffle.waffle.dto.Member
import com.waff1e.waffle.waffle.dto.WaffleResponse
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.LocalDateTime

data class WaffleListUiState(val waffleList: List<WaffleResponse> = listOf())