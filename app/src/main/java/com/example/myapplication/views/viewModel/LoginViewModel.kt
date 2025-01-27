package com.example.myapplication.views.viewModel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.myapplication.MyApplication
import com.example.myapplication.model.Login
import com.example.myapplication.utils.EncryptionUtils
import com.example.myapplication.views.Screen
import com.example.myapplication.views.ValidationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.crypto.KeyGenerator

import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val encryptionUtils: EncryptionUtils
): ViewModel() {

    private val _currentScreen = MutableStateFlow(Screen.Login)
    val currentScreen: StateFlow<Screen> = _currentScreen

    private val _validationState = MutableStateFlow(ValidationState.LOADING)
    val validationState: StateFlow<ValidationState> = _validationState

    private val _isLoginSuccessful = MutableStateFlow(false)
    val isLoginSuccessful: StateFlow<Boolean> = _isLoginSuccessful

    private val sharedPreferences: SharedPreferences by lazy {
        EncryptedSharedPreferences.create(
            "login_prefs",
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            MyApplication.instance.applicationContext,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    private val encryptionKey = KeyGenerator.getInstance("AES").apply { init(256) }.generateKey().encoded
    private var encryptedLogin = ""

    fun onLogin(login: Login) {
        encryptedLogin = encryptionUtils.encryptObject(login, encryptionKey, Login.serializer())
        _currentScreen.value = Screen.Validation
    }

    fun validateLogin() {
        viewModelScope.launch {
            val decryptedLogin = encryptionUtils.decryptObject(encryptedLogin, encryptionKey, Login.serializer())

            if (decryptedLogin.username == "Teste01" && decryptedLogin.password == "Mercantil") {
                _isLoginSuccessful.value = true
                saveLog("Login bem-sucedido: Usuário=${decryptedLogin.username}")
            } else {
                _isLoginSuccessful.value = false
                saveLog("Falha no login: Usuário=${decryptedLogin.username}")
            }

            _currentScreen.value = Screen.Result
        }
    }

    fun resetToLogin() {
        _currentScreen.value = Screen.Login
    }

    private fun saveLog(message: String) {
        val currentLogs = sharedPreferences.getString("logs", "") ?: ""
        val updatedLogs = "$currentLogs\n$message"
        sharedPreferences.edit().putString("logs", updatedLogs).apply()
    }

}