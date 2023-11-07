package com.waff1e.waffle.waffle.ui.waffles

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waff1e.waffle.di.LIMIT
import com.waff1e.waffle.waffle.data.WaffleRepository
import com.waff1e.waffle.waffle.dto.WaffleListFailResponse
import com.waff1e.waffle.waffle.dto.WaffleListSuccessResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class WaffleListViewModel @Inject constructor(
    private val waffleRepository: WaffleRepository,
) : ViewModel() {
    var waffleListUiState by mutableStateOf(WaffleListUiState())
    private val idx: MutableState<Long?> = mutableStateOf(null)

    init {
        viewModelScope.launch {
            getWaffleList(true)
        }
    }

    suspend fun getWaffleList(isUpdate: Boolean) {
        val responseResult: Response<WaffleListSuccessResponse>? = if (isUpdate) {
            waffleRepository.requestWaffleList(LIMIT, true, null)
        } else if (idx.value != null) {
            waffleRepository.requestWaffleList(LIMIT, false, idx.value)
        } else {
            null
        }

        if (responseResult != null && responseResult.isSuccessful) {
            waffleListUiState = if (isUpdate) {
                waffleListUiState.copy(waffleList = responseResult.body()!!.list)
            } else {
                // TODO. 무한 스크롤 시연을 위해 아래 코드 주석 처리, 실제로는 MutableSet 사용 필요
//                val newSet = waffleListUiState.value.waffleList.toMutableSet()
//                newSet.addAll(responseResult.body()!!.list)
//                val newList = newSet.sortedByDescending { it.updatedAt }
                val newList = waffleListUiState.waffleList.toMutableList()
                newList.addAll(responseResult.body()!!.list)
                newList.sortedByDescending { it.updatedAt }
                waffleListUiState.copy(waffleList = newList)
            }

            idx.value = waffleListUiState.waffleList.last().id
        } else if (responseResult != null) {
            val body = Json.decodeFromString<WaffleListFailResponse>(
                responseResult.errorBody()?.string()!!
            )
            waffleListUiState = waffleListUiState.copy(errorCode = body.errorCode)
        }
    }
}