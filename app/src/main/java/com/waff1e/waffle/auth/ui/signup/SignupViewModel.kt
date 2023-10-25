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

    var emailTerm = mutableStateOf("")
    var nicknameTerm = mutableStateOf("")

    fun updateSignupUiState(newSignupUiState: SignupUiState) {
        signupUiState = newSignupUiState.copy(
            canSignup = newSignupUiState.isValid()
        )
    }

    suspend fun checkEmail(): ResponseResult {
        signupUiState.copy(email = emailTerm.value)
        return authRepository.checkEmailStream(CheckEmailRequest(emailTerm.value)).check()
    }

    suspend fun checkNickname(): ResponseResult {
        signupUiState.copy(nickname = nicknameTerm.value)
        return authRepository.checkNickNameStream(CheckNickNameRequest(nicknameTerm.value)).check()
    }

    suspend fun requestSignup() {

    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}