package com.vt.vt.core.data.source.remote

import com.vt.vt.core.data.source.remote.auth.dto.change_password.ChangePasswordRequest
import com.vt.vt.core.data.source.remote.auth.dto.change_password.ChangePasswordResponse
import com.vt.vt.core.data.source.remote.auth.dto.login.*
import com.vt.vt.core.data.source.remote.auth.dto.register.request.*
import com.vt.vt.core.data.source.remote.auth.dto.register.response.*
import com.vt.vt.core.data.source.remote.bcs_record.dto.*
import com.vt.vt.core.data.source.remote.block_areas.dto.*
import com.vt.vt.core.data.source.remote.breeding.*
import com.vt.vt.core.data.source.remote.breeding.dto.BreedingByIdResponse
import com.vt.vt.core.data.source.remote.breeding.dto.BreedingResponse
import com.vt.vt.core.data.source.remote.breeding.dto.BreedingResponseItem
import com.vt.vt.core.data.source.remote.breeding.dto.create.CreateBreedingRequest
import com.vt.vt.core.data.source.remote.breeding.dto.history.create.HistoryBreedingRequest
import com.vt.vt.core.data.source.remote.breeding.dto.lambing.create.LambingRequest
import com.vt.vt.core.data.source.remote.breeding.dto.pregnancy.PregnancyRequest
import com.vt.vt.core.data.source.remote.budget.*
import com.vt.vt.core.data.source.remote.budget.dto.AddBudgetRequest
import com.vt.vt.core.data.source.remote.budget.dto.BudgetItemResponse
import com.vt.vt.core.data.source.remote.budget.dto.BudgetResponse
import com.vt.vt.core.data.source.remote.categories.dto.*
import com.vt.vt.core.data.source.remote.expenditure.dto.AddExpenditureRequest
import com.vt.vt.core.data.source.remote.expenditure.dto.ExpenditureResponse
import com.vt.vt.core.data.source.remote.farm_profile.dto.*
import com.vt.vt.core.data.source.remote.fattening.dto.FatteningResponse
import com.vt.vt.core.data.source.remote.feeding_record.dto.*
import com.vt.vt.core.data.source.remote.health_record.dto.*
import com.vt.vt.core.data.source.remote.height_record.dto.HeightRecordResponse
import com.vt.vt.core.data.source.remote.income.dto.IncomeCategoriesResponseItem
import com.vt.vt.core.data.source.remote.income.dto.IncomeRequest
import com.vt.vt.core.data.source.remote.income.dto.IncomeResponse
import com.vt.vt.core.data.source.remote.income.dto.IncomesItem
import com.vt.vt.core.data.source.remote.livestock.dto.*
import com.vt.vt.core.data.source.remote.products.dto.*
import com.vt.vt.core.data.source.remote.profile.dto.*
import com.vt.vt.core.data.source.remote.sleds.dto.*
import com.vt.vt.core.data.source.remote.upload_image.PostFileResponse
import com.vt.vt.core.data.source.remote.weight_record.dto.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
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

    @POST("api/change-password")
    suspend fun doChangePassword(@Body changePasswordRequest: ChangePasswordRequest): Response<ChangePasswordResponse>

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

    // Post Image Block Area
    @Multipart
    @POST("api/upload-block-area")
    suspend fun postImageBlockArea(@Part file: MultipartBody.Part): Response<PostFileResponse>

    @POST("api/block-area")
    suspend fun createBlockArea(@Body blockAndAreaRequest: BlockAndAreaRequest): Response<BlockAndAreasResponse>

    @DELETE("api/block-area/{id}")
    suspend fun deleteBlockArea(
        @Path("id") id: String
    ): Response<BlockAndAreasResponse>

    @PUT("api/block-area/{id}")
    suspend fun updateBlockArea(
        @Path("id") id: String, @Body blockAndAreaRequest: BlockAndAreaRequest
    ): Response<BlockAndAreasResponse>

    // SLED
    @GET("api/sleds")
    suspend fun getAllSleds(): Response<List<SledsResponseItem>>

    @GET("api/v1.1/options/sleds")
    suspend fun getSledOptions(): Response<List<SledOptionResponseItem>>

    @DELETE("/api/sled/{id}")
    suspend fun deleteSledById(
        @Path("id") id: String
    ): Response<SledsResponse>

    @GET("api/sled/{id}")
    suspend fun getSledById(
        @Path("id") id: String
    ): Response<SledsResponseItem>

    // Post Image Sled
    @Multipart
    @POST("api/upload-sled")
    suspend fun postImageSled(@Part file: MultipartBody.Part): Response<PostFileResponse>

    @POST("api/sled")
    suspend fun createSled(@Body sledRequest: SledRequest): Response<SledsResponse>

    @PUT("api/sled/{id}")
    suspend fun updateSled(
        @Path("id") id: String, @Body sledRequest: SledRequest
    ): Response<SledsResponse>

    @PUT("api/v1.1/sled/move-to-block-area/{id}")
    suspend fun sledMoveToBlockArea(
        @Path("id") id: String, @Body moveSledRequest: MoveSledRequest
    ): Response<SledsResponse>

    //    Livestock
    @GET("api/livestocks")
    suspend fun getLivestocks(): Response<List<LivestockResponseItem>>

    @GET("api/v1.1/search-livestocks")
    suspend fun getListBySearch(
        @Query("search_params") search_params: String?
    ): Response<List<LivestockResponseItem>>

    @GET("api/v1.1/livestock/{id}")
    suspend fun getLivestockById(
        @Path("id") id: String
    ): Response<LivestockByIdResponse>

    // Post Image Livestock
    @Multipart
    @POST("api/upload-livestock")
    suspend fun postImageLivestock(@Part file: MultipartBody.Part): Response<PostFileResponse>

    // Form Only
    @POST("api/livestock")
    suspend fun createLivestock(@Body livestockRequest: LivestockRequest): Response<LivestockResponse>

    @POST("api/v1.1/livestock/move-to-sled")
    suspend fun livestockMoveToSled(@Body livestockMoveSledRequest: LivestockMoveSledRequest): Response<LivestockResponse>

    @POST("api/store-livestock")
    suspend fun storeLivestock(@Body storeLivestockRequest: StoreLivestockRequest): Response<LivestockResponse>

    @PUT("api/livestock/{id}")
    suspend fun updateLivestockById(
        @Path("id") id: String, @Body livestockRequest: LivestockRequest
    ): Response<LivestockResponse>

    @DELETE("api/livestock/{id}")
    suspend fun deleteLivestockById(
        @Path("id") id: String
    ): Response<LivestockResponse>

    @GET("api/livestocks?gender=1")
    suspend fun getLivestocksMale(): Response<List<LivestockResponseItem>>

    @GET("api/livestocks?gender=2")
    suspend fun getLivestocksFemale(): Response<List<LivestockResponseItem>>

    @GET("api/v1.1/options/livestocks")
    suspend fun getListOptionLivestock(): Response<List<LivestockOptionResponseItem>>

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
        @Path("id") id: String, @Body productRequest: ProductRequest
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

    // Height Record

    @POST("api/v1.1/height-record")
    suspend fun createHeightRecord(@Body livestockRecordRequest: LivestockRecordRequest): Response<HeightRecordResponse>

    // Health Record
    @GET("api/health-records")
    suspend fun getHealthRecords(): Response<List<HealthRecordResponseItem>>

    @POST("api/health-record")
    suspend fun createHealthRecord(@Body healthRecordRequest: HealthRecordRequest): Response<HealthRecordResponse>

    //  Farm Profile
    //  Image Only
    @Multipart
    @POST("api/upload-farm-profile")
    suspend fun postImageFarmProfile(@Part file: MultipartBody.Part): Response<PostFileResponse>

    //  Form Only
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

