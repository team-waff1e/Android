package com.waff1e.waffle.waffle.ui.waffles

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.waff1e.waffle.waffle.dto.Waffle

data class WaffleListUiState(
    val waffleList: List<Waffle> = listOf(),
    val errorCode: Int? = null
)