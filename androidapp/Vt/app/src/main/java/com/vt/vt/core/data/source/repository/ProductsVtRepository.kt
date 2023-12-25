package com.vt.vt.core.data.source.repository

import com.vt.vt.core.data.source.remote.ApiService
import com.vt.vt.core.data.source.remote.products.dto.ProductRequest
import com.vt.vt.core.data.source.remote.products.dto.ProductResponse
import com.vt.vt.core.data.source.remote.products.dto.ProductResponseItem
import retrofit2.Response
import javax.inject.Inject

class ProductsVtRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getProducts(): Response<List<ProductResponseItem>> = apiService.getProducts()
    suspend fun createProducts(productRequest: ProductRequest): Response<ProductResponse> =
        apiService.createProduct(productRequest)

    suspend fun deleteProductById(id: String): Response<ProductResponse> =
        apiService.deleteProductById(id)

    suspend fun getProductById(id: String): Response<ProductResponseItem> =
        apiService.getProductById(id)

    suspend fun updateProductById(
        id: String,
        productRequest: ProductRequest
    ): Response<ProductResponse> =
        apiService.updateProductById(id, productRequest)
}