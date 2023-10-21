package com.vt.vt.core.data.source.repository

import com.vt.vt.core.data.source.remote.ApiService
import com.vt.vt.core.data.source.remote.budget.BudgetItemResponse
import com.vt.vt.core.data.source.remote.budget.BudgetResponse
import retrofit2.Response
import javax.inject.Inject

class BudgetVtRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getBudgetByMonth(monthYear: String): Response<BudgetResponse> =
        apiService.getBudgetByMonth(monthYear)

    suspend fun getBudgetById(id: String): Response<BudgetItemResponse> =
        apiService.getBudgetById(id)
}