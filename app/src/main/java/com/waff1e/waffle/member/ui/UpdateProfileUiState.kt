package com.waff1e.waffle.member.ui

data class UpdateProfileUiState(
    val nickname: String = "",
    val pwd: String = "",
    val canUpdate: Boolean = false
)
