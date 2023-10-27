package com.waff1e.waffle.waffle.ui.waffle

import androidx.compose.runtime.mutableStateOf
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

    val waffleUiState = mutableStateOf(WaffleUiState())
    init {
        viewModelScope.launch {
            getWaffle()
        }
    }

    private suspend fun getWaffle() {
        val responseResult = waffleRepository.requestWaffle(waffleId)

        if (responseResult.isSuccessful) {
            waffleUiState.value = waffleUiState.value.copy(waffle = responseResult.body())
        } else {
            val body = Json.decodeFromString<DefaultResponse>(responseResult.errorBody()?.string()!!)
            waffleUiState.value = waffleUiState.value.copy(errorCode = body.errorCode)
        }
    }
}