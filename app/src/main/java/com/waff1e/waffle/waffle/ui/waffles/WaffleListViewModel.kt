package com.waff1e.waffle.waffle.ui.waffles

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide.init
import com.waff1e.waffle.di.LIMIT
import com.waff1e.waffle.utils.updateLikes
import com.waff1e.waffle.waffle.data.WaffleRepository
import com.waff1e.waffle.waffle.dto.WaffleListFailResponse
import com.waff1e.waffle.waffle.dto.WaffleListSuccessResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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
    private var isLast = false

    init {
        viewModelScope.launch {
            getWaffleList(true)
        }
    }

    suspend fun getWaffleList(isUpdate: Boolean) {
        val responseResult: Response<WaffleListSuccessResponse>? = if (isUpdate) {
            waffleRepository.requestWaffleList(LIMIT, true, null)
        } else if (idx.value != null && !isLast) {
            waffleRepository.requestWaffleList(LIMIT, false, idx.value)
        } else {
            null
        }

        if (responseResult != null && responseResult.isSuccessful) {
            waffleListUiState = if (isUpdate) {
                waffleListUiState.copy(waffleList = responseResult.body()!!.contents)
            } else {
                val newSet = waffleListUiState.waffleList.toMutableSet()
                newSet.addAll(responseResult.body()!!.contents)
                val newList = newSet.sortedByDescending { it.updatedAt }.toMutableList()
                waffleListUiState.copy(waffleList = newList)
            }

            idx.value = responseResult.body()!!.lastIdx
            isLast = responseResult.body()!!.last
        } else if (responseResult != null) {
            val body = Json.decodeFromString<WaffleListFailResponse>(
                responseResult.errorBody()?.string()!!
            )
            waffleListUiState = waffleListUiState.copy(errorCode = body.errorCode)
        }
    }

    suspend fun requestWaffleLike(id: Long) {
        // TODO. 포스트맨으로만 테스트하면 에러남
        val idx = waffleListUiState.waffleList.indexOfFirst { it.id == id }
        if (waffleListUiState.waffleList[idx].liked) {

        } else {

        }

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