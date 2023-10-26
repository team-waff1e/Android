package com.waff1e.waffle.dto

import android.util.Log
import com.waff1e.waffle.waffle.dto.WaffleResponse
import kotlinx.serialization.Serializable
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

    if (this.isSuccessful) {
        Log.d("로그", "Response<T>.check() - 응답 성공")
    } else {
        Log.d("로그", "Response<T>.check() - 응답 실패")
        val body = errorBody()?.string()
        responseResult.body = Json.decodeFromString<DefaultResponse>(body!!)
    }

    return responseResult
}