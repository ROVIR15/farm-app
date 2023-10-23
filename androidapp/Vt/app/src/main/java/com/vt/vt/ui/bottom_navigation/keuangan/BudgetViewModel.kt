package com.vt.vt.ui.bottom_navigation.keuangan

import android.annotation.SuppressLint
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vt.vt.core.data.source.base.BaseViewModel
import com.vt.vt.core.data.source.remote.budget.BudgetItemResponse
import com.vt.vt.core.data.source.remote.budget.BudgetResponse
import com.vt.vt.core.data.source.remote.categories.model.CategoriesResponseItem
import com.vt.vt.core.data.source.repository.BudgetVtRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class BudgetViewModel @Inject constructor(private val budgetVtRepository: BudgetVtRepository) :
    BaseViewModel() {

    private val _currentDate = MutableLiveData<String>()
    val currentDate: LiveData<String> = _currentDate

    private val _budgetEmmiter = MutableLiveData<BudgetResponse>()
    val budgetEmmiter: LiveData<BudgetResponse> = _budgetEmmiter

    private val _budgetByIdEmmiter = MutableLiveData<BudgetItemResponse>()
    val budgetByIdEmmiter: LiveData<BudgetItemResponse> = _budgetByIdEmmiter

    private val _categoriesBudgetEmitter = MutableLiveData<List<CategoriesResponseItem>>()
    val categoriesBudgetEmitter: LiveData<List<CategoriesResponseItem>> = _categoriesBudgetEmitter

    private val _subCategoriesBudgetEmitter = MutableLiveData<List<CategoriesResponseItem>>()
    val subCategoriesBudgetEmitter: LiveData<List<CategoriesResponseItem>> =
        _subCategoriesBudgetEmitter

    init {
        _currentDate.value = getCurrentDate()
    }

    fun loadBudgetByMonth(monthYear: String) {
        launch(action = {
            val response = budgetVtRepository.getBudgetByMonth(monthYear)
            if (response.isSuccessful) {
                _budgetEmmiter.postValue(response.body())
            } else {
                val errorBody = JSONObject(response.errorBody()!!.charStream().readText())
                val message = errorBody.getString("message")
                isError.postValue(message.toString())
            }
        }, error = { if (it.isNetworkError) isError.postValue("No Internet Connection") })
    }

    fun getBudgetById(id: String) {
        launch(action = {
            val response = budgetVtRepository.getBudgetById(id)
            if (response.isSuccessful) {
                _budgetByIdEmmiter.postValue(response.body())
            } else {
                val errorBody = JSONObject(response.errorBody()!!.charStream().readText())
                val message = errorBody.getString("message")
                isError.postValue(message.toString())
            }
        }, error = { if (it.isNetworkError) isError.postValue("No Internet Connection") })
    }

    fun getCategoriesBudget() {
        launch(action = {
            val response = budgetVtRepository.getCategoriesBudget()
            if (response.isSuccessful) {
                _categoriesBudgetEmitter.postValue(response.body())
            } else {
                val errorBody = JSONObject(response.errorBody()!!.charStream().readText())
                val message = errorBody.getString("message")
                isError.postValue(message.toString())
            }
        }, error = { if (it.isNetworkError) isError.postValue("No Internet Connection") })
    }

    fun getSubCategoriesBudget(categoriesId: String) {
        launch(action = {
            val response = budgetVtRepository.getSubCategoriesBudget(categoriesId)
            if (response.isSuccessful) {
                _subCategoriesBudgetEmitter.postValue(response.body())
            } else {
                val errorBody = JSONObject(response.errorBody()!!.charStream().readText())
                val message = errorBody.getString("message")
                isError.postValue(message.toString())
            }
        }, error = { if (it.isNetworkError) isError.postValue("No Internet Connection") })
    }

    @SuppressLint("SimpleDateFormat")
    private fun getCurrentDate(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val currentDate = LocalDate.now()
            val formatter = DateTimeFormatter.ofPattern("MM-yyyy")
            currentDate.format(formatter)
        } else {
            val currentDate = Date()
            val formatter = SimpleDateFormat("MM-yyyy")
            formatter.format(currentDate)
        }
    }

    fun updateSelectedDate(newDate: String) {
        _currentDate.value = newDate
    }

}