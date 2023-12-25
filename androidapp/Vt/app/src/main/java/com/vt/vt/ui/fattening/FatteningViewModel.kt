package com.vt.vt.ui.fattening

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vt.vt.core.data.source.base.BaseViewModel
import com.vt.vt.core.data.source.remote.fattening.dto.FatteningResponse
import com.vt.vt.core.data.source.repository.FatteningVtRepository
import com.vt.vt.utils.PickDatesUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FatteningViewModel @Inject constructor(private val fatteningVtRepository: FatteningVtRepository) :
    BaseViewModel() {

    private val _currentDate = MutableLiveData<String>()
    val currentDate: LiveData<String> = _currentDate

    private val _getFatteningGraphEmitter = MutableLiveData<FatteningResponse>()
    val getFatteningGraphEmitter: LiveData<FatteningResponse> = _getFatteningGraphEmitter

    init {
        _currentDate.value = PickDatesUtils.getCurrentDate()
    }

    fun getFatteningGraph(monthAndYear: String) {
        launch(action = {
            val response = fatteningVtRepository.getFatteningGraph(monthAndYear)
            if (response.isSuccessful) {
                _getFatteningGraphEmitter.postValue(response.body())
            } else {
                isError.postValue(response.errorBody().toString())
            }
        }, error = { networkError ->
            if (networkError.isNetworkError) {
                isError.postValue("No Internet Connection")
            }
        })
    }

    fun updateSelectedDate(newDate: String) {
        _currentDate.value = newDate
    }
}