package com.vt.vt.core.data.source.repository

import com.vt.vt.core.data.source.remote.ApiService
import com.vt.vt.core.data.source.remote.budget.dto.BudgetResponse
import com.vt.vt.core.data.source.remote.income.dto.IncomeCategoriesResponseItem
import com.vt.vt.core.data.source.remote.income.dto.IncomeRequest
import com.vt.vt.core.data.source.remote.income.dto.IncomeResponse
import com.vt.vt.core.data.source.remote.income.dto.IncomesItem
import retrofit2.Response
import javax.inject.Inject

class IncomeVtRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getIncomeById(id: String): Response<IncomesItem> =
        apiService.getIncomeById(id)

    suspend fun updateIncomeById(
        id: String,
        incomeRequest: IncomeRequest
    ): Response<IncomeResponse> = apiService.updateIncome(id, incomeRequest)

    suspend fun incomeCategories(): Response<List<IncomeCategoriesResponseItem>> =
        apiService.incomeCategories()

    suspend fun createIncome(incomeRequest: IncomeRequest): Response<IncomeResponse> =
        apiService.createIncome(incomeRequest)

    suspend fun deleteIncomeById(id: String): Response<BudgetResponse> =
        apiService.deleteIncomeById(id)
}