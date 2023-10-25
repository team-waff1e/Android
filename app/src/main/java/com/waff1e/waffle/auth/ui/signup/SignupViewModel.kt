package com.waff1e.waffle.auth.ui.signup

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.waff1e.waffle.auth.data.AuthRepository
import com.waff1e.waffle.auth.dto.CheckEmailRequest
import com.waff1e.waffle.auth.dto.CheckNickNameRequest
import com.waff1e.waffle.auth.dto.SignupRequest
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

    var emailTerm = mutableStateOf("")
    var nicknameTerm = mutableStateOf("")

    fun updateSignupUiState(newSignupUiState: SignupUiState) {
        signupUiState = newSignupUiState.copy(
            canSignup = newSignupUiState.isValid()
        )
    }

    suspend fun checkEmail(): ResponseResult {
        val responseResult = authRepository.checkEmailStream(CheckEmailRequest(signupUiState.email)).check()
        val checkSignupUiState = signupUiState.copy(email = emailTerm.value, canEmail = responseResult.isSuccess)
        updateSignupUiState(checkSignupUiState)
        return responseResult
    }

    suspend fun checkNickname(): ResponseResult {
        val responseResult = authRepository.checkNickNameStream(CheckNickNameRequest(nicknameTerm.value)).check()
        val checkSignupUiState = signupUiState.copy(nickname = nicknameTerm.value, canNickname = responseResult.isSuccess)
        updateSignupUiState(checkSignupUiState)
        return responseResult
    }

    suspend fun requestSignup(): ResponseResult {
        return authRepository.signup(signupUiState.toSignupRequest()).check()
    }
}