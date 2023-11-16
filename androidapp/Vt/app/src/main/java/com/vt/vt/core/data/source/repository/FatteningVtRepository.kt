package com.vt.vt.core.data.source.repository

import com.vt.vt.core.data.source.remote.ApiService
import com.vt.vt.core.data.source.remote.fattening.model.FatteningResponse
import retrofit2.Response
import javax.inject.Inject

class FatteningVtRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getFatteningGraph(monthAndYear: String): Response<FatteningResponse> =
        apiService.getFatteningGraph(monthAndYear)
}