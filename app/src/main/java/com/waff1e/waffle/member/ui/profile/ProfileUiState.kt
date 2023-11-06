package com.waff1e.waffle.member.ui.profile

import com.waff1e.waffle.member.dto.Member

data class ProfileUiState(
    val member: Member? = null,
    val errorCode: Int? = null
)
