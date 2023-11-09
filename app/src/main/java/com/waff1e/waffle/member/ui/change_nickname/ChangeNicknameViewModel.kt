package com.waff1e.waffle.member.ui.change_nickname

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.waff1e.waffle.auth.data.AuthRepository
import com.waff1e.waffle.dto.ResponseResult
import com.waff1e.waffle.dto.check
import com.waff1e.waffle.member.data.MemberRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChangeNicknameViewModel @Inject constructor(
    private val memberRepository: MemberRepository,
    private val authRepository: AuthRepository,
): ViewModel() {
    var changeNicknameUiState by mutableStateOf(ChangeNicknameUiState())

    fun updateChangeNicknameUiState(newChangeNicknameUiState: ChangeNicknameUiState) {
        changeNicknameUiState = newChangeNicknameUiState.copy(
            canUpdate = newChangeNicknameUiState.isValid()
        )
    }

    suspend fun checkNickname() {
        val responseResult = authRepository.checkNickName(changeNicknameUiState.toCheckNicknameRequest()).check()
        val checkChangeNicknameUiState = changeNicknameUiState.copy(canNickname = responseResult.isSuccess)
        updateChangeNicknameUiState(checkChangeNicknameUiState)
    }

    suspend fun requestChangeNickname(): ResponseResult {
        return memberRepository.updateNickname(changeNicknameUiState.toNicknameRequest()).check()
    }
}