package com.vt.vt.ui.penyimpan_ternak

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vt.vt.core.data.source.base.BaseViewModel
import com.vt.vt.core.data.source.remote.block_areas.model.BlockAndAreasResponseItem
import com.vt.vt.core.data.source.repository.VtRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class PenyimpanTernakViewModel @Inject constructor(private val vtRepository: VtRepository) :
    BaseViewModel() {
    private val _blockAndAreasEmitter = MutableLiveData<List<BlockAndAreasResponseItem>>()
    val allBlockAndAreas: LiveData<List<BlockAndAreasResponseItem>> = _blockAndAreasEmitter

    //val allBlockAndAreas = vtRepository.getAreasAndBlock().asLiveData()
    fun getAllBlockAndArea() {
        launch(
            action = {
                val response = vtRepository.getBlockAndAreas()
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
            }
        )
    }

}