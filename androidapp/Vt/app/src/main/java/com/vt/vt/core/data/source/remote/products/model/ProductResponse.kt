package com.vt.vt.core.data.source.remote.products.model

import com.google.gson.annotations.SerializedName

data class ProductResponse(

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("status")
    val status: String?,

    @field:SerializedName("ProductResponse")
    val productResponse: List<ProductResponseItem?>? = null
)

data class ProductResponseItem(

    @field:SerializedName("category_name")
    val categoryName: String? = null,

    @field:SerializedName("category_id")
    val categoryId: Int,

    @field:SerializedName("unit_measurement")
    val unitMeasurement: String? = null,

    @field:SerializedName("product_id")
    val productId: Int? = null,

    @field:SerializedName("sku_id")
    val skuId: Int? = null,

    @field:SerializedName("product_name")
    val productName: String)
