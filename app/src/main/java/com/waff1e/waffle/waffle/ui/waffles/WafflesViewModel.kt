package com.waff1e.waffle.waffle.ui.waffles

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waff1e.waffle.waffle.data.WaffleRepository
import com.waff1e.waffle.waffle.dto.WaffleListRequest
import com.waff1e.waffle.waffle.dto.WaffleResponse
import com.waff1e.waffle.waffle.dto.check
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WafflesViewModel @Inject constructor(
    private val waffleRepository: WaffleRepository,
) : ViewModel() {
    val waffleListUiState: MutableState<WaffleListUiState> = mutableStateOf(WaffleListUiState())

    init {
        viewModelScope.launch {
            getWaffleList(true)
        }
    }

    suspend fun getWaffleList(isUpdate: Boolean): List<WaffleResponse> {
        val waffleListRequest = WaffleListRequest(
            limit = LIMIT,
            isUpdate = isUpdate,
            idx = 0,
        )

        // TODO. GET 메소드에서 데이터 전송 방식 설정 필요
//        val responseResult = waffleRepository.requestWaffleList(waffleListRequest).check()
        val responseResult = waffleRepository.requestWaffleList().check()

        waffleListUiState.value = waffleListUiState.value.copy(waffleList = responseResult.list)

        return waffleListUiState.value.waffleList
    }

    companion object {
        const val LIMIT = 20
    }
}