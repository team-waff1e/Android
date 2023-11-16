package com.waff1e.waffle.waffle.ui.postwaffle

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.waff1e.waffle.dto.ResponseResult
import com.waff1e.waffle.dto.check
import com.waff1e.waffle.waffle.data.WaffleRepository
import com.waff1e.waffle.waffle.dto.PostWaffleRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.serialization.json.JsonNull.content
import javax.inject.Inject

@HiltViewModel
class PostWaffleViewModel @Inject constructor(
    private val waffleRepository: WaffleRepository
) : ViewModel() {
    private var _content by mutableStateOf("")
    val content: String get() = _content

    fun setContent(text: String) {
        _content = text
    }

    suspend fun postWaffle(): ResponseResult {
        val postWaffleRequest = PostWaffleRequest(
            content = content
        )

        return waffleRepository.postWaffle(postWaffleRequest = postWaffleRequest).check()
    }
}