package com.scp.market.ui.search

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.scp.market.BuildConfig
import com.scp.market.R
import com.scp.market.model.product.response.Product
import com.scp.market.ui.product.ProductFragment
import com.scp.market.util.dpToPx
import java.text.DecimalFormat


class SearchAdapter(
        val activity: Activity,
        val fm: FragmentManager
        ): RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    private var data: ArrayList<Product> = ArrayList()

    init {
//        data.add(Search("귤로장생 GAP 인증 당도선별 카라향...", "117,000원", "137,000원", R.drawable.image_apple))
//        data.add(Search("귤로장생 GAP 인증 당도선별 카라향...", "117,000원", "137,000원", R.drawable.image_orange))
//        data.add(Search("귤로장생 GAP 인증 당도선별 카라향...", "117,000원", "137,000원", R.drawable.image_pizza))
//        data.add(Search("귤로장생 GAP 인증 당도선별 카라향...", "117,000원", "137,000원", R.drawable.image_apple))
//        data.add(Search("귤로장생 GAP 인증 당도선별 카라향...", "117,000원", "137,000원", R.drawable.image_pizza))
//        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder
            = SearchViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_search,
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) =
        holder.bind(data[position])


    override fun getItemCount(): Int = data.size

    fun addAll(list: List<Product>?) {

        if (list != null) {
            Log.i("addAllLog", "1")
            if (data.size > 0) {

                Log.i("addAllLog", "2")
                if (data[0].name != list[0].name) {
                    Log.i("addAllLog", "3")
                    data = ArrayList()
                    data.addAll(list)
                    notifyDataSetChanged()
                } else {
                    Log.i("addAllLog", "3.5, $list")

                    // 뒤로가기 돌아왔을시, 데이터 바인딩을 위한 코드
                    data.clear()
                    notifyDataSetChanged()
                    data.addAll(list)
                    notifyDataSetChanged()
                }

            } else {
                Log.i("addAllLog", "4")
                data.addAll(list)
                notifyDataSetChanged()

            }
        }
    }

    inner class SearchViewHolder(view: View): RecyclerView.ViewHolder(view) {

        private val txtTitle: TextView = view.findViewById(R.id.txtTitle)
        private val txtPrice: TextView = view.findViewById(R.id.txtPrice)
        private val txtCancelPrice: TextView = view.findViewById(R.id.txtCancelPrice)
        private val ivImage: ImageView = view.findViewById(R.id.ivImage)

        @SuppressLint("SetTextI18n")
        fun bind(item: Product) {
            txtTitle.text = item.name
            txtPrice.text = "${DecimalFormat("###,###").format(item.price)}원"
            if (item.price_org == null) {
                txtCancelPrice.visibility = View.INVISIBLE
            } else {
                Log.i("item.price_org", "${item.price_org}")
                txtCancelPrice.visibility = View.VISIBLE
                txtCancelPrice.text = "${DecimalFormat("###,###").format(item.price_org)}원"
            }
            txtCancelPrice.paintFlags = txtCancelPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            val requestOptions = RequestOptions().transforms(RoundedCorners(6.dpToPx()), CenterCrop())

            // 썸네일 생성 규칙
            // 도메인/upload/{data.list.image_url}
            Glide.with(itemView)
                    .applyDefaultRequestOptions(requestOptions)
                    .load("${ BuildConfig.DOMAIN }/upload/${item.imageUrl?.get("${item.images?.get(0)}")}").into(ivImage)

            var name = item.name
            var price = item.price ?: 0
            var price_org = item.price_org ?: 0
            var content = item.content

            itemView.setOnClickListener {

                fm.popBackStack("product", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                fm.beginTransaction().replace(
                        R.id.container ,
                        ProductFragment().apply {
                            arguments = Bundle().apply {
                                putString("name", name)
                                putInt("price", price)
                                putInt("price_org", price_org)
                                putString("image_url", "${ BuildConfig.DOMAIN }/upload/${item.imageUrl?.get("${item.images?.get(0)}")}")
                                putString("content", content)
                            }
                        })
                        .addToBackStack("product").commitAllowingStateLoss()
            }
        }
    }
}