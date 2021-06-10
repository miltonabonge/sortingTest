package com.naveen.sampleapp.ui.sort

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naveen.sampleapp.model.GroupedItem
import com.naveen.sampleapp.model.Item
import com.naveen.sampleapp.utils.ApiResult
import com.naveen.sampleapp.utils.RetrofitApi
import com.naveen.sampleapp.utils.handleApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SortByViewModel : ViewModel() {

    private val _itemsListLiveData = MutableLiveData<List<Item>>()
    private val _isLoading = MutableLiveData<Boolean>()
    private val _isError = MutableLiveData<String>()
    val itemsListLiveData: LiveData<List<Item>> = _itemsListLiveData
    val isLoading: LiveData<Boolean> = _isLoading
    val isError: LiveData<String?> = _isError

    init {
        viewModelScope.launch {
            getAllItemsGrouped()
        }
    }

    private suspend fun getAllItemsGrouped() {
        withContext(Dispatchers.IO) {
            _isLoading.postValue(true)
            val result: ApiResult<List<Item>> =
                handleApi({ RetrofitApi.retrofitService.getItemsList() })
            when (result) {
                is ApiResult.Success -> {
                    _isLoading.postValue(false)
                    if (result.data != null && result.data.isNotEmpty()) {
                        result.data.let {
                            _itemsListLiveData.postValue(it)
                        }
                    } else {
                        _itemsListLiveData.postValue(arrayListOf<Item>())
                    }
                }
                is ApiResult.ApiError -> {
                    _isError.postValue(result.exception?.message)
                    _isLoading.postValue(false)
                    _itemsListLiveData.postValue(arrayListOf<Item>())
                }
            }
        }
    }
}