package com.vt.vt.core.data.source.repository

import com.vt.vt.core.data.source.remote.ApiService
import com.vt.vt.core.data.source.remote.breeding.dto.BreedingByIdResponse
import com.vt.vt.core.data.source.remote.breeding.dto.BreedingResponse
import com.vt.vt.core.data.source.remote.breeding.dto.BreedingResponseItem
import com.vt.vt.core.data.source.remote.breeding.dto.create.CreateBreedingRequest
import com.vt.vt.core.data.source.remote.breeding.dto.history.create.HistoryBreedingRequest
import com.vt.vt.core.data.source.remote.breeding.dto.lambing.create.LambingRequest
import com.vt.vt.core.data.source.remote.breeding.dto.pregnancy.PregnancyRequest
import retrofit2.Response
import javax.inject.Inject

class BreedingVtRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getAllBreedings(): Response<List<BreedingResponseItem>> = apiService.getBreedings()
    suspend fun getBreedingById(id: String): Response<BreedingByIdResponse> =
        apiService.getBreedingById(id)

    suspend fun createBreeding(breedingRequest: CreateBreedingRequest): Response<BreedingResponse> =
        apiService.createBreeding(breedingRequest)

    suspend fun deleteBreedingById(id: String): Response<BreedingResponse> =
        apiService.deleteBreedingById(id)

    suspend fun createHistoryBreeding(historyBreedingRequest: HistoryBreedingRequest): Response<BreedingResponse> =
        apiService.createHistoryBreeding(historyBreedingRequest)

    suspend fun deleteLambing(id: String): Response<BreedingResponse> =
        apiService.deleteLambing(id)

    suspend fun createLambing(lambingRequest: LambingRequest): Response<BreedingResponse> =
        apiService.createLambing(lambingRequest)

    suspend fun updatePregnancy(
        id: String,
        pregnancyRequest: PregnancyRequest
    ): Response<BreedingResponse> = apiService.updatePregnancy(id, pregnancyRequest)

}