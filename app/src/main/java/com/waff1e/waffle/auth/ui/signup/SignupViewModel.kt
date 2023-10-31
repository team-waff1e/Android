package com.waff1e.waffle.auth.ui.signup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.waff1e.waffle.auth.data.AuthRepository
import com.waff1e.waffle.auth.dto.CheckEmailRequest
import com.waff1e.waffle.auth.dto.CheckNickNameRequest
import com.waff1e.waffle.dto.ResponseResult
import com.waff1e.waffle.dto.check
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    var signupUiState by mutableStateOf(SignupUiState())
        private set

    fun updateSignupUiState(newSignupUiState: SignupUiState) {
        signupUiState = newSignupUiState.copy(
            canSignup = newSignupUiState.isValid()
        )
    }

    suspend fun checkEmail() {
        val responseResult = authRepository.checkEmailStream(CheckEmailRequest(signupUiState.email)).check()
        val checkSignupUiState = signupUiState.copy(canEmail = responseResult.isSuccess)
        updateSignupUiState(checkSignupUiState)
    }

    suspend fun checkNickname() {
        val responseResult = authRepository.checkNickNameStream(CheckNickNameRequest(signupUiState.nickname)).check()
        val checkSignupUiState = signupUiState.copy(canNickname = responseResult.isSuccess)
        updateSignupUiState(checkSignupUiState)
    }

    suspend fun requestSignup(): ResponseResult {
        return authRepository.signup(signupUiState.toSignupRequest()).check()
    }
}