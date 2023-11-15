package com.waff1e.waffle.waffle.data

import com.waff1e.waffle.dto.DefaultResponse
import com.waff1e.waffle.waffle.dto.PostWaffleRequest
import com.waff1e.waffle.waffle.dto.WaffleListSuccessResponse
import com.waff1e.waffle.waffle.dto.Waffle
import com.waff1e.waffle.waffle.network.WaffleService
import retrofit2.Response
import javax.inject.Inject

interface WaffleRepository {
    suspend fun requestWaffleList(limit: Int, isUpdate: Boolean, idx: Long?): Response<WaffleListSuccessResponse>

    suspend fun requestWaffleListByMemberId(memberId: Long?, limit: Int, isUpdate: Boolean, idx: Long?): Response<WaffleListSuccessResponse>

    suspend fun requestWaffle(id: Long): Response<Waffle>

    suspend fun postWaffle(postWaffleRequest: PostWaffleRequest): Response<DefaultResponse>

    suspend fun updateWaffle(id: Long, updateWaffleRequest: PostWaffleRequest): Response<DefaultResponse>

    suspend fun deleteWaffle(id: Long): Response<DefaultResponse>

    suspend fun likeWaffle(id: Long): Response<Waffle>

    suspend fun unlikeWaffle(id: Long): Response<Waffle>
}

class DefaultWaffleRepository @Inject constructor(
    private val waffleService: WaffleService
) : WaffleRepository {
    override suspend fun requestWaffleList(limit: Int, isUpdate: Boolean, idx: Long?): Response<WaffleListSuccessResponse> {
        return waffleService.getWaffleList(limit, if (isUpdate) null else idx)
    }

    override suspend fun requestWaffleListByMemberId(memberId: Long?, limit: Int, isUpdate: Boolean, idx: Long?, ): Response<WaffleListSuccessResponse> {
        return waffleService.getWaffleListByMemberId(memberId, limit, if (isUpdate) null else idx)
    }

    override suspend fun requestWaffle(id: Long): Response<Waffle> {
        return waffleService.getWaffle(id)
    }

    override suspend fun postWaffle(postWaffleRequest: PostWaffleRequest): Response<DefaultResponse> {
        return waffleService.postWaffle(postWaffleRequest)
    }

    override suspend fun updateWaffle(id: Long, updateWaffleRequest: PostWaffleRequest): Response<DefaultResponse> {
        return waffleService.updateWaffle(id, updateWaffleRequest)
    }

    override suspend fun deleteWaffle(id: Long): Response<DefaultResponse> {
        return waffleService.deleteWaffle(id)
    }

    override suspend fun likeWaffle(id: Long): Response<Waffle> {
        return waffleService.likeWaffle(id)
    }

    override suspend fun unlikeWaffle(id: Long): Response<Waffle> {
        return waffleService.unlikeWaffle(id)
    }
}