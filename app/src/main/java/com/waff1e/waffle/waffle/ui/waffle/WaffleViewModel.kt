package com.waff1e.waffle.waffle.ui.waffle

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waff1e.waffle.dto.DefaultResponse
import com.waff1e.waffle.ui.navigation.NavigationDestination
import com.waff1e.waffle.waffle.data.WaffleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class WaffleViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val waffleRepository: WaffleRepository
) : ViewModel() {
    private val waffleId: Long = checkNotNull(savedStateHandle[NavigationDestination.Waffle.waffleArg])
    var waffleUiState by mutableStateOf(WaffleUiState())

    init {
        viewModelScope.launch {
            getWaffle()
        }
    }

    private suspend fun getWaffle() {
        val responseResult = waffleRepository.requestWaffle(waffleId)

        waffleUiState = if (responseResult.isSuccessful) {
            waffleUiState.copy(
                waffle = responseResult.body()!!.waffle,
                commentList = responseResult.body()!!.commentList
            )
        } else {
            val body = Json.decodeFromString<DefaultResponse>(responseResult.errorBody()?.string()!!)
            waffleUiState.copy(errorCode = body.errorCode)
        }
    }
}