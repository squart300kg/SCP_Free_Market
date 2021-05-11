package com.scp.market.ui.charge

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.scp.market.R
import com.scp.market.databinding.FragmentChargeBinding

class ChargeFragment : Fragment() {

    private lateinit var binding: FragmentChargeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChargeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var isCharge = true
        binding.btnCharge.isSelected = true

        binding.btnCharge.setOnClickListener {
            if (!isCharge) {
                binding.chargeContainer.visibility = View.VISIBLE
                binding.calculateContainer.visibility = View.GONE
                binding.btnCharge.isSelected = true
                binding.btnCalculate.isSelected = false
                isCharge = !isCharge
            }
        }

        binding.edtCharge.addTextChangedListener {
            if (it.toString().isEmpty()) {
                binding.edtCharge.setText("0")
            }
        }

        binding.btnCalculate.setOnClickListener {
            if (isCharge) {
                binding.calculateContainer.visibility = View.VISIBLE
                binding.chargeContainer.visibility = View.GONE
                binding.btnCalculate.isSelected = true
                binding.btnCharge.isSelected = false
                isCharge = !isCharge
            }
        }

        val requestOptions = RequestOptions().transforms(CircleCrop())

        Glide.with(this).applyDefaultRequestOptions(requestOptions).load(R.drawable.image_profile).into(binding.ivProfile)
    }

}