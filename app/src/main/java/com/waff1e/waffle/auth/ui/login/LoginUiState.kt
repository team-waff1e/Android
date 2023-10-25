package com.waff1e.waffle.auth.ui.login

import com.waff1e.waffle.auth.dto.Login

// 로그인 상태 데이터 클래스
data class LoginUiState(
    val email: String = "",
    val pwd: String = "",
    val canLogin: Boolean = false
)

fun LoginUiState.toLogin() = Login(email = email, pwd = pwd)

fun LoginUiState.isEmailValid() = email.isNotBlank()
fun LoginUiState.isPwdValid() = pwd.isNotBlank()

// TODO. 이메일, 패스워드 조건 추가 필요
fun LoginUiState.isValid(): Boolean {
    val emailCheck = isEmailValid()
    val pwdCheck = isPwdValid()

    return emailCheck && pwdCheck
}