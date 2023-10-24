package com.vt.vt.ui.pengeluaran

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vt.vt.core.data.source.base.BaseViewModel
import com.vt.vt.core.data.source.remote.expenditure.AddExpenditureRequest
import com.vt.vt.core.data.source.remote.expenditure.ExpenditureResponse
import com.vt.vt.core.data.source.repository.ExpenditureVtRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class ExpenditureViewModel @Inject constructor(private val expenditureVtRepository: ExpenditureVtRepository) :
    BaseViewModel() {

    private val _addExpenditureEmitter = MutableLiveData<ExpenditureResponse>()
    val addExpenditureEmitter: LiveData<ExpenditureResponse> = _addExpenditureEmitter

    private val _deleteExpenditureEmitter = MutableLiveData<ExpenditureResponse>()
    val deleteExpenditureEmitter: LiveData<ExpenditureResponse> = _deleteExpenditureEmitter

    fun addExpenditure(
        budgetCategoryId: Int,
        budgetSubCategoryId: Int,
        amount: Double,
        skuId: Int?,
        remarks: String?,
        date: String
    ) {
        launch(action = {
            val addExpenditureRequest = AddExpenditureRequest(
                budgetCategoryId,
                budgetSubCategoryId,
                amount,
                skuId,
                remarks,
                date
            )
            val response = expenditureVtRepository.addExpenditure(addExpenditureRequest)
            if (response.isSuccessful) {
                _addExpenditureEmitter.postValue(response.body())
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

    fun deleteExpenditure(id: String) {
        launch(action = {
            val response = expenditureVtRepository.deleteExpenditure(id)
            if (response.isSuccessful) {
                _deleteExpenditureEmitter.postValue(response.body())
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