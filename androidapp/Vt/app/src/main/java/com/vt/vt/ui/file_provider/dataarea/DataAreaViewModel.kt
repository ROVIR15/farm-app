package com.vt.vt.ui.file_provider.dataarea

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vt.vt.core.data.source.base.BaseViewModel
import com.vt.vt.core.data.source.remote.block_areas.model.BlockAndAreaRequest
import com.vt.vt.core.data.source.remote.block_areas.model.BlockAndAreasResponse
import com.vt.vt.core.data.source.repository.VtRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class DataAreaViewModel @Inject constructor(private val vtRepository: VtRepository) :
    BaseViewModel() {
    private val _createBlockAndArea = MutableLiveData<BlockAndAreasResponse?>()
    val isCreatedBlockAndArea: LiveData<BlockAndAreasResponse?> = _createBlockAndArea
    fun createBlockAndArea(title: String?, description: String?) {
        launch(action = {
            val blockAndAreaModel = BlockAndAreaRequest(title, description)
            val response = vtRepository.createBlockAndArea(blockAndAreaModel)
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
}