package com.vt.vt.core.data.source.repository

import com.vt.vt.core.data.source.remote.ApiService
import com.vt.vt.core.data.source.remote.budget.BudgetResponse
import com.vt.vt.core.data.source.remote.income.IncomeCategoriesResponse
import com.vt.vt.core.data.source.remote.income.IncomeCategoriesResponseItem
import com.vt.vt.core.data.source.remote.income.IncomeRequest
import com.vt.vt.core.data.source.remote.income.IncomeResponse
import retrofit2.Response
import javax.inject.Inject

class IncomeVtRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun incomeCategories(): Response<List<IncomeCategoriesResponseItem>> =
        apiService.incomeCategories()

    suspend fun createIncome(incomeRequest: IncomeRequest): Response<IncomeResponse> =
        apiService.createIncome(incomeRequest)

    suspend fun deleteIncomeById(id: String): Response<BudgetResponse> =
        apiService.deleteIncomeById(id)
}