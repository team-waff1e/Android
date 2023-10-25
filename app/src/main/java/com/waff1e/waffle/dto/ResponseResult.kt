package com.waff1e.waffle.dto

import com.waff1e.waffle.dto.DefaultResponse

data class ResponseResult(
    var isSuccess: Boolean = true,
    var statusCode: Int = 0,
    var body: DefaultResponse? = null
)