package com.vt.vt.ui.barang_dan_jasa

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vt.vt.core.data.source.base.BaseViewModel
import com.vt.vt.core.data.source.remote.products.dto.ProductResponse
import com.vt.vt.core.data.source.remote.products.dto.ProductResponseItem
import com.vt.vt.core.data.source.repository.ProductsVtRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class ListItemsAndServiceViewModel @Inject constructor(private val productsVtRepository: ProductsVtRepository) :
    BaseViewModel() {

    private val _productsEmitter = MutableLiveData<List<ProductResponseItem>>()
    val productsEmitter: LiveData<List<ProductResponseItem>> = _productsEmitter

    private val _deleteProductEmitter = MutableLiveData<ProductResponse>()
    val isDeletedProduct: LiveData<ProductResponse> = _deleteProductEmitter
    fun getProductBySkuId(id: Int): ProductResponseItem? {
        return _productsEmitter.value?.firstOrNull { it.skuId == id }
    }

    fun getAllProducts() {
        launch(action = {
            val response = productsVtRepository.getProducts()
            if (response.isSuccessful) {
                _productsEmitter.postValue(response.body())
            } else {
                val errorBody = JSONObject(response.errorBody()!!.charStream().readText())
                val message = errorBody.getString("message")
                isError.postValue(message.toString())
            }
        }, error = { networkError ->
            if (networkError.isNetworkError) {
                isError.postValue("No Internet Connection")
            }
        })
    }

    fun deleteProduct(id: String) {
        launch(action = {
            val response = productsVtRepository.deleteProductById(id)
            if (response.isSuccessful) {
                _deleteProductEmitter.postValue(response.body())
            } else {
                val errorBody = JSONObject(response.errorBody()!!.charStream().readText())
                val message = errorBody.getString("message")
                isError.postValue(message.toString())
            }
        }, error = { networkError ->
            if (networkError.isNetworkError) {
                isError.postValue("No Internet Connection")
            }
        })
    }
}