package com.example.myapplication.views.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.MyList
import com.example.myapplication.model.ResultWrapper
import com.example.myapplication.repository.MyListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MyListRepository
): ViewModel() {

    private val _list: MutableStateFlow<List<MyList>?> = MutableStateFlow(null)
    val list = _list.asStateFlow()

    init {
        getList()
    }

    private fun getList() = viewModelScope.launch {
        repository.getList().let { result ->
            if (result is ResultWrapper.Success) {
                _list.value = result.value
            } else {
                // Handle error
            }
        }
    }

}