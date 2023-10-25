package com.waff1e.waffle.auth.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.waff1e.waffle.auth.data.AuthRepository
import com.waff1e.waffle.dto.ResponseResult
import com.waff1e.waffle.dto.check
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {
    var loginUiState by mutableStateOf(LoginUiState())
        private set

    fun updateLoginUiState(newLoginUiState: LoginUiState) {
        loginUiState = newLoginUiState.copy(
            canLogin = newLoginUiState.isValid()
        )
    }

    suspend fun requestLogin(): ResponseResult {
        return authRepository.login(loginUiState.toLoginRequest()).check()
    }
}