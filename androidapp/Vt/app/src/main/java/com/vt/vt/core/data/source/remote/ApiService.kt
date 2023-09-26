package com.vt.vt.core.data.source.remote

import com.vt.vt.core.data.source.remote.auth.model.login.LoginRequest
import com.vt.vt.core.data.source.remote.auth.model.login.LoginResponse
import com.vt.vt.core.data.source.remote.auth.model.register.request.RegisterRequest
import com.vt.vt.core.data.source.remote.auth.model.register.response.RegisterResponse
import com.vt.vt.core.data.source.remote.bcs_record.model.BcsRecordResponseItem
import com.vt.vt.core.data.source.remote.block_areas.model.BlockAndAreaRequest
import com.vt.vt.core.data.source.remote.block_areas.model.BlockAndAreasResponse
import com.vt.vt.core.data.source.remote.block_areas.model.BlockAndAreasResponseItem
import com.vt.vt.core.data.source.remote.categories.model.CategoriesResponseItem
import com.vt.vt.core.data.source.remote.livestock.model.LivestockByIdResponse
import com.vt.vt.core.data.source.remote.livestock.model.LivestockRequest
import com.vt.vt.core.data.source.remote.livestock.model.LivestockResponse
import com.vt.vt.core.data.source.remote.livestock.model.LivestockResponseItem
import com.vt.vt.core.data.source.remote.products.model.ProductRequest
import com.vt.vt.core.data.source.remote.products.model.ProductResponse
import com.vt.vt.core.data.source.remote.products.model.ProductResponseItem
import com.vt.vt.core.data.source.remote.profile.model.UserResponse
import com.vt.vt.core.data.source.remote.sleds.model.SledRequest
import com.vt.vt.core.data.source.remote.sleds.model.SledsResponse
import com.vt.vt.core.data.source.remote.sleds.model.SledsResponseItem
import com.vt.vt.core.data.source.remote.weight_record.model.WeightRecordResponseItem
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

    //    Livestock
    @GET("api/livestocks")
    suspend fun getLivestocks(): Response<List<LivestockResponseItem>>

    @GET("api/livestock/{id}")
    suspend fun getLivestockById(
        @Path("id") id: String
    ): Response<LivestockByIdResponse>

    @POST("api/livestock")
    suspend fun createLivestock(@Body livestockRequest: LivestockRequest): Response<LivestockResponse>

    @PUT("api/livestock/{id}")
    suspend fun updateLivestockById(
        @Path("id") id: String,
        @Body livestockRequest: LivestockRequest
    ): Response<LivestockResponse>

    @DELETE("api/livestock/{id}")
    suspend fun deleteLivestockById(
        @Path("id") id: String
    ): Response<LivestockResponse>

    // Get Categories
    @GET("api/categories")
    suspend fun getCategories(): Response<List<CategoriesResponseItem>>

    //    Products
    @GET("api/products")
    suspend fun getProducts(): Response<List<ProductResponseItem>>

    @POST("api/product")
    suspend fun createProduct(@Body productRequest: ProductRequest): Response<ProductResponse>

    @DELETE("api/product/{id}")
    suspend fun deleteProductById(
        @Path("id") id: String
    ): Response<ProductResponse>

    @GET("api/product/{id}")
    suspend fun getProductById(
        @Path("id") id: String
    ): Response<ProductResponseItem>

    @PUT("api/product/{id}")
    suspend fun updateProductById(
        @Path("id") id: String,
        @Body productRequest: ProductRequest
    ): Response<ProductResponse>

    //    BCS RECORD
    @GET("api/bcs-records")
    suspend fun getBcsRecords(): Response<List<BcsRecordResponseItem>>

    // Weight Record
    @GET("api/weight-records")
    suspend fun getWeightRecords(): Response<List<WeightRecordResponseItem>>
}