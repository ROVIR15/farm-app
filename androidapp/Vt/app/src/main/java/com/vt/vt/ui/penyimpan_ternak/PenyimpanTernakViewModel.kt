package com.vt.vt.ui.penyimpan_ternak

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vt.vt.core.data.source.base.BaseViewModel
import com.vt.vt.core.data.source.remote.block_areas.model.BlockAndAreasResponse
import com.vt.vt.core.data.source.remote.block_areas.model.BlockAndAreasResponseItem
import com.vt.vt.core.data.source.repository.BlockAndAreasVtRepository
import com.vt.vt.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class PenyimpanTernakViewModel @Inject constructor(private val blockAndAreasVtRepository: BlockAndAreasVtRepository) :
    BaseViewModel() {
    private val _blockAndAreasEmitter = MutableLiveData<List<BlockAndAreasResponseItem>>()
    val allBlockAndAreas: LiveData<List<BlockAndAreasResponseItem>> = _blockAndAreasEmitter

    private val _deleteBlockAndArea = MutableLiveData<BlockAndAreasResponse?>()
    val deleteBlockAndArea: LiveData<BlockAndAreasResponse?> = _deleteBlockAndArea

    fun getAllBlockAndArea() {
        launch(action = {
            val response = blockAndAreasVtRepository.getBlockAndAreas()
            if (response.isSuccessful) {
                _blockAndAreasEmitter.postValue(response.body())
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

    fun deleteBlockAndArea(id: String) {
        launch(action = {
            val response = blockAndAreasVtRepository.deleteBlockAndArea(id)
            if (response.isSuccessful) {
                _deleteBlockAndArea.postValue(response.body())
                _isDeleted.postValue(Event(response.body()?.message.toString()))
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