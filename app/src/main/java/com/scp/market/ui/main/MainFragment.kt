package com.scp.market.ui.main

import android.annotation.SuppressLint
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.scp.market.Application
import com.scp.market.BuildConfig
import com.scp.market.R
import com.scp.market.databinding.FragmentMainBinding
import com.scp.market.model.product.request.ProductListRequest
import com.scp.market.model.product.response.Product
import com.scp.market.state.NetworkState
import com.scp.market.ui.MainActivity
import com.scp.market.ui.product.ProductFragment
import com.scp.market.ui.search.SearchViewModel
import com.scp.market.util.dpToPx
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.DecimalFormat

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val searchViewModel: SearchViewModel by viewModel()
    private var prodList: List<Product>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getProductList()

        configureObservables()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setPriceCancelLine()

        binding.edtSearch.setOnTouchListener(View.OnTouchListener { _, event ->
            val right = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= binding.edtSearch.right - binding.edtSearch.compoundDrawables[right].bounds.width()
                ) {
                    (activity as MainActivity).navigationBar.selectedItemId = R.id.menu03
                    return@OnTouchListener true
                }
            }
            false
        })
    }

    private fun setPriceCancelLine() {
        binding.txtItemCancel01.paintFlags = binding.txtItemCancel01.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        binding.txtItemCancel02.paintFlags = binding.txtItemCancel02.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        binding.txtItemCancel03.paintFlags = binding.txtItemCancel03.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        val list = listOf(binding.bestItem01, binding.bestItem02, binding.bestItem03)
        for (item in list) {
            var index = 0
            item.setOnClickListener {

                index = if (item == binding.bestItem01) 0 else if (item == binding.bestItem02) 1 else 2
                (activity as MainActivity).navigationBar.selectedItemId = R.id.menu03
                (activity as MainActivity).supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                (activity as MainActivity).supportFragmentManager.beginTransaction().replace(
                        R.id.container,
                        ProductFragment().apply {
                            arguments = Bundle().apply {

                                var name = prodList?.get(index)?.name
                                var price = if (prodList?.get(index)?.price == null) 0 else prodList?.get(index)?.price
                                var price_org = if (prodList?.get(index)?.price_org == null) 0 else prodList?.get(index)?.price_org
                                var image_url = "${ BuildConfig.DOMAIN }/upload/${prodList?.get(index)?.imageUrl?.get("${prodList!!.get(index).images?.get(0)}")}"
                                var content = prodList?.get(index)?.content

                                putString("name", name)
                                putInt("price", price!!)
                                putInt("price_org", price_org!!)
                                putString("image_url", image_url)
                                putString("content", content)
                                Log.i("MainFragmentLog", "name : $name\nprice : $price")

                            }
                        }).commitAllowingStateLoss()
            }
            index++
        }

//        val requestOptions = RequestOptions().transforms(RoundedCorners(6.dpToPx()), CenterCrop())
//
//        Glide.with(this).applyDefaultRequestOptions(requestOptions).load(R.drawable.image_apple).into(binding.ivItem01)
//        Glide.with(this).applyDefaultRequestOptions(requestOptions).load(R.drawable.image_orange).into(binding.ivItem02)
//        Glide.with(this).applyDefaultRequestOptions(requestOptions).load(R.drawable.image_pizza).into(binding.ivItem03)
    }

    private fun configureObservables() {
        searchViewModel.productListInfoNetworkState.observe(this, Observer { networkState ->
            when(networkState) {
                NetworkState.RUNNING -> {}

                NetworkState.SUCCESS -> {
                    Log.i("productList결과값 - 성공 : ", searchViewModel.productList.value.toString())

                    initMainProduct(searchViewModel.productList.value)


                }

                NetworkState.FAILED -> {
                    Log.i("productList결과값 - 실패 : ", "ㅋㅎㅋㅎ")
                }
            }
        })
    }

    private fun initMainProduct(productList: List<Product>?) {

        prodList = productList

        val requestOptions = RequestOptions().transforms(RoundedCorners(6.dpToPx()), CenterCrop())
        Glide.with(this).applyDefaultRequestOptions(requestOptions).load("${ BuildConfig.DOMAIN }/upload/${productList?.get(0)?.imageUrl?.get("${productList?.get(0).images?.get(0)}")}").into(binding.ivItem01)
        Glide.with(this).applyDefaultRequestOptions(requestOptions).load("${ BuildConfig.DOMAIN }/upload/${productList?.get(1)?.imageUrl?.get("${productList?.get(1).images?.get(0)}")}").into(binding.ivItem02)
        Glide.with(this).applyDefaultRequestOptions(requestOptions).load("${ BuildConfig.DOMAIN }/upload/${productList?.get(2)?.imageUrl?.get("${productList?.get(2).images?.get(0)}")}").into(binding.ivItem03)

        binding.txtItem01.text = productList?.get(0)?.name
        binding.txtItem02.text = productList?.get(1)?.name
        binding.txtItem03.text = productList?.get(2)?.name

        "${DecimalFormat("###,###").format(productList?.get(0)?.price)}원"
        binding.txtItemPrice01.text = "${DecimalFormat("###,###").format(productList?.get(0)?.price)}원"
        binding.txtItemPrice02.text = "${DecimalFormat("###,###").format(productList?.get(1)?.price)}원"
        binding.txtItemPrice03.text = "${DecimalFormat("###,###").format(productList?.get(2)?.price)}원"

        if (productList?.get(0)?.price_org == null) {
            binding.txtItemCancel01.visibility = View.INVISIBLE
        } else {
            "${DecimalFormat("###,###").format(productList?.get(0)?.price)}원"
            binding.txtItemCancel01.text = "${DecimalFormat("###,###").format(productList?.get(0)?.price_org)}원"
        }

        if (productList?.get(1)?.price_org == null) {
            binding.txtItemCancel02.visibility = View.INVISIBLE
        } else {
            binding.txtItemCancel02.text = "${DecimalFormat("###,###").format(productList?.get(1)?.price_org)}원"
        }

        if (productList?.get(2)?.price_org == null) {
            binding.txtItemCancel03.visibility = View.INVISIBLE
        } else {
            binding.txtItemCancel03.text = "${DecimalFormat("###,###").format(productList?.get(2)?.price_org)}원"
        }

    }

    private fun getProductList() {

        searchViewModel.getProductInfoList(
                ProductListRequest(null, null)
        )

    }
}