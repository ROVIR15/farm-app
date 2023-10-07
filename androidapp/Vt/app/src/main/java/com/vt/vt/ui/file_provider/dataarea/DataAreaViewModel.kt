package com.vt.vt.ui.file_provider.dataarea

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vt.vt.core.data.source.base.BaseViewModel
import com.vt.vt.core.data.source.remote.block_areas.model.BlockAndAreaRequest
import com.vt.vt.core.data.source.remote.block_areas.model.BlockAndAreasResponse
import com.vt.vt.core.data.source.remote.block_areas.model.BlockAndAreasResponseItem
import com.vt.vt.core.data.source.repository.BlockAndAreasVtRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class DataAreaViewModel @Inject constructor(private val blockAndAreasVtRepository: BlockAndAreasVtRepository) :
    BaseViewModel() {
    private val _createBlockAndArea = MutableLiveData<BlockAndAreasResponse?>()
    val isCreatedBlockAndArea: LiveData<BlockAndAreasResponse?> = _createBlockAndArea

    private val _getBlockArea = MutableLiveData<BlockAndAreasResponseItem>()
    val getBlockArea: LiveData<BlockAndAreasResponseItem> = _getBlockArea

    private val _updateBlockAndArea = MutableLiveData<BlockAndAreasResponse?>()
    val isUpdatedBlockAndArea: LiveData<BlockAndAreasResponse?> = _updateBlockAndArea

    fun createBlockAndArea(title: String?, description: String?) {
        launch(action = {
            val blockAndAreaModel = BlockAndAreaRequest(title, description)
            val response = blockAndAreasVtRepository.createBlockAndArea(blockAndAreaModel)
            if (response.isSuccessful) {
                _createBlockAndArea.postValue(response.body())
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

    fun getBlockArea(id: String) {
        launch(action = {
            val response = blockAndAreasVtRepository.getBlockAndArea(id)
            if (response.isSuccessful) {
                println("success ${response.body()}")
                _getBlockArea.postValue(response.body())
            } else {
                val errorBody = JSONObject(response.errorBody()!!.charStream().readText())
                val message = errorBody.getString("message")
                println("failed ${response.body()}")
                isError.postValue(message)
            }
        }, error = { networkError ->
            println("failed network ${networkError.isNetworkError}")
            if (networkError.isNetworkError) {
                isError.postValue("No Internet Connection")
            }
        })
    }

    fun updateBlockAndArea(id: String, name: String?, description: String?) {
        launch(action = {
            val blockAndAreaModel = BlockAndAreaRequest(name, description)
            val response = blockAndAreasVtRepository.updateBlockAndArea(id, blockAndAreaModel)
            if (response.isSuccessful) {
                _updateBlockAndArea.postValue(response.body())
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