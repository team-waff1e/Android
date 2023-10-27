package com.waff1e.waffle.waffle.ui.waffles

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waff1e.waffle.waffle.data.WaffleRepository
import com.waff1e.waffle.waffle.dto.WaffleListFailResponse
import com.waff1e.waffle.waffle.dto.WaffleListRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class WaffleListViewModel @Inject constructor(
    private val waffleRepository: WaffleRepository,
) : ViewModel() {
    val waffleListUiState = mutableStateOf(WaffleListUiState())

    init {
        viewModelScope.launch {
            getWaffleList(true)
        }
    }

    suspend fun getWaffleList(isUpdate: Boolean) {
        val waffleListRequest = WaffleListRequest(
            limit = LIMIT,
            isUpdate = isUpdate,
            idx = 0,
        )

        // TODO. GET 메소드에서 데이터 전송 방식 설정 필요
//        val responseResult = waffleRepository.requestWaffleList(waffleListRequest).check()
        val responseResult = waffleRepository.requestWaffleList()

        if (responseResult.isSuccessful) {
            waffleListUiState.value = waffleListUiState.value.copy(waffleList = responseResult.body()!!.list)
        } else {
            val body = Json.decodeFromString<WaffleListFailResponse>(responseResult.errorBody()?.string()!!)
            waffleListUiState.value = waffleListUiState.value.copy(errorCode = body.errorCode)
        }
    }

    companion object {
        const val LIMIT = 20
    }
}