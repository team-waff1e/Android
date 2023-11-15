package com.waff1e.waffle.comment.ui.editcomment

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waff1e.waffle.comment.data.CommentRepository
import com.waff1e.waffle.comment.dto.CommentRequest
import com.waff1e.waffle.dto.DefaultResponse
import com.waff1e.waffle.dto.ResponseResult
import com.waff1e.waffle.dto.check
import com.waff1e.waffle.ui.navigation.NavigationDestination
import com.waff1e.waffle.waffle.dto.PostWaffleRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class EditCommentViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val commentRepository: CommentRepository,
): ViewModel() {
    val waffleId: Long = checkNotNull(savedStateHandle[NavigationDestination.EditComment.waffleId])
    private val commentId: Long = checkNotNull(savedStateHandle[NavigationDestination.EditComment.commentId])
    var editCommentUiState = EditCommentUiState()
    private var _content by mutableStateOf("")
    val content: String get() = _content

    init {
        viewModelScope.launch {
            getComment()
        }
    }

    fun setContent(text: String) {
        _content = text
    }

    private suspend fun getComment() {
        val responseResult = commentRepository.getCommentDetail(waffleId, commentId)

        editCommentUiState = if (responseResult.isSuccessful) {
            editCommentUiState.copy(content = responseResult.body()!!.content)
        } else {
            val body = Json.decodeFromString<DefaultResponse>(responseResult.errorBody()?.string()!!)
            editCommentUiState.copy(errorCode = body.errorCode)
        }
    }

    suspend fun updateComment(): ResponseResult {
        val postCommentRequest = CommentRequest(
            content = content
        )

        return commentRepository.updateComment(waffleId, commentId, postCommentRequest).check()
    }
}