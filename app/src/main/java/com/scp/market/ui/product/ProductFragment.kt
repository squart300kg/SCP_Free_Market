package com.scp.market.ui.product

import android.annotation.SuppressLint
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.OnTouchListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.scp.market.BuildConfig
import com.scp.market.R
import com.scp.market.databinding.FragmentProductBinding
import com.scp.market.ui.search.SearchFragment
import com.scp.market.util.dpToPx
import java.text.DecimalFormat
import kotlin.math.roundToLong


class ProductFragment : Fragment() {

    private lateinit var binding: FragmentProductBinding

    private var name: String? = null
    private var price: Int? = null
    private var price_org: Int? = null
    private var image_url: String? = null
    private var content: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        name = arguments?.getString("name")
        price = arguments?.getInt("price")
        price_org = arguments?.getInt("price_org")
        image_url = arguments?.getString("image_url")
        content = arguments?.getString("content")

    }
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

        binding.txtTitle.text = name
        binding.txtPrice.text = "${DecimalFormat("###,###").format(price)}원"


        if (price_org == 0) {
            binding.txtCancelPrice.visibility = View.INVISIBLE
            binding.txtSale.visibility = View.INVISIBLE
        } else {
            binding.txtSale.text = "${calculatePercentage(price?.toLong(), price_org?.toLong())} (${DecimalFormat("###,###").format(price_org!! - price!!)}) 즉시할인가"
            binding.txtCancelPrice.text = "${DecimalFormat("###,###").format(price_org)}원"
        }

        if (content == null) {
            binding.txtSale.visibility = View.INVISIBLE
        } else if (content!!.contains("http")) {
            // 글라이드를 통해서 이미지를 파싱해준다.
            binding.contentImage.visibility = View.VISIBLE
            binding.contentText.visibility = View.GONE
            var contentURL = extracContentURL(content!!)
            Glide.with(this)
                    .load(contentURL)
                    .override(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
                    .into(binding.contentImage)
        } else {
            // 텍스트뷰를 통해서 설명을 보여준다.
            binding.contentImage.visibility = View.GONE
            binding.contentText.visibility = View.VISIBLE
            binding.contentText.text = content
        }

        // TODO 검색 기능
//        binding.edtSearch.setOnTouchListener(OnTouchListener { _, event ->
//            val right = 2
//            if (event.action == MotionEvent.ACTION_UP) {
//                if (event.rawX >= binding.edtSearch.right - binding.edtSearch.compoundDrawables[right].bounds.width()
//                ) {
//                    fragmentManager!!.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
//                    fragmentManager!!.beginTransaction().replace(R.id.container , SearchFragment()).addToBackStack(null).commitAllowingStateLoss()
//                    return@OnTouchListener true
//                }
//            }
//            false
//        })

        // TODO fragmentManager Deprecated대응!!

        val requestOptions = RequestOptions().transforms(RoundedCorners(6.dpToPx()), CenterCrop())
        Glide.with(this)
                .load(image_url)
                .into(binding.ivImage01)

        Log.i("contentURL  : ", content.toString())
        binding.txtCancelPrice.paintFlags = binding.txtCancelPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }

    private fun calculatePercentage(price: Long?, originPrice: Long?): String {
        return if (price != null && originPrice != null) {
            val percent = if(price == 0L && originPrice == 0L) {
                0L
            } else {
                ((originPrice.toFloat() - price.toFloat()) / originPrice.toFloat() * 100).roundToLong()
            }

            "$percent%"
        } else {
            ""
        }
    }

    private fun extracContentURL(url: String): String {

        var idx: Int = url.indexOf("src=\"")
        var result = url.substring(idx+5)

        var idx2: Int = result.indexOf("\">")
        var result2 = result.substring(0, idx2)
        Log.i("resultURL", result2)
        return result2
    }

}