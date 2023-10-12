package com.vt.vt.core.data.source.repository

import com.vt.vt.core.data.source.remote.ApiService
import com.vt.vt.core.data.source.remote.breeding.BreedingByIdResponse
import com.vt.vt.core.data.source.remote.breeding.BreedingResponse
import com.vt.vt.core.data.source.remote.breeding.BreedingResponseItem
import com.vt.vt.core.data.source.remote.breeding.create.CreateBreedingRequest
import com.vt.vt.core.data.source.remote.breeding.history.create.HistoryBreedingRequest
import com.vt.vt.core.data.source.remote.breeding.lambing.create.LambingRequest
import retrofit2.Response
import javax.inject.Inject

class BreedingVtRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getAllBreedings(): Response<List<BreedingResponseItem>> = apiService.getBreedings()
    suspend fun getBreedingById(id: String): Response<BreedingByIdResponse> =
        apiService.getBreedingById(id)

    suspend fun createBreeding(breedingRequest: CreateBreedingRequest): Response<BreedingResponse> =
        apiService.createBreeding(breedingRequest)
    suspend fun createHistoryBreeding(historyBreedingRequest: HistoryBreedingRequest): Response<BreedingResponse> =
        apiService.
        createHistoryBreeding(historyBreedingRequest)
    suspend fun createLambing(lambingRequest: LambingRequest): Response<BreedingResponse> =
        apiService.
        createLambing(lambingRequest)

}