package com.vt.vt.core.data.source.repository

import com.vt.vt.core.data.source.remote.ApiService
import com.vt.vt.core.data.source.remote.expenditure.AddExpenditureRequest
import com.vt.vt.core.data.source.remote.expenditure.ExpenditureResponse
import retrofit2.Response
import javax.inject.Inject

class ExpenditureVtRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun addExpenditure(addExpenditureRequest: AddExpenditureRequest): Response<ExpenditureResponse> =
        apiService.addExpenditure(addExpenditureRequest)
}