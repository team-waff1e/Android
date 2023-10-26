package com.waff1e.waffle.waffle.network

import com.waff1e.waffle.dto.DefaultResponse
import com.waff1e.waffle.waffle.dto.WaffleListRequest
import com.waff1e.waffle.waffle.dto.WaffleListResponse
import com.waff1e.waffle.waffle.dto.WaffleListSuccessResponse
import com.waff1e.waffle.waffle.dto.WaffleResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.QueryMap
import retrofit2.http.QueryName

interface WaffleService {
    @GET("waffles")
    suspend fun getWaffleList(): Response<WaffleListSuccessResponse>

    // TODO. GET 메소드에서 데이터 전송 방식 설정 필요
//    @GET("waffles")
//    suspend fun getWaffleList(waffleListRequest: WaffleListRequest): Response<List<WaffleResponse>>
}