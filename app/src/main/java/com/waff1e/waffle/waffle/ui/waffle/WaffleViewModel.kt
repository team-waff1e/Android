package com.waff1e.waffle.waffle.ui.waffle

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waff1e.waffle.comment.data.CommentRepository
import com.waff1e.waffle.comment.dto.CommentRequest
import com.waff1e.waffle.di.LIMIT
import com.waff1e.waffle.dto.DefaultResponse
import com.waff1e.waffle.dto.check
import com.waff1e.waffle.ui.navigation.NavigationDestination
import com.waff1e.waffle.utils.removeComment
import com.waff1e.waffle.waffle.data.WaffleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class WaffleViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val waffleRepository: WaffleRepository,
    private val commentRepository: CommentRepository,
) : ViewModel() {
    private val waffleId: Long =
        checkNotNull(savedStateHandle[NavigationDestination.Waffle.waffleId])
    var waffleUiState by mutableStateOf(WaffleUiState())
    var commentContent by mutableStateOf("")

    init {
        viewModelScope.launch {
            getWaffle()
            getCommentList()
        }
    }

    private suspend fun getWaffle() {
        val responseWaffleResult = waffleRepository.requestWaffle(waffleId)

        waffleUiState = if (responseWaffleResult.isSuccessful) {
            waffleUiState.copy(
                waffle = responseWaffleResult.body()!!,
            )
        } else {
            val body =
                Json.decodeFromString<DefaultResponse>(responseWaffleResult.errorBody()?.string()!!)
            waffleUiState.copy(errorCode = body.errorCode)
        }
    }

    suspend fun getCommentList() {
        val responseCommentListResult = commentRepository.getCommentList(waffleId, LIMIT)

        waffleUiState = if (responseCommentListResult.isSuccessful) {
            val newSet = waffleUiState.commentList.toMutableSet()
            newSet.addAll(responseCommentListResult.body()!!)
            val newList = newSet.sortedByDescending { it.updatedAt }.toMutableList()

            waffleUiState.copy(commentList = newList)
        } else {
            val body = Json.decodeFromString<DefaultResponse>(
                responseCommentListResult.errorBody()?.string()!!
            )
            waffleUiState.copy(errorCode = body.errorCode)
        }
    }

    suspend fun postComment() {
        val responseResult =
            commentRepository.postComment(waffleId, CommentRequest(commentContent)).check()

        if (responseResult.isSuccess) {
            getCommentList()
        } else {
            waffleUiState = waffleUiState.copy(errorCode = responseResult.body!!.errorCode)
        }
    }

    suspend fun removeWaffle(id: Long) {
        val responseResult = waffleRepository.deleteWaffle(id).check()

        if (!responseResult.isSuccess) {
            waffleUiState = waffleUiState.copy(errorCode = responseResult.body!!.errorCode)
        }
    }

    suspend fun removeComment(waffleId: Long, commentId: Long) {
        waffleUiState = waffleUiState.copy(commentList = waffleUiState.commentList.removeComment(commentId))

        val responseResult = commentRepository.deleteComment(waffleId, commentId).check()

        if (!responseResult.isSuccess) {
            waffleUiState = waffleUiState.copy(errorCode = responseResult.body!!.errorCode)
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