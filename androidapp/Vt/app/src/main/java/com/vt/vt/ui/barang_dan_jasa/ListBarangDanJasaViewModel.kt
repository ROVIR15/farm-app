package com.vt.vt.ui.barang_dan_jasa

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vt.vt.core.data.source.base.BaseViewModel
import com.vt.vt.core.data.source.remote.products.model.ProductResponseItem
import com.vt.vt.core.data.source.repository.ProductsVtRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class ListBarangDanJasaViewModel @Inject constructor(private val productsVtRepository: ProductsVtRepository) :
    BaseViewModel() {

    private val _productsEmitter = MutableLiveData<List<ProductResponseItem>>()
    val productsEmitter: LiveData<List<ProductResponseItem>> = _productsEmitter

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
}