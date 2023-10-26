package com.waff1e.waffle.waffle.dto

import android.util.Log
import com.waff1e.waffle.dto.DefaultResponse
import com.waff1e.waffle.dto.ResponseResult
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import retrofit2.Response

@Serializable
data class WaffleListResponse(
    var list: List<WaffleResponse> = listOf(),
    var errorCode: Int? = null,
)

fun Response<WaffleListResponse>.check(): WaffleListResponse {
    return if (this.isSuccessful) {
        body()!!
    } else {
        Log.d("로그", "Response<T>.check() - 응답 실패")
        Json.decodeFromString(errorBody()?.string()!!)
    }
}