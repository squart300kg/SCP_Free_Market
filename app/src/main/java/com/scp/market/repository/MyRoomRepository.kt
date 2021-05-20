package com.scp.market.repository

import com.scp.market.api.MyRoomService
import retrofit2.await
import retrofit2.awaitResponse

class MyRoomRepository(private val myRoomService: MyRoomService) {

    suspend fun getUserInfoList()
    = myRoomService.getUserInfoList().awaitResponse()

    suspend fun getInjuryList()
    =myRoomService.getInjuryList().awaitResponse()
}