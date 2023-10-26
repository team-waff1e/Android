package com.waff1e.waffle.waffle.dto

import android.util.Log
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import retrofit2.Response

interface WaffleListResponse {
}

@Serializable
data class WaffleListSuccessResponse(
    val list: List<WaffleResponse> = listOf(),
): WaffleListResponse

@Serializable
data class WaffleListFailResponse(
    val errorCode: Int
): WaffleListResponse

//fun Response<WaffleListResponse>.check(): WaffleListResponse {
//    return if (this.isSuccessful) {
//        body()!!
//    } else {
//        Log.d("로그", "Response<T>.check() - 응답 실패")
//        Json.decodeFromString(errorBody()?.string()!!)
//    }
//}