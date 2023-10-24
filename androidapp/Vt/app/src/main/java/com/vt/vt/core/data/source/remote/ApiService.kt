package com.vt.vt.core.data.source.remote

import com.vt.vt.core.data.source.remote.auth.model.login.*
import com.vt.vt.core.data.source.remote.auth.model.register.request.*
import com.vt.vt.core.data.source.remote.auth.model.register.response.*
import com.vt.vt.core.data.source.remote.bcs_record.model.*
import com.vt.vt.core.data.source.remote.block_areas.model.*
import com.vt.vt.core.data.source.remote.breeding.*
import com.vt.vt.core.data.source.remote.breeding.create.*
import com.vt.vt.core.data.source.remote.breeding.history.create.*
import com.vt.vt.core.data.source.remote.breeding.lambing.create.*
import com.vt.vt.core.data.source.remote.breeding.pregnancy.*
import com.vt.vt.core.data.source.remote.budget.*
import com.vt.vt.core.data.source.remote.categories.model.*
import com.vt.vt.core.data.source.remote.expenditure.AddExpenditureRequest
import com.vt.vt.core.data.source.remote.expenditure.ExpenditureResponse
import com.vt.vt.core.data.source.remote.farm_profile.model.*
import com.vt.vt.core.data.source.remote.feeding_record.model.*
import com.vt.vt.core.data.source.remote.health_record.model.*
import com.vt.vt.core.data.source.remote.livestock.model.*
import com.vt.vt.core.data.source.remote.products.model.*
import com.vt.vt.core.data.source.remote.profile.model.*
import com.vt.vt.core.data.source.remote.sleds.model.*
import com.vt.vt.core.data.source.remote.weight_record.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("register")
    suspend fun doRegistration(@Body registerRequest: RegisterRequest): Response<RegisterResponse>

    @POST("login")
    suspend fun doLogin(@Body loginRequest: LoginRequest): Response<LoginResponse>

    //    Profile
    @GET("api/profile")
    suspend fun getProfileUser(): Response<ProfileResponse>

    @POST("api/logout")
    suspend fun doLogout(): Response<LoginResponse>

    //    BLOCK AND AREAS
    @GET("api/block-areas")
    suspend fun getBlockAreas(): Response<List<BlockAndAreasResponseItem>>

    @GET("api/block-area/{id}")
    suspend fun getBlockArea(
        @Path("id") id: String
    ): Response<BlockAndAreasResponseItem>

    @GET("api/block-area-info/{id}")
    suspend fun getBlockAreaInfo(
        @Path("id") id: String
    ): Response<BlockAreaInfoResponse>

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

    @POST("api/store-livestock")
    suspend fun storeLivestock(@Body storeLivestockRequest: StoreLivestockRequest): Response<LivestockResponse>

    @PUT("api/livestock/{id}")
    suspend fun updateLivestockById(
        @Path("id") id: String,
        @Body livestockRequest: LivestockRequest
    ): Response<LivestockResponse>

    @DELETE("api/livestock/{id}")
    suspend fun deleteLivestockById(
        @Path("id") id: String
    ): Response<LivestockResponse>

    @GET("api/livestocks?gender=1")
    suspend fun getLivestocksMale(): Response<List<LivestockResponseItem>>

    @GET("api/livestocks?gender=2")
    suspend fun getLivestocksFemale(): Response<List<LivestockResponseItem>>

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

    @POST("api/bcs-record")
    suspend fun createBcsRecord(@Body livestockRecordRequest: LivestockRecordRequest): Response<BcsRecordResponse>

    // Weight Record
    @GET("api/weight-records")
    suspend fun getWeightRecords(): Response<List<WeightRecordResponseItem>>

    @POST("api/weight-record")
    suspend fun createWeightRecord(@Body livestockRecordRequest: LivestockRecordRequest): Response<WeightRecordResponse>

    // Health Record
    @GET("api/health-records")
    suspend fun getHealthRecords(): Response<List<HealthRecordResponseItem>>

    @POST("api/health-record")
    suspend fun createHealthRecord(@Body healthRecordRequest: HealthRecordRequest): Response<HealthRecordResponse>

    //    Farm Profile
    @GET("api/farm-profile")
    suspend fun getFarmProfile(): Response<FarmProfileResponse>

    //    Farm Profile
    @POST("api/feeding-record")
    suspend fun createFeedingRecord(@Body feedingRecordRequest: FeedingRecordRequest): Response<FeedingRecordResponse>

    // Breeding
    @GET("api/breedings")
    suspend fun getBreedings(): Response<List<BreedingResponseItem>>

    @GET("api/breeding/{id}")
    suspend fun getBreedingById(@Path("id") id: String): Response<BreedingByIdResponse>

    @POST("api/breeding")
    suspend fun createBreeding(@Body breedingRequest: CreateBreedingRequest): Response<BreedingResponse>

    @DELETE("api/breeding/{id}")
    suspend fun deleteBreedingById(@Path("id") id: String): Response<BreedingResponse>

    @POST("api/breeding-history")
    suspend fun createHistoryBreeding(@Body historyBreedingRequest: HistoryBreedingRequest): Response<BreedingResponse>

    @DELETE("api/breeding/lambing/{id}")
    suspend fun deleteLambing(@Path("id") id: String): Response<BreedingResponse>

    @POST("api/breeding/lambing")
    suspend fun createLambing(@Body lambingRequest: LambingRequest): Response<BreedingResponse>

    @PUT("api/pregnancy/{id}")
    suspend fun updatePregnancy(
        @Path("id") id: String,
        @Body pregnancyRequest: PregnancyRequest
    ): Response<BreedingResponse>

    // Budget
    @GET("api/budget")
    suspend fun getBudgetByMonth(@Query("month-year") monthYear: String): Response<BudgetResponse>

    @GET("api/budget-item/{id}")
    suspend fun getBudgetById(@Path("id") id: String): Response<BudgetItemResponse>

    @GET("api/budget-categories")
    suspend fun getBudgetCategories(): Response<List<CategoriesResponseItem>>

    @GET("api/budget-sub-category/{id}")
    suspend fun getBudgetSubCategoriesById(@Path("id") id: String): Response<List<CategoriesResponseItem>>

    @POST("api/budget")
    suspend fun addBudget(@Body addBudgetRequest: AddBudgetRequest): Response<BudgetResponse>

    // Expenditure
    @POST("api/expenditure")
    suspend fun addExpenditure(@Body addExpenditureRequest: AddExpenditureRequest): Response<ExpenditureResponse>

    @DELETE("api/expenditure/{id}")
    suspend fun deleteExpenditure(@Path("id") id: String): Response<ExpenditureResponse>

}