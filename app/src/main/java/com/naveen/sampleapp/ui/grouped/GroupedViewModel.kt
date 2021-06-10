package com.naveen.sampleapp.ui.grouped

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

class GroupedViewModel : ViewModel() {

    private val _itemsListLiveData = MutableLiveData<List<GroupedItem>>()
    private val _isLoading = MutableLiveData<Boolean>()
    private val _isError = MutableLiveData<String>()
    val itemsListLiveData: LiveData<List<GroupedItem>> = _itemsListLiveData
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
                    if (result.data != null && result.data.isNotEmpty()) {
                        result.data.let {
                            val groupingItemsByListId = it.groupBy { it1 -> it1.listId }
                            val groupedList = mutableListOf<GroupedItem>()
                            for ((key, value) in groupingItemsByListId) {
                                groupedList.add(GroupedItem(1/*1 for title*/, key, null))
                                value.forEach { item ->
                                    groupedList.add(GroupedItem(2/*2 for item*/, null, item))
                                }
                            }
                            _isLoading.postValue(false)
                            _itemsListLiveData.postValue(groupedList)
                        }
                    } else {
                        _isLoading.postValue(false)
                        _itemsListLiveData.postValue(arrayListOf<GroupedItem>())
                    }
                }
                is ApiResult.ApiError -> {
                    _isError.postValue(result.exception?.message)
                    _isLoading.postValue(false)
                    _itemsListLiveData.postValue(arrayListOf<GroupedItem>())
                }
            }
        }
    }
}