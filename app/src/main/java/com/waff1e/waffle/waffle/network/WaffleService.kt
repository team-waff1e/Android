package com.waff1e.waffle.waffle.network

import com.waff1e.waffle.waffle.dto.WaffleListSuccessResponse
import com.waff1e.waffle.waffle.dto.WaffleResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WaffleService {
    // TODO. GET 메소드에서 데이터 전송 방식 설정 필요
    @GET("waffles")
    suspend fun getWaffleList(
        @Query("limit") limit: Int,
        @Query("isUpdate") isUpdate: Boolean,
        @Query("idx") idx: Int?
    ): Response<WaffleListSuccessResponse>

    @GET("waffles/{id}")
    suspend fun getWaffle(@Path("id") id: Long): Response<WaffleResponse>
}