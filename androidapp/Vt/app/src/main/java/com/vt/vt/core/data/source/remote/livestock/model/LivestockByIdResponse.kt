package com.vt.vt.core.data.source.remote.livestock.model

import com.google.gson.annotations.SerializedName
import com.vt.vt.core.data.source.remote.block_areas.model.FeedingRecordsItem

data class LivestockByIdResponse(

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("info")
    val info: String,

    @field:SerializedName("bangsa")
    val bangsa: String,

    @field:SerializedName("gender")
    val gender: Int,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("weight_records")
    val weightRecords: List<WeightRecordsItem>,

    @field:SerializedName("bcs_records")
    val bcsRecords: List<BcsRecordsItem>,

    @field:SerializedName("health_records")
    val healthRecords: List<HealthRecordsItem>,

    @field:SerializedName("feeding_records")
    val feedingRecords: List<FeedingRecordsItem>? = null,
)

data class HealthRecordsItem(

    @field:SerializedName("date")
    val date: String,

    @field:SerializedName("livestock_id")
    val livestockId: Int,

    @field:SerializedName("treatment_methods")
    val treatmentMethods: String? = null, // tipe data null

    @field:SerializedName("disease_type")
    val diseaseType: String? = null, // tipe data null

    @field:SerializedName("created_at")
    val createdAt: String,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("remarks")
    val remarks: String
)

data class WeightRecordsItem(

    @field:SerializedName("date")
    val date: String,

    @field:SerializedName("score")
    val score: Double,

    @field:SerializedName("growth")
    val growth: String,

    @field:SerializedName("prev_score")
    val prevScore: Double,

    @field:SerializedName("livestock_id")
    val livestockId: Int,

    @field:SerializedName("created_at")
    val createdAt: String,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("remarks")
    val remarks: String
)

data class BcsRecordsItem(

    @field:SerializedName("date")
    val date: String,

    @field:SerializedName("livestock_id")
    val livestockId: Int,

    @field:SerializedName("created_at")
    val createdAt: String,

    @field:SerializedName("growth")
    val growth: String,

    @field:SerializedName("prev_score")
    val prevScore: Double,

    @field:SerializedName("score")
    val score: Double,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("remarks")
    val remarks: String
)
