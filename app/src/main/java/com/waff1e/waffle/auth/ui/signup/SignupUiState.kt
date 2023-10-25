package com.waff1e.waffle.auth.ui.signup

import com.waff1e.waffle.auth.dto.SignupRequest

data class SignupUiState(
    val email: String = "",
    val name: String = "",
    val password: String = "",
    val passwordConfirm: String = "",
    val nickname: String = "",
    val canEmail: Boolean = false,
    val canNickname: Boolean = false,
    val canSignup: Boolean = false
)

fun SignupUiState.toSignupRequest() = SignupRequest(
    email = email,
    name = name,
    password =  password,
    nickname = nickname
)

// TODO. 패스워드 조건 추가 필요
fun SignupUiState.isPasswordMatch(): Boolean {
    return passwordConfirm.isNotBlank() && password == passwordConfirm
}
fun SignupUiState.isPasswordValid(): Boolean {
    return password.isNotBlank() && isPasswordMatch()
}
fun SignupUiState.isEmailValid(): Boolean {
    return email.isNotBlank() && canEmail
}
fun SignupUiState.isNameValid() = name.isNotBlank()
fun SignupUiState.isNicknameValid(): Boolean {
    return nickname.isNotBlank() && canNickname
}

fun SignupUiState.isValid(): Boolean {
    val emailCheck = isEmailValid()
    val passwordCheck = isPasswordValid()
    val nameCheck = isNameValid()
    val nicknameCheck = isNicknameValid()

    return emailCheck && passwordCheck && nameCheck && nicknameCheck
}