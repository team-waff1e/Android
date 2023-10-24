package com.waff1e.waffle.ui.login

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.waff1e.waffle.data.WaffleRepository
import com.waff1e.waffle.dto.DefaultResponse
import com.waff1e.waffle.dto.Login
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import retrofit2.Response

import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val waffleRepository: WaffleRepository,
) : ViewModel() {
    var loginUiState by mutableStateOf(LoginUiState())
        private set

    fun updateLoginUiState(newLoginUiState: LoginUiState) {
        loginUiState = newLoginUiState.copy(
            canLogin = newLoginUiState.isValid()
        )
    }

    suspend fun requestLogin(): Int? {
        val login = Login(
            email = loginUiState.email,
            pwd = loginUiState.pwd
        )

        val jsonLogin = Json.encodeToJsonElement(login)

        val response = waffleRepository.login(jsonLogin)

        var errorCode: Int? = null

        // TODO. 개발용 로그, 추후 삭제 필요
        if (response.isSuccessful) {
            Log.d("로그", "LoginScreen() - 로그인 성공")
            Log.d("로그", "${response.code()}")
        } else {
            Log.d("로그", "LoginScreen() - 로그인 실패")
            Log.d("로그", "response.code() : ${response.code()}")
            val body = response.errorBody()?.string()
            val jsonToObjectBody = Json.decodeFromString<DefaultResponse>(body!!)
            errorCode = jsonToObjectBody.errorCode
            Log.d("로그", "response.errorCode : $errorCode")

        }

        return errorCode
    }
}