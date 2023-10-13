package com.vt.vt.core.data.source.remote.breeding

import com.google.gson.annotations.SerializedName

data class BreedingResponse(

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("breeding_id")
    val breedingId: Int,

    @field:SerializedName("farm_profile")
    val farmProfile: Int,

    @field:SerializedName("BreedingResponse")
    val breedingResponse: List<BreedingResponseItem>? = null

)

data class BreedingResponseItem(


    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("livestock_male_name")
    val livestockMaleName: String? = null,

    @field:SerializedName("is_active")
    val isActive: Boolean? = null,

    @field:SerializedName("sled_id")
    val sledId: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("livestock_female_id")
    val livestockFemaleId: Int? = null,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("livestock_female_name")
    val livestockFemaleName: String? = null,

    @field:SerializedName("livestock_male_id")
    val livestockMaleId: Int? = null,

    @field:SerializedName("farm_profile_id")
    val farmProfileId: Int
)
