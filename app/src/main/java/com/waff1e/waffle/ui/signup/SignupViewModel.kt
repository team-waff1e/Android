package com.waff1e.waffle.ui.signup

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waff1e.waffle.data.WaffleRepository
import com.waff1e.waffle.dto.CheckEmail
import com.waff1e.waffle.dto.CheckNickName
import com.waff1e.waffle.dto.DefaultResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val waffleRepository: WaffleRepository
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

    suspend fun checkEmail() {
        signupUiState.copy(email = emailTerm.value)

        Log.d("로그", "emailTerm.value : ${emailTerm.value}")
        val response = waffleRepository.checkEmailStream(CheckEmail(emailTerm.value))
        var errorCode: Int? = null

        // TODO. 개발용 로그, 추후 삭제 필요
        if (response.isSuccessful) {
            Log.d("로그", "checkEmail() - 이메일 체크 성공")
            Log.d("로그", "${response.code()}")
        } else {
            Log.d("로그", "checkEmail() - 이메일 체크 실패")
            Log.d("로그", "response.code() : ${response.code()}")
            val body = response.errorBody()?.string()
            val jsonToObjectBody = Json.decodeFromString<DefaultResponse>(body!!)
            errorCode = jsonToObjectBody.errorCode
            Log.d("로그", "response.errorCode : $errorCode")
        }
    }

    suspend fun checkNickname() {
        signupUiState.copy(nickname = nicknameTerm.value)

        Log.d("로그", "emailTerm.value : ${nicknameTerm.value}")
        val response =waffleRepository.checkNickNameStream(CheckNickName(nicknameTerm.value))
        var errorCode: Int? = null

        // TODO. 개발용 로그, 추후 삭제 필요
        if (response.isSuccessful) {
            Log.d("로그", "checkNickname() - 닉네임 체크 성공")
            Log.d("로그", "${response.code()}")
        } else {
            Log.d("로그", "checkNickname() - 닉네임 체크 실패")
            Log.d("로그", "response.code() : ${response.code()}")
            val body = response.errorBody()?.string()
            val jsonToObjectBody = Json.decodeFromString<DefaultResponse>(body!!)
            errorCode = jsonToObjectBody.errorCode
            Log.d("로그", "response.errorCode : $errorCode")
        }
    }

    suspend fun requestSignup() {

    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}