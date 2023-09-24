package com.vt.vt.core.data.source.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vt.vt.core.data.source.network.NetworkError
import com.vt.vt.utils.Event
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {
    protected val isError = MutableLiveData<String>()
    fun isError(): LiveData<String> = isError

    protected val isLoading = MutableLiveData<Boolean>()
    fun observeLoading(): LiveData<Boolean> = isLoading

    protected val isEmptyData = MutableLiveData<Boolean>()
    fun observeEmptyData(): LiveData<Boolean> = isEmptyData

    protected val _isDeleted = MutableLiveData<Event<String>>()
    val isDeleted: LiveData<Event<String>> = _isDeleted


    protected val isException = MutableLiveData<Exception?>()
    fun observeException(): LiveData<Exception?> = isException

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        isError.postValue(throwable.message)
        isLoading.postValue(false)
    }

    protected fun launch(
        action: suspend () -> Unit,
        error: (NetworkError) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO + Job() + exceptionHandler) {
            isLoading.postValue(true)
            try {
                action.invoke()
            } catch (ex: Exception) {
                error.invoke(NetworkError(ex)).toString()
            } finally {
                isLoading.postValue(false)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Job().cancel()
    }
}