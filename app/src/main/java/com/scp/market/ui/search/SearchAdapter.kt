package com.scp.market.ui.search

import android.annotation.SuppressLint
import android.graphics.Paint
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
import com.scp.market.R
import com.scp.market.data.Search
import com.scp.market.ui.product.ProductFragment
import com.scp.market.util.dpToPx


class SearchAdapter(val fm: FragmentManager): RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    private var data: List<Search> = listOf()

    init {
        data = listOf(
            Search("귤로장생 GAP 인증 당도선별 카라향...", "117,000원", "137,000원", R.drawable.image_apple),
            Search("귤로장생 GAP 인증 당도선별 카라향...", "117,000원", "137,000원", R.drawable.image_orange),
            Search("귤로장생 GAP 인증 당도선별 카라향...", "117,000원", "137,000원", R.drawable.image_pizza),
            Search("귤로장생 GAP 인증 당도선별 카라향...", "117,000원", "137,000원", R.drawable.image_apple),
            Search("귤로장생 GAP 인증 당도선별 카라향...", "117,000원", "137,000원", R.drawable.image_pizza)
        )
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
            = ViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_search,
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        private val txtTitle: TextView = view.findViewById(R.id.txtTitle)
        private val txtPrice: TextView = view.findViewById(R.id.txtPrice)
        private val txtCancelPrice: TextView = view.findViewById(R.id.txtCancelPrice)
        private val ivImage: ImageView = view.findViewById(R.id.ivImage)

        @SuppressLint("SetTextI18n")
        fun bind(item: Search) {
            txtTitle.text = item.title
            txtPrice.text = item.price
            txtCancelPrice.text = item.cancelPrice
            txtCancelPrice.paintFlags = txtCancelPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            val requestOptions = RequestOptions().transforms(RoundedCorners(6.dpToPx()), CenterCrop())

            Glide.with(itemView).applyDefaultRequestOptions(requestOptions).load(item.image).into(ivImage)

            itemView.setOnClickListener {
                fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                fm.beginTransaction().replace(R.id.container , ProductFragment()).addToBackStack(null).commitAllowingStateLoss()
            }
        }
    }
}