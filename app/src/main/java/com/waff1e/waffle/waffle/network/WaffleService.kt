package com.waff1e.waffle.waffle.network

import com.waff1e.waffle.dto.DefaultResponse
import com.waff1e.waffle.waffle.dto.PostWaffleRequest
import com.waff1e.waffle.waffle.dto.WaffleListSuccessResponse
import com.waff1e.waffle.waffle.dto.Waffle
import com.waff1e.waffle.waffle.dto.WaffleResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface WaffleService {
    @GET("waffles")
    suspend fun getWaffleList(
        @Query("limit") limit: Int,
        @Query("isUpdate") isUpdate: Boolean,
        @Query("idx") idx: Long?
    ): Response<WaffleListSuccessResponse>

    @GET("waffles/{id}")
    suspend fun getWaffle(@Path("id") id: Long): Response<WaffleResponse>

    @POST("waffles")
    suspend fun postWaffle(@Body postWaffleRequest: PostWaffleRequest): Response<DefaultResponse>
}