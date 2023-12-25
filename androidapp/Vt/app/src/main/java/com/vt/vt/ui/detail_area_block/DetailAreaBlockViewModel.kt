package com.vt.vt.ui.detail_area_block

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vt.vt.core.data.source.base.BaseViewModel
import com.vt.vt.core.data.source.remote.sleds.model.MoveSledRequest
import com.vt.vt.core.data.source.remote.sleds.model.SledsResponse
import com.vt.vt.core.data.source.remote.sleds.model.SledsResponseItem
import com.vt.vt.core.data.source.repository.SledsVtRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailAreaBlockViewModel @Inject constructor(
    private val sledsVtRepository: SledsVtRepository
) :
    BaseViewModel() {
    private val _sledsEmitter = MutableLiveData<List<SledsResponseItem>>()
    val sledItems: LiveData<List<SledsResponseItem>> = _sledsEmitter

    private val _moveSledEmitter = MutableLiveData<SledsResponse>()
    val moveSledEmitter: LiveData<SledsResponse> = _moveSledEmitter

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

    fun moveSledToBlockArea(id: String, blockAreaId: Int) {
        launch(action = {
            val moveSledRequest = MoveSledRequest(blockAreaId)
            val response = sledsVtRepository.moveSledToBlockArea(id, moveSledRequest)
            if (response.isSuccessful) {
                _moveSledEmitter.postValue(response.body())
            } else {
                Log.e(DetailAreaBlockViewModel::class.java.simpleName, "error : ${response.body()}")
                isError.postValue(response.message())
            }
        }, error = { networkError ->
            if (networkError.isNetworkError) {
                isError.postValue("No Internet Connection")
            }
        })
    }

}
