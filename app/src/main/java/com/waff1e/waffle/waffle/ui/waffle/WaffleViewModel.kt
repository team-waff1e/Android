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
        val responseWaffleResult = waffleRepository.requestWaffle(waffleId)

        waffleUiState = if (responseWaffleResult.isSuccessful) {
            waffleUiState.copy(
                waffle = responseWaffleResult.body()!!,
            )
        } else {
            val body = Json.decodeFromString<DefaultResponse>(responseWaffleResult.errorBody()?.string()!!)
            waffleUiState.copy(errorCode = body.errorCode)
        }
    }

    suspend fun requestWaffleLike(id: Long) {
        // TODO. 포스트맨으로만 테스트하면 에러남
//        val idx = waffleListUiState.waffleList.indexOfFirst { it.id == id }
//        val responseResult = waffleRepository.likeWaffle(id)
//
//        if (responseResult.isSuccessful) {
//            val waffle = responseResult.body()!!
//            waffleListUiState.waffleList[idx] = waffle
//        } else {
//            val body = Json.decodeFromString<WaffleListFailResponse>(
//                responseResult.errorBody()?.string()!!
//            )
//            waffleListUiState = waffleListUiState.copy(errorCode = body.errorCode)
//        }
    }
}