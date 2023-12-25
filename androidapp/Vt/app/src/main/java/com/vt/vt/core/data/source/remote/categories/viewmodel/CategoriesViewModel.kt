package com.vt.vt.core.data.source.remote.categories.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vt.vt.core.data.source.base.BaseViewModel
import com.vt.vt.core.data.source.remote.categories.dto.CategoriesResponseItem
import com.vt.vt.core.data.source.repository.CategoriesVtRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(private val categoriesVtRepository: CategoriesVtRepository) :
    BaseViewModel() {

    private val _categoriesEmitter = MutableLiveData<List<CategoriesResponseItem>>()
    val categoriesEmitter: LiveData<List<CategoriesResponseItem>> = _categoriesEmitter
    fun getAllCategories() {
        launch(action = {
            val response = categoriesVtRepository.getCategories()
            if (response.isSuccessful) {
                _categoriesEmitter.postValue(response.body())
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