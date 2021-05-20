package com.scp.market.ui.myroom

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.scp.market.base.BaseViewModel
import com.scp.market.model.injury.response.Injury
import com.scp.market.model.user.response.UserInfo
import com.scp.market.repository.MyRoomRepository
import com.scp.market.state.NetworkState
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class MyRoomViewModel(private val myRoomRepository: MyRoomRepository): BaseViewModel() {

    val userInfoNetworkState = MutableLiveData<NetworkState>()
    val injuryNetworkState = MutableLiveData<NetworkState>()

    var userInfoList = MutableLiveData<List<UserInfo>>()
    var injuryList = MutableLiveData<List<Injury>>()

    fun getUserInfoList() {
        userInfoNetworkState.postValue(NetworkState.RUNNING)
        ioScope.launch {
            try {
                val response = myRoomRepository.getUserInfoList()

                if (response.isSuccessful) {

                    userInfoList.postValue(response.body()?.data?.list)
                    Log.i("MyRoomViewModelLogUser_InfoList", userInfoList.toString())
                    userInfoNetworkState.postValue(NetworkState.SUCCESS)
                } else {

                    userInfoNetworkState.postValue(NetworkState.FAILED)
                }

            } catch (socketTimeoutException: SocketTimeoutException) {
                Log.e("SupervisorJob", "SocketTimeoutException")
            } catch (ex: Throwable) {
                Log.e("SupervisorJob", "throwable")
            }
        }
    }

    fun getInjuryList() {
        injuryNetworkState.postValue(NetworkState.RUNNING)
        ioScope.launch {
            try {
                val response = myRoomRepository.getInjuryList()

                if (response.isSuccessful) {

                    injuryList.postValue(response.body()?.data?.list)
                    Log.i("MyRoomViewModelLog_InjuryList", userInfoList.toString())
                    injuryNetworkState.postValue(NetworkState.SUCCESS)
                } else {

                    injuryNetworkState.postValue(NetworkState.FAILED)
                }

            } catch (socketTimeoutException: SocketTimeoutException) {
                Log.e("SupervisorJob", "SocketTimeoutException")
            } catch (ex: Throwable) {
                Log.e("SupervisorJob", "throwable")
            }
        }
    }
}