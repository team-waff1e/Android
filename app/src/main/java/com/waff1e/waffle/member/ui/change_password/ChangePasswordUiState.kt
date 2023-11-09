package com.waff1e.waffle.member.ui.change_password

import com.waff1e.waffle.di.PASSWORD_RULE
import com.waff1e.waffle.member.dto.PasswordRequest
import java.util.regex.Pattern

data class ChangePasswordUiState(
    val currentPwd: String = "",
    val newPwd: String = "",
    val newPwdConfirm: String = "",
    val canUpdate: Boolean = false,
    val isCurrentPwdError: Boolean = false,
)

fun ChangePasswordUiState.isMatchNewPwdConfirm(): Boolean {
    return newPwd == newPwdConfirm
}

fun ChangePasswordUiState.isNewPwdValid(): Boolean {
    return Pattern.matches(PASSWORD_RULE, newPwd)
}

fun ChangePasswordUiState.isValid(): Boolean {
    return isMatchNewPwdConfirm() && isNewPwdValid()
}

fun ChangePasswordUiState.toCheckPasswordRequest() = PasswordRequest(pwd = currentPwd)

fun ChangePasswordUiState.toUpdatePasswordRequest() = PasswordRequest(pwd = newPwd)