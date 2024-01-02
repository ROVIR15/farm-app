package com.vt.vt.ui.file_provider.addlivestock

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vt.vt.core.data.source.base.BaseViewModel
import com.vt.vt.core.data.source.remote.livestock.dto.LivestockRequest
import com.vt.vt.core.data.source.remote.livestock.dto.LivestockResponse
import com.vt.vt.core.data.source.remote.livestock.dto.StoreLivestockRequest
import com.vt.vt.core.data.source.remote.upload_image.PostFileResponse
import com.vt.vt.core.data.source.repository.LivestockVtRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class AddLivestockViewModel @Inject constructor(private val livestockVtRepository: LivestockVtRepository) :
    BaseViewModel() {
    private val _createLivestock = MutableLiveData<LivestockResponse>()
    val createLivestock: LiveData<LivestockResponse> = _createLivestock

    private val _postImageLivestock = MutableLiveData<PostFileResponse>()
    val postImageLivestock: LiveData<PostFileResponse> = _postImageLivestock

    private val _storeLivestock = MutableLiveData<LivestockResponse>()
    val storeLivestock: LiveData<LivestockResponse> = _storeLivestock

    fun postImageLivestock(file: MultipartBody.Part) {
        launch(action = {
            val response = livestockVtRepository.postImageLivestock(file)
            if (response.isSuccessful) {
                _postImageLivestock.postValue(response.body())
            } else {
                isError.postValue(response.message())
            }
        }, error = { networkError ->
            if (networkError.isNetworkError) {
                isError.postValue("No Internet Connection")
            }
        })
    }

    fun createLivestock(
        name: String?,
        birthDate: String?,
        description: String?,
        gender: Int,
        bangsa: String?,
        file: String?
    ) {
        launch(action = {
            val livestockRequest = LivestockRequest(
                bangsa,
                gender,
                name,
                birthDate,
                description,
                null,
                null,
                file

            )
            val response = livestockVtRepository.createLivestock(livestockRequest)
            if (response.isSuccessful) {
                _createLivestock.postValue(response.body())
            } else {
                Log.d("AddLivestock", "createLivestock: ${response.body()} ")
                val errorBody = JSONObject(response.errorBody()!!.charStream().readText())
                val message = errorBody.getString("message")
                isError.postValue(message.toString())
            }
        }, error = { networkError ->
            Log.e("AddLivestock", "errorrr 1 : ${networkError.errorMessage} ")
            Log.e("AddLivestock", "errorrr 2 : ${networkError.error} ")
            if (networkError.isNetworkError) {
                isError.postValue("No Internet Connection")
            }
        })
    }

    fun storeLivestock(livestockId: Int, sledId: Int, blockAreaId: Int) {
        launch(action = {
            val storeLivestockRequest = StoreLivestockRequest(livestockId, sledId, blockAreaId)
            val response = livestockVtRepository.storeLivestock(storeLivestockRequest)
            if (response.isSuccessful) {
                _storeLivestock.postValue(response.body())
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