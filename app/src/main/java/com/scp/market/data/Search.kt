package com.scp.market.data

import androidx.annotation.DrawableRes

data class Search(
    val title: String,
    val price: String,
    val cancelPrice: String,
    @DrawableRes val image: Int
)
