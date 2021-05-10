package com.scp.market.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.scp.market.R
import com.scp.market.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnRegister.setOnClickListener {
            (activity as MainActivity).navigationBar.selectedItemId = R.id.menu03
        }

        val items = listOf("대분류","자율시장", "지역특산물", "제례용품", "입점상품", "중고/직거래", "부동산/용역")
        val spinnerAdapter = ArrayAdapter(context!!, R.layout.spinner_item, items)
        binding.spinner.adapter = spinnerAdapter
        binding.spinner.setOnTouchListener { _, _ ->
            val view = activity?.currentFocus
            if (view != null) {
                val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
            activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
            false
        }
//        binding.spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
//                when(position) {
//                    0 -> {
//                    }
//                    6 -> {
//
//                    }
//                    else -> deliveryRequest = items[position]
//                }
//            }
//            override fun onNothingSelected(p0: AdapterView<*>?) { }
//        }
    }

}