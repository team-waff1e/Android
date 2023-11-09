package com.waff1e.waffle.member.ui.change_nickname

import com.waff1e.waffle.auth.dto.CheckNickNameRequest
import com.waff1e.waffle.member.dto.NicknameRequest

data class ChangeNicknameUiState(
    val nickname: String = "",
    val canUpdate: Boolean = false,
    val canNickname: Boolean = true
)

fun ChangeNicknameUiState.toNicknameRequest() = NicknameRequest(
    nickname = nickname
)

fun ChangeNicknameUiState.toCheckNicknameRequest() = CheckNickNameRequest(
    nickname = nickname
)

fun ChangeNicknameUiState.isNicknameValid(): Boolean {
    return nickname.isNotBlank() && nickname.length < 20
}

fun ChangeNicknameUiState.isValid(): Boolean {
    return isNicknameValid() && canNickname
}