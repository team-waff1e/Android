package com.waff1e.waffle.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waff1e.waffle.data.WaffleRepository
import com.waff1e.waffle.dto.Login
import com.waff1e.waffle.dto.LoginResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
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

    suspend fun requestLogin() : LoginResponse? {
        val login = Login(
            email = loginUiState.email,
            pwd = loginUiState.pwd
        )

        return waffleRepository.login(login)
    }
}