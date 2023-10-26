package com.waff1e.waffle.waffle.network

import com.waff1e.waffle.waffle.dto.WaffleListSuccessResponse
import retrofit2.Response
import retrofit2.http.GET

interface WaffleService {
    // TODO. GET 메소드에서 데이터 전송 방식 설정 필요
    @GET("waffles")
    suspend fun getWaffleList(): Response<WaffleListSuccessResponse>
}