package com.waff1e.waffle.waffles.network

import com.waff1e.waffle.waffles.dto.WaffleListRequest
import com.waff1e.waffle.waffles.dto.WaffleResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET

interface WafflesService {
    @GET("waffles")
    suspend fun getWaffleList(@Body waffleListRequest: WaffleListRequest): Response<List<WaffleResponse>>
}