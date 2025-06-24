package com.example.myapplication.views.viewModel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.Post
import com.example.myapplication.model.ResultWrapper
import com.example.myapplication.repository.PostRepository
import com.example.myapplication.views.Screen
import com.example.myapplication.views.ValidationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: PostRepository,
    private val sharedPreferences: SharedPreferences
): ViewModel() {

    private val _validationState = MutableStateFlow(ValidationState.LOADING)
    val validationState: StateFlow<ValidationState> = _validationState

    private val _currentScreen = MutableStateFlow<Screen>(Screen.Posts)
    val currentScreen: StateFlow<Screen> = _currentScreen

    private val _list: MutableStateFlow<List<Post>?> = MutableStateFlow(null)
    val list = _list.asStateFlow()

    private val _isMock = MutableStateFlow(true)
    val isMock = _isMock.asStateFlow()

    fun getList() = viewModelScope.launch {
        repository.getList(_isMock.value).let { result ->
            if (result is ResultWrapper.Success) {
                _list.value = result.value
            } else {
                // Handle error
            }
        }
    }

    fun goToScreen(view: Screen) {
        _currentScreen.value = view
    }

}