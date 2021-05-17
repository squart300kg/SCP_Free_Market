package com.scp.market.ui.search

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.scp.market.databinding.FragmentSearchBinding
import com.scp.market.model.product.request.ProductListRequest
import com.scp.market.model.product.response.Product
import com.scp.market.state.NetworkState
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding

    private var searchAdapter: SearchAdapter? = null

    private var productList: List<Product>? = null

    private val searchViewModel: SearchViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getProductList()

        configureObservables()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
//        binding.rvSearch.adapter = SearchAdapter(fragmentManager!!)
//        binding.rvSearch.layoutManager = LinearLayoutManager(context)
    }

    private fun initRecyclerView() {

        searchAdapter = SearchAdapter(requireActivity(), activity?.supportFragmentManager!!)

        binding.rvSearch.apply {
            setHasFixedSize(true)
            setItemViewCacheSize(10)
            layoutManager = LinearLayoutManager(context)
            adapter = searchAdapter
//            searchAdapter = SearchAdapter(fragmentManager!!)
        }
    }

    private fun configureObservables() {
        searchViewModel.productListInfoNetworkState.observe(this, Observer { networkState ->
            when(networkState) {
                NetworkState.RUNNING -> {}

                NetworkState.SUCCESS -> {
                    Log.i("productList결과값 - 성공 : ", searchViewModel.productList.value.toString())
                    addProductList(searchViewModel.productList.value)
                }

                NetworkState.FAILED -> {
                    Log.i("productList결과값 - 실패 : ", "ㅋㅎㅋㅎ")
                }
            }
        })
    }

    private fun addProductList(productList: List<Product>?) {

        Log.i("addProductList", productList.toString())
        searchAdapter?.addAll(productList)

    }

    private fun getProductList() {

        if (productList == null) {
            searchViewModel.getProductInfoList(
                ProductListRequest(null, null)
            )
        }

    }

}