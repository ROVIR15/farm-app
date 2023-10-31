package com.vt.vt.ui.income

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vt.vt.core.data.source.base.BaseViewModel
import com.vt.vt.core.data.source.remote.budget.BudgetResponse
import com.vt.vt.core.data.source.repository.IncomeVtRepository
import com.vt.vt.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class IncomeViewModel @Inject constructor(private val incomeVtRepository: IncomeVtRepository) :
    BaseViewModel() {

    private val _deleteIncomeEmitter = MutableLiveData<BudgetResponse>()
    val deleteIncomeEmitter: LiveData<BudgetResponse> = _deleteIncomeEmitter

    fun deleteIncomeById(id: String) {
        launch(action = {
            val response = incomeVtRepository.deleteIncomeById(id)
            if (response.isSuccessful) {
                _deleteIncomeEmitter.postValue(response.body())
                _isDeleted.postValue(Event(response.body()?.message.toString()))
            } else {
                val errorBody = JSONObject(response.errorBody()!!.charStream().readText())
                val message = errorBody.getString("message")
                isError.postValue(message.toString())
            }
        }, error = { if (it.isNetworkError) isError.postValue("No Internet Connection") })
    }
}