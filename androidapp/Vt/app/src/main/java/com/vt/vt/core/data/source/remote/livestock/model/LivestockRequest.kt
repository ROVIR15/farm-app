package com.vt.vt.core.data.source.remote.livestock.model

import com.google.gson.annotations.SerializedName

data class LivestockRequest(

    @field:SerializedName("bangsa")
    val bangsa: String?,

    @field:SerializedName("gender")
    val gender: Int,

    @field:SerializedName("name")
    val name: String?,

    @field:SerializedName("birth_date")
    val birthDate: String?,

    @field:SerializedName("description")
    val description: String?,

    @field:SerializedName("parent_female_id")
    val parentFemaleId: Int?,

    @field:SerializedName("parent_male_id")
    val parentMaleId: Int?,
)
