package com.scp.market.ui.search

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.scp.market.base.BaseViewModel
import com.scp.market.model.product.request.ProductListRequest
import com.scp.market.model.product.response.Product
import com.scp.market.repository.ProductRepository
import com.scp.market.state.NetworkState
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class SearchViewModel(private val productRepository: ProductRepository): BaseViewModel() {

    val productListInfoNetworkState = MutableLiveData<NetworkState>()

    val productList = MutableLiveData<List<Product>>()

    fun getProductInfoList(productListRequest: ProductListRequest) {

        productListInfoNetworkState.postValue(NetworkState.RUNNING)

        ioScope.launch {
            try {

                val response = productRepository.getProductList(productListRequest)

                if (response.isSuccessful) {
                    if (response.body()?.code == -7) { // TOO MANY REQUEST
                        productListInfoNetworkState.postValue(NetworkState.FAILED)
                        Log.i("searchResponseLog", "-7")
                    } else { // 200
                        productList.postValue(response.body()?.data?.list)
                        productListInfoNetworkState.postValue(NetworkState.SUCCESS)
                        Log.i("searchResponseLog", "200")
                    }
                } else {

                    productListInfoNetworkState.postValue(NetworkState.FAILED)
                }

            } catch (socketTimeoutException: SocketTimeoutException) {
                Log.e("SupervisorJob", "SocketTimeoutException")
            } catch (ex: Throwable) {
                Log.e("SupervisorJob", "throwable")
            }
        }
    }

    // 1. 로그인을 성공
    // 2. 액세스 토큰 발급 완료
    // 3. 액세스 토큰을 시큐어 프리퍼런스에 저장




}