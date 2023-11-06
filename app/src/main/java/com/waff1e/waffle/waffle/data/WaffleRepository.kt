package com.waff1e.waffle.waffle.data

import com.waff1e.waffle.dto.DefaultResponse
import com.waff1e.waffle.waffle.dto.PostWaffleRequest
import com.waff1e.waffle.waffle.dto.WaffleListSuccessResponse
import com.waff1e.waffle.waffle.dto.Waffle
import com.waff1e.waffle.waffle.dto.WaffleResponse
import com.waff1e.waffle.waffle.network.WaffleService
import retrofit2.Response
import javax.inject.Inject

interface WaffleRepository {
    // TODO. GET 메소드에서 데이터 전송 방식 설정 필요
    suspend fun requestWaffleList(limit: Int, isUpdate: Boolean, idx: Long?): Response<WaffleListSuccessResponse>

    suspend fun requestWaffle(id: Long): Response<WaffleResponse>

    suspend fun postWaffle(postWaffleRequest: PostWaffleRequest): Response<DefaultResponse>
}

class DefaultWaffleRepository @Inject constructor(
    private val waffleService: WaffleService
) : WaffleRepository {
    override suspend fun requestWaffleList(limit: Int, isUpdate: Boolean, idx: Long?): Response<WaffleListSuccessResponse> {
        return waffleService.getWaffleList(limit, isUpdate, idx)
    }

    override suspend fun requestWaffle(id: Long): Response<WaffleResponse> {
        return waffleService.getWaffle(id)
    }

    override suspend fun postWaffle(postWaffleRequest: PostWaffleRequest): Response<DefaultResponse> {
        return waffleService.postWaffle(postWaffleRequest)
    }
}