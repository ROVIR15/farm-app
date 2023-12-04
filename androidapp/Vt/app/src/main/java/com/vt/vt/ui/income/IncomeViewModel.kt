package com.vt.vt.ui.income

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vt.vt.core.data.source.base.BaseViewModel
import com.vt.vt.core.data.source.remote.budget.BudgetResponse
import com.vt.vt.core.data.source.remote.income.IncomeCategoriesResponseItem
import com.vt.vt.core.data.source.remote.income.IncomeRequest
import com.vt.vt.core.data.source.remote.income.IncomeResponse
import com.vt.vt.core.data.source.remote.income.IncomesItem
import com.vt.vt.core.data.source.repository.IncomeVtRepository
import com.vt.vt.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class IncomeViewModel @Inject constructor(private val incomeVtRepository: IncomeVtRepository) :
    BaseViewModel() {

    private val _incomeByIdEmitter = MutableLiveData<IncomesItem>()
    val incomeByIdEmitter: LiveData<IncomesItem> = _incomeByIdEmitter

    private val _deleteIncomeEmitter = MutableLiveData<BudgetResponse>()
    val deleteIncomeEmitter: LiveData<BudgetResponse> = _deleteIncomeEmitter

    private val _createIncomeEmitter = MutableLiveData<IncomeResponse>()
    val createIncomeEmitter: LiveData<IncomeResponse> = _createIncomeEmitter

    private val _updateIncomeEmitter = MutableLiveData<IncomeResponse>()
    val updateIncomeEmitter: LiveData<IncomeResponse> = _updateIncomeEmitter

    private val _incomeCategoriesEmitter = MutableLiveData<List<IncomeCategoriesResponseItem>>()
    val incomeCategoriesEmitter: LiveData<List<IncomeCategoriesResponseItem>> =
        _incomeCategoriesEmitter

    fun getIncomeById(incomeId: String) {
        launch(action = {
            val response = incomeVtRepository.getIncomeById(incomeId)
            if (response.isSuccessful) {
                _incomeByIdEmitter.postValue(response.body())
            } else {
                val errorBody = JSONObject(response.errorBody()!!.charStream().readText())
                val message = errorBody.getString("message")
                isError.postValue(message.toString())
            }
        }, error = { if (it.isNetworkError) isError.postValue("No Internet Connection") })
    }

    fun createIncome(date: String, amount: BigDecimal, remarks: String?, incomeCategoryId: Int?) {
        launch(action = {
            val incomeRequest = IncomeRequest(date, incomeCategoryId, amount, remarks)
            val response = incomeVtRepository.createIncome(incomeRequest)
            if (response.isSuccessful) {
                _createIncomeEmitter.postValue(response.body())
            } else {
                val errorBody = JSONObject(response.errorBody()!!.charStream().readText())
                val message = errorBody.getString("message")
                isError.postValue(message.toString())
            }
        }, error = { if (it.isNetworkError) isError.postValue("No Internet Connection") })
    }

    fun updateIncome(
        id: String, date: String, amount: BigDecimal, remarks: String?, incomeCategoryId: Int?
    ) {
        launch(action = {
            val incomeRequest = IncomeRequest(date, incomeCategoryId, amount, remarks)
            val response = incomeVtRepository.updateIncomeById(id, incomeRequest)
            if (response.isSuccessful) {
                _updateIncomeEmitter.postValue(response.body())
            } else {
                val errorBody = JSONObject(response.errorBody()!!.charStream().readText())
                val message = errorBody.getString("message")
                isError.postValue(message.toString())
            }
        }, error = { if (it.isNetworkError) isError.postValue("No Internet Connection") })
    }

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

    fun incomeCategories() {
        launch(action = {
            val response = incomeVtRepository.incomeCategories()
            if (response.isSuccessful) {
                _incomeCategoriesEmitter.postValue(response.body())
            } else {
                val errorBody = JSONObject(response.errorBody()!!.charStream().readText())
                val message = errorBody.getString("message")
                isError.postValue(message.toString())
            }
        }, error = { if (it.isNetworkError) isError.postValue("No Internet Connection") })
    }
}