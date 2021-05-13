package com.scp.market.model.product.response


import com.google.gson.annotations.SerializedName

data class Sync(
    @SerializedName("event_words")
    val eventWords: String?,
    @SerializedName("import_flag")
    val importFlag: Boolean?,
    @SerializedName("is_culture_benefit")
    val isCultureBenefit: Boolean?,
    @SerializedName("naver_category")
    val naverCategory: String?,
    @SerializedName("order_made")
    val orderMade: Boolean?,
    @SerializedName("parallel_import")
    val parallelImport: Boolean?,
    @SerializedName("pay_product_name")
    val payProductName: String?,
    @SerializedName("product_condition")
    val productCondition: String?,
    @SerializedName("product_flag")
    val productFlag: String?
)