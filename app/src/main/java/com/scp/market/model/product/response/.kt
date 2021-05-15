package com.scp.market.model.product.response


import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("add_time")
    val addTime: Int?,
    val badge: Badge?,
    val brand: String?,
    val categories: List<String>?,
    val content: String?,
    @SerializedName("content_plain")
    val contentPlain: String?,
    @SerializedName("custom_prod_code")
    val customProdCode: String?,
    @SerializedName("digital_prod_data")
    val digitalProdData: Any?,
    @SerializedName("edit_time")
    val editTime: Int?,
    val etc: Etc?,
    @SerializedName("image_url")
    val imageUrl: HashMap<String, String>?,
    val images: List<String>?,
    val maker: String?,
    val name: String?,
    val no: Int?,
    val origin: String?,
    val point: Point?,
    val price: Int?,
    @SerializedName("price_none")
    val priceNone: Boolean?,
    @SerializedName("price_tax")
    val priceTax: Boolean?,
    @SerializedName("prod_status")
    val prodStatus: String?,
    @SerializedName("prod_type")
    val prodType: String?,
    val prodinfo: List<Any>?,
    @SerializedName("product_discount_options")
    val productDiscountOptions: List<String>?,
    val seo: Seo?,
    @SerializedName("simple_content")
    val simpleContent: String?,
    @SerializedName("simple_content_plain")
    val simpleContentPlain: String?,
    val stock: Stock?,
    val sync: Sync?,
    @SerializedName("use_mobile_prod_content")
    val useMobileProdContent: Boolean?,
    @SerializedName("use_pre_sale")
    val usePreSale: Boolean?,
    val weight: String?
)