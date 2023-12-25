package com.vt.vt.core.data.source.repository

import com.vt.vt.core.data.source.remote.ApiService
import com.vt.vt.core.data.source.remote.budget.dto.AddBudgetRequest
import com.vt.vt.core.data.source.remote.budget.dto.BudgetItemResponse
import com.vt.vt.core.data.source.remote.budget.dto.BudgetResponse
import com.vt.vt.core.data.source.remote.categories.dto.CategoriesResponseItem
import retrofit2.Response
import javax.inject.Inject

class BudgetVtRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getCategoriesBudget(): Response<List<CategoriesResponseItem>> =
        apiService.getBudgetCategories()

    suspend fun getSubCategoriesBudget(id: String): Response<List<CategoriesResponseItem>> =
        apiService.getBudgetSubCategoriesById(id)

    suspend fun getBudgetByMonth(monthYear: String): Response<BudgetResponse> =
        apiService.getBudgetByMonth(monthYear)

    suspend fun getBudgetById(id: String): Response<BudgetItemResponse> =
        apiService.getBudgetById(id)

    suspend fun addBudget(addBudgetRequest: AddBudgetRequest): Response<BudgetResponse> =
        apiService.addBudget(addBudgetRequest)

    suspend fun deleteBudgetById(id: String): Response<BudgetResponse> =
        apiService.deleteBudgetById(id)
}