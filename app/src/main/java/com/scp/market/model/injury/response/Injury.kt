package com.scp.market.model.injury.response

data class Injury(
        val idx: Int?,
        val np_review_id: Int?,
        val type: String?,
        val prod_no: String?,
        val nick: Int?,
        val subject: String?,
        val body: String?,
        val image: List<Any>?,
        val is_secret: Boolean?,
        val is_hide: Boolean?,
        val read_cnt: Int?,
        val status: Int?,
        val writer_ip:Int?,
        val wtime: Int?
)
