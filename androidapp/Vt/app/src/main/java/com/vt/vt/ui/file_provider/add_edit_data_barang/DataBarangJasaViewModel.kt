package com.vt.vt.ui.file_provider.add_edit_data_barang

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vt.vt.core.data.source.base.BaseViewModel
import com.vt.vt.core.data.source.remote.products.model.ProductRequest
import com.vt.vt.core.data.source.remote.products.model.ProductResponse
import com.vt.vt.core.data.source.remote.products.model.ProductResponseItem
import com.vt.vt.core.data.source.repository.ProductsVtRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class DataBarangJasaViewModel @Inject constructor(private val productsVtRepository: ProductsVtRepository) :
    BaseViewModel() {

    private val _createProduct = MutableLiveData<ProductResponse>()
    val isCreatedProduct: LiveData<ProductResponse> = _createProduct

    private val _getProductEmitter = MutableLiveData<ProductResponseItem?>()
    val getProductEmitter: LiveData<ProductResponseItem?> = _getProductEmitter

    private val _updateProductEmitter = MutableLiveData<ProductResponse?>()
    val isUpdatedEmitter: LiveData<ProductResponse?> = _updateProductEmitter
    fun createProduct(
        categoryId: Int, name: String, description: String, unitMeasurement: String
    ) {
        launch(action = {
            val productsRequest = ProductRequest(categoryId, unitMeasurement, name, description)
            val response = productsVtRepository.createProducts(productsRequest)
            if (response.isSuccessful) {
                _createProduct.postValue(response.body())
            } else if (response.code() == 500) {
                val errorBody = JSONObject(response.errorBody()!!.charStream().readText())
                val message = errorBody.getString("message")
                Log.e("barang error", "error viewmodel 500 ${message.toString()}")
                Log.e("barang error", "error viewmodel 500 ${response.body()?.status}")
                Log.e("barang error", "error viewmodel 500 ${response.body()?.message}")

            } else {
                Log.e("barang error", "error ${response.body()}")
                val errorBody = JSONObject(response.errorBody()!!.charStream().readText())
                val message = errorBody.getString("message")
                isError.postValue(message.toString())
            }
        }, error = { networkError ->
            Log.e("barang error", "can'r create")
            if (networkError.isNetworkError) {
                isError.postValue("No Internet Connection")
            }
        })
    }

    fun getProductById(id: String) {
        launch(action = {
            val response = productsVtRepository.getProductById(id)
            if (response.isSuccessful) {
                _getProductEmitter.postValue(response.body())
            } else {
                val errorBody = JSONObject(response.errorBody()!!.charStream().readText())
                val message = errorBody.getString("message")
                isError.postValue(message)
            }
        }, error = { networkError ->
            if (networkError.isNetworkError) {
                isError.postValue("No Internet Connection")
            }
        })
    }

    fun updateBarangJasa(
        id: String, categoryId: Int, name: String, description: String, unitMeasurement: String
    ) {
        launch(action = {
            val productRequest = ProductRequest(categoryId, unitMeasurement, name, description)
            val response = productsVtRepository.updateProductById(id, productRequest)
            if (response.isSuccessful) {
                _updateProductEmitter.postValue(response.body())
            } else {
                val errorBody = JSONObject(response.errorBody()!!.charStream().readText())
                val message = errorBody.getString("message")
                isError.postValue(message)
            }
        }, error = { networkError ->
            if (networkError.isNetworkError) {
                isError.postValue("No Internet Connection")
            }
        })
    }
}