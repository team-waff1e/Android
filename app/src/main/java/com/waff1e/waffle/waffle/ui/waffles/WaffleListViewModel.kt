package com.waff1e.waffle.waffle.ui.waffles

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waff1e.waffle.waffle.data.WaffleRepository
import com.waff1e.waffle.waffle.dto.WaffleListFailResponse
import com.waff1e.waffle.waffle.dto.WaffleListSuccessResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import retrofit2.Response
import java.util.Collections.addAll
import javax.inject.Inject

@HiltViewModel
class WaffleListViewModel @Inject constructor(
    private val waffleRepository: WaffleRepository,
) : ViewModel() {
    val waffleListUiState = mutableStateOf(WaffleListUiState())
    val idx: MutableState<Long?> = mutableStateOf(null)

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
            if (isUpdate) {
                waffleListUiState.value =
                    waffleListUiState.value.copy(waffleList = responseResult.body()!!.list)
            } else {
                // TODO. 무한 스크롤 시연을 위해 아래 코드 주석 처리, 실제로는 MutableSet 사용 필요
//                val newSet = waffleListUiState.value.waffleList.toMutableSet()
//                newSet.addAll(responseResult.body()!!.list)
//                val newList = newSet.sortedByDescending { it.updatedAt }
                val newList = waffleListUiState.value.waffleList.toMutableList()
                newList.addAll(responseResult.body()!!.list)
                newList.sortedByDescending { it.updatedAt }
                waffleListUiState.value = waffleListUiState.value.copy(waffleList = newList)
            }

            idx.value = waffleListUiState.value.waffleList.last().id
        } else if (responseResult != null) {
            val body = Json.decodeFromString<WaffleListFailResponse>(
                responseResult.errorBody()?.string()!!
            )
            waffleListUiState.value = waffleListUiState.value.copy(errorCode = body.errorCode)
        }
    }

    companion object {
        const val LIMIT = 20
    }
}