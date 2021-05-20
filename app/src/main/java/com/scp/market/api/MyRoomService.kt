package com.scp.market.api

import com.scp.market.model.injury.response.UserInjuryListResponse
import com.scp.market.model.user.response.UserInfoListResponse
import retrofit2.Call
import retrofit2.http.GET

interface MyRoomService {

    @GET("member/members")
    fun getUserInfoList(): Call<UserInfoListResponse>

    @GET("shop/inquirys")
    fun getInjuryList(): Call<UserInjuryListResponse>
}