package com.scp.market.model.product.response


import com.google.gson.annotations.SerializedName

data class Etc(
    val adult: Boolean?,
    @SerializedName("maximum_purchase_quantity")
    val maximumPurchaseQuantity: Int?,
    @SerializedName("member_maximum_purchase_quantity")
    val memberMaximumPurchaseQuantity: Int?,
    @SerializedName("minimum_purchase_quantity")
    val minimumPurchaseQuantity: Int?,
    @SerializedName("optional_limit_type")
    val optionalLimitType: String?,
    @SerializedName("use_unipass_number")
    val useUnipassNumber: String?
)