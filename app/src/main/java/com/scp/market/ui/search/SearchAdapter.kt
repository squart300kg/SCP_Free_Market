package com.scp.market.ui.search

import android.annotation.SuppressLint
import android.graphics.Paint
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


class SearchAdapter(val fm: FragmentManager): RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

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
        Log.i("어댑터 : ", list.toString())
        if (list != null) {
            Log.i("어댑터 0.5 : ", list.toString() + "사이즈 : ")
            if (data.size > 0) {

                Log.i("어댑터1 : ", list.toString())
                if (data[0].name != list[0].name) {

                    Log.i("어댑터2 : ", list.toString())
                    data = ArrayList()
                    data.addAll(list)
                    notifyDataSetChanged()
                }
            } else {

                Log.i("어댑터3 : ", list.toString())
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
            txtCancelPrice.text = "${DecimalFormat("###,###").format(item.price)}원"
            txtCancelPrice.paintFlags = txtCancelPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            val requestOptions = RequestOptions().transforms(RoundedCorners(6.dpToPx()), CenterCrop())

            // 썸네일 생성 규칙
            // 도메인/upload/{data.list.image_url}
            Glide.with(itemView)
                    .applyDefaultRequestOptions(requestOptions)
                    .load("${ BuildConfig.DOMAIN }/upload/${item.imageUrl?.get("${item.images?.get(0)}")}").into(ivImage)

            itemView.setOnClickListener {
                fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                fm.beginTransaction().replace(R.id.container , ProductFragment()).addToBackStack(null).commitAllowingStateLoss()
            }
        }
    }
}