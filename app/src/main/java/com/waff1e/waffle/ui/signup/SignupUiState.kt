package com.waff1e.waffle.ui.signup

import com.waff1e.waffle.dto.Signup

data class SignupUiState(
    val email: String = "",
    val name: String = "",
    val password: String = "",
    val passwordConfirm: String = "",
    val nickname: String = "",
    val canSignup: Boolean = false
)

fun SignupUiState.toSignup() = Signup(
    email = email,
    name = name,
    password =  password,
    nickname = nickname
)

// TODO. 패스워드 조건 추가 필요
fun SignupUiState.isPasswordMatch() = password == passwordConfirm
fun SignupUiState.isPasswordValid(): Boolean {
    return password.isNotBlank() && isPasswordMatch()
}
fun SignupUiState.isEmailValid() = email.isNotBlank()
fun SignupUiState.isNameValid() = name.isNotBlank()
fun SignupUiState.isNicknameValid() = nickname.isNotBlank()

fun SignupUiState.isValid(): Boolean {
    val emailCheck = isEmailValid()
    val passwordCheck = isPasswordValid()
    val nameCheck = isNameValid()
    val nicknameCheck = isNicknameValid()

    return emailCheck && passwordCheck && nameCheck && nicknameCheck
}