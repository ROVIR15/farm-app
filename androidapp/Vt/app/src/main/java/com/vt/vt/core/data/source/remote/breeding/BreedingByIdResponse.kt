package com.vt.vt.core.data.source.remote.breeding

import com.google.gson.annotations.SerializedName

data class BreedingByIdResponse(

    @field:SerializedName("message")
    val message: String?,

    @field:SerializedName("status")
    val status: String?,

    @field:SerializedName("livestock_female")
    val livestockFemale: LivestockFemale? = null,

    @field:SerializedName("is_active")
    val isActive: Boolean,

    @field:SerializedName("sled_id")
    val sledId: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("livestock_male")
    val livestockMale: LivestockMale? = null,

    @field:SerializedName("livestock_female_id")
    val livestockFemaleId: Int? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("livestock_male_id")
    val livestockMaleId: Int? = null
)

data class LivestockFemale(

    @field:SerializedName("bangsa")
    val bangsa: String? = null,

    @field:SerializedName("gender")
    val gender: Int? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("id")
    val id: Int? = null
)

data class LivestockMale(

    @field:SerializedName("bangsa")
    val bangsa: String? = null,

    @field:SerializedName("gender")
    val gender: Int? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("id")
    val id: Int? = null
)
