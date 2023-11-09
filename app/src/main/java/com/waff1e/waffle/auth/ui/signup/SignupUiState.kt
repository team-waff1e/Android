package com.waff1e.waffle.auth.ui.signup

import com.waff1e.waffle.auth.dto.SignupRequest
import com.waff1e.waffle.di.NAME_MAX_LENGTH
import com.waff1e.waffle.di.NICKNAME_MAX_LENGTH
import com.waff1e.waffle.di.PASSWORD_RULE
import java.util.regex.Pattern

data class SignupUiState(
    val email: String = "",
    val name: String = "",
    val password: String = "",
    val passwordConfirm: String = "",
    val nickname: String = "",
    val canEmail: Boolean = true,
    val canNickname: Boolean = true,
    val canSignup: Boolean = false
)

fun SignupUiState.toSignupRequest() = SignupRequest(
    email = email,
    name = name,
    password =  password,
    nickname = nickname
)

fun SignupUiState.isPasswordValid(): Boolean {
    return Pattern.matches(PASSWORD_RULE, password)
}
fun SignupUiState.isPasswordMatch(): Boolean {
    return password == passwordConfirm
}
fun SignupUiState.isPasswordIsNotBlank(): Boolean {
    return password.isNotBlank()
}
fun SignupUiState.isEmailValid(): Boolean {
    return email.isNotBlank() && canEmail
}
fun SignupUiState.isNameValid(): Boolean {
    return name.isNotBlank() && name.length <= NAME_MAX_LENGTH
}
fun SignupUiState.isNicknameValid(): Boolean {
    return nickname.isNotBlank() && canNickname && nickname.length <= NICKNAME_MAX_LENGTH
}

fun SignupUiState.isValid(): Boolean {
    val emailCheck = isEmailValid()
    val passwordCheck = isPasswordValid() && isPasswordIsNotBlank() && isPasswordMatch()
    val nameCheck = isNameValid()
    val nicknameCheck = isNicknameValid()

    return emailCheck && passwordCheck && nameCheck && nicknameCheck
}