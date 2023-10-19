package com.waff1e.waffle.data

import com.waff1e.waffle.network.WaffleService
import javax.inject.Inject

interface WaffleRepository {

}

class DefaultWaffleRepository(
    @Inject private val waffleService: WaffleService
) : WaffleRepository {

}