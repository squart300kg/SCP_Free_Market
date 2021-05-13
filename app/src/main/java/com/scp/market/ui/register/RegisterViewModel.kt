package com.scp.market.ui.register

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.scp.market.Application
import com.scp.market.base.BaseViewModel
import com.scp.market.model.ProductInfo
import com.scp.market.repository.ProductRepository
import com.scp.market.state.NetworkState
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class RegisterViewModel(private val ProductRepository: ProductRepository): BaseViewModel() {

    // FOR DATA
    val testNetworkState = MutableLiveData<NetworkState>()

    fun registerProduct(productInfo: ProductInfo) {
        ioScope.launch {
            try {
                val response = ProductRepository.registerProduct(productInfo)

                Log.i("결과값 : ", productInfo.category.toString() + ", accessToken : ${Application.instance?.user}")

            } catch (socketTimeoutException: SocketTimeoutException) {
                Log.e("SupervisorJob", "SocketTimeoutException")
            } catch (ex: Throwable) {
                Log.e("SupervisorJob", "throwable")
            }

        }
    }




}