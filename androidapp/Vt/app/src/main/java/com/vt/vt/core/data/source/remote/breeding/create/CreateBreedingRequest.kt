package com.vt.vt.core.data.source.remote.breeding.create

import com.google.gson.annotations.SerializedName

data class CreateBreedingRequest(

    @field:SerializedName("sled_id")
    val sledId: Int? = null,

    @field:SerializedName("birth_date")
    val birthDate: String? = null,

    @field:SerializedName("livestock_female_id")
    val livestockFemaleId: String? = null,

    @field:SerializedName("livestock_male_id")
    val livestockMaleId: String? = null,

    @field:SerializedName("block_area_id")
    val blockAreaId: Int? = null
)
