package com.scp.market.ui.myroom

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.scp.market.R
import com.scp.market.databinding.FragmentMyroomBinding
import com.scp.market.ui.LocalAdminFragment

class MyRoomFragment : Fragment() {

    private lateinit var binding: FragmentMyroomBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyroomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLocalAdmin.setOnClickListener {
            fragmentManager!!.beginTransaction().replace(R.id.container, LocalAdminFragment()).addToBackStack(null).commitAllowingStateLoss()
        }

        val requestOptions = RequestOptions().transforms(CircleCrop())

        Glide.with(this).applyDefaultRequestOptions(requestOptions).load(R.drawable.image_profile).into(binding.ivProfile)
    }

}