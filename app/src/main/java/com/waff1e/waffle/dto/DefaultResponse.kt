package com.waff1e.waffle.dto

import android.util.Log
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import retrofit2.Response

@Serializable
data class DefaultResponse(
    val errorCode: Int
)

// Retrofit 응답 결과에 따른 처리 메소드
fun <T> Response<T>.check(): ResponseResult {
    val responseResult = ResponseResult(isSuccess = isSuccessful, statusCode = code())

    if (this.isSuccessful) {
        Log.d("로그", "Response<T>.check() - 응답 성공")
    } else {
        Log.d("로그", "Response<T>.check() - 응답 실패")
        val body = errorBody()?.string()
        responseResult.body = Json.decodeFromString<DefaultResponse>(body!!)
    }

    return responseResult
}