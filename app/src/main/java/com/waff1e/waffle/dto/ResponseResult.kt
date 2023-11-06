package com.waff1e.waffle.dto

import kotlinx.serialization.json.Json
import retrofit2.Response

// DefaultResponse에 대한 결과를 담는 클래스
data class ResponseResult(
    var isSuccess: Boolean = true,
    var statusCode: Int = 0,
    var body: DefaultResponse? = null
)

// Retrofit 응답 결과에 따른 처리 메소드
fun Response<DefaultResponse>.check(): ResponseResult {
    val responseResult = ResponseResult(isSuccess = isSuccessful, statusCode = code())

    if (!this.isSuccessful) {
        val body = errorBody()?.string()
        responseResult.body = Json.decodeFromString<DefaultResponse>(body!!)
    }

    return responseResult
}