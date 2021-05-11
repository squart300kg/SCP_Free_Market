package com.scp.market.ui.main

import android.annotation.SuppressLint
import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.scp.market.Application
import com.scp.market.R
import com.scp.market.databinding.FragmentMainBinding
import com.scp.market.ui.MainActivity
import com.scp.market.ui.product.ProductFragment
import com.scp.market.util.dpToPx

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

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
        Application.instance?.user = "hello"
    }

    private fun setPriceCancelLine() {
        binding.txtItemCancel01.paintFlags = binding.txtItemCancel01.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        binding.txtItemCancel02.paintFlags = binding.txtItemCancel02.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        binding.txtItemCancel03.paintFlags = binding.txtItemCancel03.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        val list = listOf(binding.bestItem01, binding.bestItem02, binding.bestItem03)
        for (item in list) {
            item.setOnClickListener {
                (activity as MainActivity).navigationBar.selectedItemId = R.id.menu03
                fragmentManager!!.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                fragmentManager!!.beginTransaction().replace(R.id.container, ProductFragment()).commitAllowingStateLoss()
            }
        }

        val requestOptions = RequestOptions().transforms(RoundedCorners(6.dpToPx()), CenterCrop())

        Glide.with(this).applyDefaultRequestOptions(requestOptions).load(R.drawable.image_apple).into(binding.ivItem01)
        Glide.with(this).applyDefaultRequestOptions(requestOptions).load(R.drawable.image_orange).into(binding.ivItem02)
        Glide.with(this).applyDefaultRequestOptions(requestOptions).load(R.drawable.image_pizza).into(binding.ivItem03)
    }
}