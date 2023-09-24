package com.vt.vt.core.data.source.remote

import com.vt.vt.core.data.source.remote.auth.model.login.LoginRequest
import com.vt.vt.core.data.source.remote.auth.model.login.LoginResponse
import com.vt.vt.core.data.source.remote.auth.model.register.request.RegisterRequest
import com.vt.vt.core.data.source.remote.auth.model.register.response.RegisterResponse
import com.vt.vt.core.data.source.remote.block_areas.model.BlockAndAreaRequest
import com.vt.vt.core.data.source.remote.block_areas.model.BlockAndAreasResponse
import com.vt.vt.core.data.source.remote.block_areas.model.BlockAndAreasResponseItem
import com.vt.vt.core.data.source.remote.profile.model.UserResponse
import com.vt.vt.core.data.source.remote.sleds.model.SledRequest
import com.vt.vt.core.data.source.remote.sleds.model.SledsResponse
import com.vt.vt.core.data.source.remote.sleds.model.SledsResponseItem
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @POST("register")
    suspend fun doRegistration(@Body registerRequest: RegisterRequest): Response<RegisterResponse>

    @POST("login")
    suspend fun doLogin(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @GET("api/profile")
    suspend fun getUser(): Response<UserResponse>

    @POST("api/logout")
    suspend fun doLogout(): Response<LoginResponse>

    //    BLOCK AND AREAS
    @GET("api/block-areas")
    suspend fun getBlockAreas(): Response<List<BlockAndAreasResponseItem>>

    @GET("api/block-area/{id}")
    suspend fun getBlockArea(
        @Path("id") id: String
    ): Response<BlockAndAreasResponseItem>

    @POST("api/block-area")
    suspend fun createBlockArea(@Body blockAndAreaRequest: BlockAndAreaRequest): Response<BlockAndAreasResponse>

    @DELETE("api/block-area/{id}")
    suspend fun deleteBlockArea(
        @Path("id") id: String
    ): Response<BlockAndAreasResponse>

    @PUT("api/block-area/{id}")
    suspend fun updateBlockArea(
        @Path("id") id: String,
        @Body blockAndAreaRequest: BlockAndAreaRequest
    ): Response<BlockAndAreasResponse>

    // SLED
    @GET("api/sleds")
    suspend fun getAllSleds(): Response<List<SledsResponseItem>>

    @DELETE("/api/sled/{id}")
    suspend fun deleteSledById(
        @Path("id") id: String
    ): Response<SledsResponse>

    @GET("api/sled/{id}")
    suspend fun getSledById(
        @Path("id") id: String
    ): Response<SledsResponseItem>

    @POST("api/sled")
    suspend fun createSled(@Body sledRequest: SledRequest): Response<SledsResponse>

    @PUT("api/sled/{id}")
    suspend fun updateSled(
        @Path("id") id: String,
        @Body sledRequest: SledRequest
    ): Response<SledsResponse>
}