package com.vt.vt.ui.bottom_navigation.livestock

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.vt.vt.core.data.source.base.BaseViewModel
import com.vt.vt.core.data.source.remote.livestock.model.LivestockMoveSledRequest
import com.vt.vt.core.data.source.remote.livestock.model.LivestockOptionResponseItem
import com.vt.vt.core.data.source.remote.livestock.model.LivestockResponse
import com.vt.vt.core.data.source.remote.livestock.model.LivestockResponseItem
import com.vt.vt.core.data.source.repository.LivestockVtRepository
import com.vt.vt.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class LivestockViewModel @Inject constructor(private val livestockVtRepository: LivestockVtRepository) :
    BaseViewModel() {

    private val _livestockEmitter = MutableLiveData<List<LivestockResponseItem>>()
    val livestockItems: LiveData<List<LivestockResponseItem>> = _livestockEmitter

    private val _livestockOptionEmitter = MutableLiveData<List<LivestockOptionResponseItem>>()
    val livestockOptionEmitter: LiveData<List<LivestockOptionResponseItem>> =
        _livestockOptionEmitter

    private val _livestocksMaleEmitter = MutableLiveData<List<LivestockResponseItem>>()
    val livestocksMaleEmitter: LiveData<List<LivestockResponseItem>> = _livestocksMaleEmitter

    private val _livestocksFemaleEmitter = MutableLiveData<List<LivestockResponseItem>>()
    val livestocksFemaleEmitter: LiveData<List<LivestockResponseItem>> = _livestocksFemaleEmitter

    private val _livestockMoveSledEmitter = MutableLiveData<LivestockResponse>()
    val livestockMoveSledEmitter: LiveData<LivestockResponse> = _livestockMoveSledEmitter

    private val _deleteLivestock = MutableLiveData<LivestockResponse>()
    val deleteLivestock: LiveData<LivestockResponse> = _deleteLivestock

    fun filterLivestock(query: String?): LiveData<List<LivestockOptionResponseItem>> {
        return _livestockOptionEmitter.map { livestockList ->
            if (query.isNullOrBlank()) {
                livestockList
            } else {
                livestockList.filter { it.name?.contains(query, true) == true }
            }
        }
    }

    fun livestockMoveSled(livestockId: Int, sledId: Int, blockAreaId: Int) {
        launch(action = {
            val livestockMoveSledRequest =
                LivestockMoveSledRequest(livestockId, sledId, blockAreaId)
            val response = livestockVtRepository.livestockMoveSled(livestockMoveSledRequest)
            if (response.isSuccessful) {
                _livestockMoveSledEmitter.postValue(response.body())
            } else {
                isError.postValue(response.message())
            }
        }, error = { networkError ->
            if (networkError.isNetworkError) {
                isError.postValue("No Internet Connection")
            }
        })
    }

    fun getLivestocks() {
        launch(action = {
            val response = livestockVtRepository.getLivestock()
            if (response.isSuccessful) {
                _livestockEmitter.postValue(response.body())
            } else {
                val errorBody = JSONObject(response.errorBody()!!.charStream().readText())
                val message = errorBody.getString("message")
                isError.postValue(message.toString())
            }
        }, error = { networkError ->
            isError.postValue("You don't have any livestock")
            if (networkError.isNetworkError) {
                isError.postValue("No Internet Connection")
            }
        })
    }

    fun searchLivestock(query: String?) {
        launch(action = {
            val response = livestockVtRepository.searchLivestock(query)
            if (response.isSuccessful) {
                _livestockEmitter.postValue(response.body())
            } else {
                val errorBody = JSONObject(response.errorBody()!!.charStream().readText())
                val message = errorBody.getString("message")
                isError.postValue(message.toString())
            }
        }, error = { networkError ->
            isError.postValue("You don't have any livestock")
            if (networkError.isNetworkError) {
                isError.postValue("No Internet Connection")
            }
        })
    }

    fun deleteLivestockById(id: String) {
        launch(action = {
            val response = livestockVtRepository.deleteLivestockById(id)
            if (response.isSuccessful) {
                _deleteLivestock.postValue(response.body())
                _isDeleted.postValue(Event(response.body()?.message.toString()))
            } else {
                isError.postValue(response.errorBody().toString())
            }
        }, error = { networkError ->
            if (networkError.isNetworkError) {
                isError.postValue("No Internet Connection")
            }
        })
    }

    fun getListOptionLivestock() {
        launch(action = {
            val response = livestockVtRepository.getOptionLivestock()
            if (response.isSuccessful) {
                _livestockOptionEmitter.postValue(response.body())
            } else {
                val errorBody = JSONObject(response.errorBody()!!.charStream().readText())
                val message = errorBody.getString("message")
                isError.postValue(message.toString())
            }
        }, error = { networkError ->
            isError.postValue("You don't have any livestock")
            if (networkError.isNetworkError) {
                isError.postValue("No Internet Connection")
            }
        })
    }

    fun getLivestocksMale() {
        launch(action = {
            val response = livestockVtRepository.getLivestocksMale()
            if (response.isSuccessful) {
                val dataEmpty = mutableListOf(
                    livestockMaleResponseItem
                )
                val listToMutable = response.body()?.toMutableList()
                listToMutable?.addAll(0, dataEmpty)
                _livestocksMaleEmitter.postValue(listToMutable!!)
            } else {
                isError.postValue(response.errorBody().toString())
            }
        }, error = { networkError ->
            if (networkError.isNetworkError) {
                isError.postValue("No Internet Connection")
            }
        })
    }

    fun getLivestocksFemale() {
        launch(action = {
            val response = livestockVtRepository.getLivestocksFemale()
            if (response.isSuccessful) {
                val dataEmpty = mutableListOf(
                    livestockFemaleResponseItem
                )
                val listToMutable = response.body()?.toMutableList()
                listToMutable?.addAll(0, dataEmpty)
                _livestocksFemaleEmitter.postValue(listToMutable!!)
            } else {
                isError.postValue(response.errorBody().toString())
            }
        }, error = { networkError ->
            if (networkError.isNetworkError) {
                isError.postValue("No Internet Connection")
            }
        })
    }

    companion object {
        val livestockMaleResponseItem = LivestockResponseItem(
            null,
            "Bangsa",
            "2023-01-01",
            "2023",
            "Empty List",
            "1",
            "Empty List",
            "-- Pilih Livestock Jantan --"
        )
        val livestockFemaleResponseItem = LivestockResponseItem(
            null,
            "Bangsa",
            "2023-01-01",
            "2023",
            "Empty List",
            "1",
            "Empty List",
            "-- Pilih Livestock Betina --"
        )
    }
}