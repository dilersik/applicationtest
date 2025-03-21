package com.example.myapplication.views.viewModel

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.Post
import com.example.myapplication.model.ResultWrapper
import com.example.myapplication.repository.PostRepository
import com.example.myapplication.views.Screen
import com.example.myapplication.views.ValidationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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

    private val _currentScreen = MutableStateFlow<Screen>(Screen.Validation)
    val currentScreen: StateFlow<Screen> = _currentScreen

    private val _list: MutableStateFlow<List<Post>?> = MutableStateFlow(null)
    val list = _list.asStateFlow()

    private val _isMock = MutableStateFlow(false)
    val isMock = _isMock.asStateFlow()

    init {
        viewModelScope.launch {
            getList()
        }
    }

    private suspend fun getList() {
        repository.getList(_isMock.value).let { result ->
            if (result is ResultWrapper.Success) {
                _list.value = result.value
            } else {
                // Handle error
            }
        }
    }

    fun onLogin(token: String) {
        sharedPreferences.edit {
            putString("token", token)
        }
        _currentScreen.value = Screen.Validation
    }

    fun validateLogin() = viewModelScope.launch {
        _currentScreen.value = Screen.Validation
        _validationState.value = ValidationState.LOADING

        val token = sharedPreferences.getString("token", null)
        val isLoginSuccessful = token != null

        if (isLoginSuccessful) {
            getList()
            delay(2L)
            _currentScreen.value = Screen.ScreenSaver
        } else {
            _currentScreen.value = Screen.Login
        }
    }

    fun goToScreen(view: Screen) {
        _currentScreen.value = view
    }

    fun logout() {
        sharedPreferences.edit {
            remove("token")
        }
        _currentScreen.value = Screen.Login
    }

    fun setIsMock(isMock: Boolean) {
        _isMock.value = isMock
        viewModelScope.launch {
            getList()
        }
    }

}