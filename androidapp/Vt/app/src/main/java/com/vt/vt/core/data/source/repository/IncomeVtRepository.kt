package com.vt.vt.core.data.source.repository

import com.vt.vt.core.data.source.remote.ApiService
import com.vt.vt.core.data.source.remote.budget.BudgetResponse
import retrofit2.Response
import javax.inject.Inject

class IncomeVtRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun deleteIncomeById(id: String): Response<BudgetResponse> =
        apiService.deleteIncomeById(id)
}