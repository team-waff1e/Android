package com.waff1e.waffle.waffle.ui.editwaffle

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waff1e.waffle.dto.DefaultResponse
import com.waff1e.waffle.dto.ResponseResult
import com.waff1e.waffle.dto.check
import com.waff1e.waffle.ui.navigation.NavigationDestination
import com.waff1e.waffle.waffle.data.WaffleRepository
import com.waff1e.waffle.waffle.dto.PostWaffleRequest
import com.waff1e.waffle.waffle.ui.waffle.WaffleUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class EditWaffleViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val waffleRepository: WaffleRepository,
) : ViewModel() {
    private val waffleId: Long = checkNotNull(savedStateHandle[NavigationDestination.EditWaffle.WAFFLE_ID])
    var waffleUiState by mutableStateOf(WaffleUiState())
    private var _content by mutableStateOf("")
    val content: String get() = _content

    init {
        viewModelScope.launch {
            getWaffle()
            setContent(waffleUiState.waffle!!.content)
        }
    }

    fun setContent(text: String) {
        _content = text
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

    suspend fun updateWaffle(): ResponseResult {
        val postWaffleRequest = PostWaffleRequest(
            content = content
        )

        return waffleRepository.updateWaffle(waffleId, updateWaffleRequest = postWaffleRequest).check()
    }
}