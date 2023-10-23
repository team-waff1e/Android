package com.waff1e.waffle.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.waff1e.waffle.data.WaffleRepository
import com.waff1e.waffle.dto.Login
import com.waff1e.waffle.dto.LoginResponse
import dagger.hilt.android.lifecycle.HiltViewModel

import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val waffleRepository: WaffleRepository
) : ViewModel() {
    var loginUiState by mutableStateOf(LoginUiState())
        private set

    fun updateLoginUiState(newLoginUiState: LoginUiState) {
        loginUiState = newLoginUiState.copy(
            canLogin = newLoginUiState.isValid()
        )
    }

    suspend fun requestLogin() : LoginResponse {
        val login = Login(
            email = loginUiState.email,
            pwd = loginUiState.pwd
        )

        return waffleRepository.login(login)
    }
}