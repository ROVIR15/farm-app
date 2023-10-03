package com.vt.vt.ui.detail_area_block

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vt.vt.core.data.source.base.BaseViewModel
import com.vt.vt.core.data.source.remote.sleds.model.SledsResponse
import com.vt.vt.core.data.source.remote.sleds.model.SledsResponseItem
import com.vt.vt.core.data.source.repository.SledsVtRepository
import com.vt.vt.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailAreaBlockViewModel @Inject constructor(private val sledsVtRepository: SledsVtRepository) :
    BaseViewModel() {
    private val _sledsEmitter = MutableLiveData<List<SledsResponseItem>>()
    val sledItems: LiveData<List<SledsResponseItem>> = _sledsEmitter

    fun getSleds() {
        launch(action = {
            val response = sledsVtRepository.getSleds()
            if (response.isSuccessful) {
                _sledsEmitter.postValue(response.body())
            } else {
                isError.postValue(response.message())
            }
        }, error = { networkError ->
            if (networkError.isNetworkError) {
                isError.postValue("No Internet Connection")
            }
        })
    }
}