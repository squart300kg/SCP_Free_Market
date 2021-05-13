package com.scp.market.ui.login

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.scp.market.base.BaseViewModel
import com.scp.market.model.login.request.AccessTokenRequest
import com.scp.market.repository.LoginRepository
import com.scp.market.state.NetworkState
import com.securepreferences.SecurePreferences
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class LoginViewModel(
        private val loginRepository: LoginRepository,
        private val securePreferences: SecurePreferences): BaseViewModel() {

    val accessTokenNetworkState = MutableLiveData<NetworkState>()

    fun getAccessToken(accessTokenRequest: AccessTokenRequest) {
        accessTokenNetworkState.postValue(NetworkState.RUNNING)
        ioScope.launch {
            try {
                val response = loginRepository.getAccessToekn(
                    AccessTokenRequest(
                        accessTokenRequest.key,
                        accessTokenRequest.secret
                    )
                )

                if (response.isSuccessful) {

                    Log.i("imweb accessToken수신성공", response.body().toString())
                    securePreferences.edit().putUnencryptedString("token", response.body()?.access_token).commit()

                    accessTokenNetworkState.postValue(NetworkState.SUCCESS)
                } else {

                    Log.i("imweb accessToken수신실패", response.code().toString())
                    accessTokenNetworkState.postValue(NetworkState.FAILED)
                }

            } catch (socketTimeoutException: SocketTimeoutException) {
                Log.e("SupervisorJob", "SocketTimeoutException")
            } catch (ex: Throwable) {
                Log.e("SupervisorJob", "throwable")
            }
        }
    }


}