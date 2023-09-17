package com.vt.vt.core.data.source.remote.auth.model.register.request

import com.google.gson.annotations.SerializedName

data class RegisterRequest(

    @field:SerializedName("username")
    val username: String?,

    @field:SerializedName("email")
    val email: String?,

    @field:SerializedName("password")
    val password: String?,

    @field:SerializedName("farm_profile")
    val farmProfile: FarmProfile?,
)

data class FarmProfile(

    @field:SerializedName("name")
    val name: String?,

    @field:SerializedName("address_one")
    val addressOne: String?,

    @field:SerializedName("address_two")
    val addressTwo: String?,

    @field:SerializedName("city")
    val city: String?,

    @field:SerializedName("province")
    val province: String?

)
