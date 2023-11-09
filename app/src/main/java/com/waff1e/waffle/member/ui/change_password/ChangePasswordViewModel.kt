package com.waff1e.waffle.member.ui.change_password

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.waff1e.waffle.dto.ResponseResult
import com.waff1e.waffle.dto.check
import com.waff1e.waffle.member.data.MemberRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val memberRepository: MemberRepository,
) : ViewModel() {
    var changePasswordUiState by mutableStateOf(ChangePasswordUiState())

    fun updateChangePasswordUiState(newChangePasswordUiState: ChangePasswordUiState) {
        changePasswordUiState = newChangePasswordUiState.copy(
            canUpdate = newChangePasswordUiState.isValid()
        )
    }

    private suspend fun requestCheckPassword(): ResponseResult {
        return memberRepository.checkPassword(changePasswordUiState.toCheckPasswordRequest()).check()
    }

    suspend fun requestUpdatePassword(): ResponseResult {
        val checkPasswordResponseResult = requestCheckPassword()

        return if (checkPasswordResponseResult.isSuccess) {
            memberRepository.updatePassword(changePasswordUiState.toUpdatePasswordRequest()).check()
        } else {
            checkPasswordResponseResult
        }
    }
}
