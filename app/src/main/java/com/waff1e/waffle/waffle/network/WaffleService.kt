package com.waff1e.waffle.waffle.network

import com.waff1e.waffle.dto.DefaultResponse
import com.waff1e.waffle.waffle.dto.PostWaffleRequest
import com.waff1e.waffle.waffle.dto.WaffleListSuccessResponse
import com.waff1e.waffle.waffle.dto.Waffle
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface WaffleService {
    @GET("waffles")
    suspend fun getWaffleList(
        @Query("limit") limit: Int,
        @Query("idx") idx: Long?
    ): Response<WaffleListSuccessResponse>

    @GET("waffles/memberId")
    suspend fun getWaffleListByMemberId(
        @Query("memberId") memberId: Long?,
        @Query("limit") limit: Int,
        @Query("idx") idx: Long?
    ): Response<WaffleListSuccessResponse>

    @GET("waffles/{waffleId}")
    suspend fun getWaffle(@Path("waffleId") id: Long): Response<Waffle>

    @POST("waffles")
    suspend fun postWaffle(@Body postWaffleRequest: PostWaffleRequest): Response<DefaultResponse>

    @PATCH("waffles/{waffleId}")
    suspend fun updateWaffle(
        @Path("waffleId") id: Long,
        @Body updateWaffleRequest: PostWaffleRequest
    ): Response<DefaultResponse>

    @DELETE("waffles/{waffleId}")
    suspend fun deleteWaffle(
        @Path("waffleId") id: Long
    ): Response<DefaultResponse>

    @POST("waffles/{waffleId}/like")
    suspend fun likeWaffle(@Path("waffleId") id: Long): Response<Waffle>

    @POST("waffles/{waffleId}/unlike")
    suspend fun unlikeWaffle(@Path("waffleId") id: Long): Response<Waffle>

}