package com.scp.market.ui.register

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.scp.market.Application
import com.scp.market.base.BaseViewModel
import com.scp.market.model.Category
import com.scp.market.model.ProductInfo
import com.scp.market.model.registerProduct.request.RegisterProductRequest
import com.scp.market.repository.ProductRepository
import com.scp.market.state.NetworkState
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class RegisterViewModel(private val ProductRepository: ProductRepository): BaseViewModel() {

    // FOR DATA
    val testNetworkState = MutableLiveData<NetworkState>()
    val categoryNetWorkState = MutableLiveData<NetworkState>()
    val registerNetworkState = MutableLiveData<NetworkState>()

    val categories = MutableLiveData<List<Category>>()

    fun registerProduct(registerProductRequest: RegisterProductRequest) {
        registerNetworkState.postValue(NetworkState.RUNNING)
        ioScope.launch {
            try {
                val response = ProductRepository.registerProduct(registerProductRequest)

                if (response.isSuccessful) {
                    registerNetworkState.postValue(NetworkState.SUCCESS)
                } else {
                    registerNetworkState.postValue(NetworkState.FAILED)
                }


            } catch (socketTimeoutException: SocketTimeoutException) {
                Log.e("SupervisorJob", "SocketTimeoutException")
            } catch (ex: Throwable) {
                Log.e("SupervisorJob", "throwable")
            }

        }
    }

    fun getCategories() {
        categoryNetWorkState.postValue(NetworkState.RUNNING)
        ioScope.launch {
            try {
                val response = ProductRepository.getCategories()

                if (response.isSuccessful) {


                    categories.postValue(response.body()?.data)
                    categoryNetWorkState.postValue(NetworkState.SUCCESS)
                } else {

                    categoryNetWorkState.postValue(NetworkState.FAILED)
                }
//                Log.i("결과값 : ", productInfo.category.toString() + ", accessToken : ${Application.instance?.user}")

            } catch (socketTimeoutException: SocketTimeoutException) {
                Log.e("SupervisorJob", "SocketTimeoutException")
            } catch (ex: Throwable) {
                Log.e("SupervisorJob", "throwable")
            }

        }
    }


}