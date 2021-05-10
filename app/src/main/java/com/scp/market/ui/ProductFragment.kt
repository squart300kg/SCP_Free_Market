package com.scp.market.ui

import android.annotation.SuppressLint
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.scp.market.R
import com.scp.market.databinding.FragmentProductBinding
import com.scp.market.util.dpToPx


class ProductFragment : Fragment() {

    private lateinit var binding: FragmentProductBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.edtSearch.setOnTouchListener(OnTouchListener { _, event ->
            val right = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= binding.edtSearch.right - binding.edtSearch.compoundDrawables[right].bounds.width()
                ) {
                    fragmentManager!!.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    fragmentManager!!.beginTransaction().replace(R.id.container , SearchFragment()).addToBackStack(null).commitAllowingStateLoss()
                    return@OnTouchListener true
                }
            }
            false
        })

        val requestOptions = RequestOptions().transforms(RoundedCorners(6.dpToPx()), CenterCrop())
        Glide.with(this).applyDefaultRequestOptions(requestOptions).load(R.drawable.image_product_01).into(
            binding.ivImage01
        )

        binding.txtCancelPrice.paintFlags = binding.txtCancelPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }

}