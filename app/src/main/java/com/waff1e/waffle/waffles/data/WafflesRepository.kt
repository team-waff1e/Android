package com.waff1e.waffle.waffles.data

import com.waff1e.waffle.waffles.dto.WaffleListRequest
import com.waff1e.waffle.waffles.dto.WaffleResponse
import com.waff1e.waffle.waffles.network.WafflesService
import retrofit2.Response
import javax.inject.Inject

interface WafflesRepository {
    suspend fun requestWaffleList(waffleListRequest: WaffleListRequest): Response<List<WaffleResponse>>
}

class DefaultWafflesRepository @Inject constructor(
    private val wafflesService: WafflesService
) : WafflesRepository {
    override suspend fun requestWaffleList(waffleListRequest: WaffleListRequest): Response<List<WaffleResponse>> {
        return wafflesService.getWaffleList(waffleListRequest)
    }
}