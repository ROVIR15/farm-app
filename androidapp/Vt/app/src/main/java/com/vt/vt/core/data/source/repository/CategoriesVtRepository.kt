package com.vt.vt.core.data.source.repository

import com.vt.vt.core.data.source.remote.ApiService
import com.vt.vt.core.data.source.remote.categories.model.CategoriesResponse
import com.vt.vt.core.data.source.remote.categories.model.CategoriesResponseItem
import retrofit2.Response
import javax.inject.Inject

class CategoriesVtRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getCategories(): Response<List<CategoriesResponseItem>> = apiService.getCategories()
}