//    @GET("api/v1.1/breeding-info/{id}")
//    suspend fun getBreedingInfoById(@Path("id") id: String): Response<BreedingByIdResponse>

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
        @Path("id") id: String, @Body pregnancyRequest: PregnancyRequest
    ): Response<BreedingResponse>

    // Fattening
    @GET("api/feeding-graph")
    suspend fun getFatteningGraph(@Query("month-year") monthYear: String): Response<FatteningResponse>

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

    @DELETE("api/budget-item/{id}")
    suspend fun deleteBudgetById(@Path("id") id: String): Response<BudgetResponse>

    // Expenditure
    @POST("api/expenditure")
    suspend fun addExpenditure(@Body addExpenditureRequest: AddExpenditureRequest): Response<ExpenditureResponse>

    @DELETE("api/expenditure/{id}")
    suspend fun deleteExpenditure(@Path("id") id: String): Response<ExpenditureResponse>

    // Income
    @GET("api/income/{id}")
    suspend fun getIncomeById(@Path("id") id: String): Response<IncomesItem>

    @PUT("api/income/{id}")
    suspend fun updateIncome(
        @Path("id") id: String, @Body incomeRequest: IncomeRequest
    ): Response<IncomeResponse>


    @GET("api/income-categories")
    suspend fun incomeCategories(): Response<List<IncomeCategoriesResponseItem>>

    @POST("api/income")
    suspend fun createIncome(@Body incomeRequest: IncomeRequest): Response<IncomeResponse>

    @DELETE("/api/income/{id}")
    suspend fun deleteIncomeById(@Path("id") id: String): Response<BudgetResponse>

}