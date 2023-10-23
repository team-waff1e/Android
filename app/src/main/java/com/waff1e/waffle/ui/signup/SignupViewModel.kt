package com.waff1e.waffle.ui.signup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waff1e.waffle.data.WaffleRepository
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
class SignupViewModel @Inject constructor(
    private val waffleRepository: WaffleRepository
) : ViewModel() {
    var signupUiState by mutableStateOf(SignupUiState())
        private set

    val emailTerm = MutableStateFlow("")
    val nicknameTerm = MutableStateFlow("")

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val debounceEmailTerm = emailTerm
        .debounce(350)
        .distinctUntilChanged()
        .flatMapLatest { emailTermValue ->
            waffleRepository.checkEmailStream(emailTermValue)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = ""
        )

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val debounceNicknameTerm = nicknameTerm
        .debounce(350)
        .distinctUntilChanged()
        .flatMapLatest { nicknameTermValue ->
            waffleRepository.checkNickNameStream(nicknameTermValue)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = ""
        )

    fun updateSignupUiState(newSignupUiState: SignupUiState) {
        signupUiState = newSignupUiState.copy(
            canSignup = newSignupUiState.isValid()
        )
    }

    suspend fun requestSignup() {

    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}