package com.vt.vt.core.data.source.remote.feeding_record.model

import com.google.gson.annotations.SerializedName

data class FeedingRecordRequest(

	@field:SerializedName("consumption_record")
	val consumptionRecord: List<ConsumptionRecordItem?>? = null
)
data class ConsumptionRecordItem(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("score")
	val score: Double? = null,

	@field:SerializedName("feed_category")
	val feedCategory: Int? = null,

	@field:SerializedName("left")
	val left: Int? = null,

	@field:SerializedName("sku_id")
	val skuId: Int? = null,

	@field:SerializedName("block_area_id")
	val blockAreaId: Int? = null,

	@field:SerializedName("remarks")
	val remarks: String? = null
)
