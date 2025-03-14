package com.example.myapplication.views.ui

import android.hardware.SensorManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.myapplication.R
import com.example.myapplication.utils.SensorUUIDGenerator
import com.example.myapplication.views.viewModel.MainViewModel
import java.util.concurrent.Executor

@Composable
fun LoginScreen(viewModel: MainViewModel) {

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text(stringResource(R.string.user)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(R.string.password)) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            // viewModel.onLogin(Login(username, password))
        }) {
            Text(stringResource(R.string.login))
        }
    }
}

@Composable
fun BiometricAuthScreen(paddingValues: PaddingValues, onAuthenticated: (String) -> Unit) {
    val context = LocalContext.current
    val sensorManager = context.getSystemService(SensorManager::class.java)
    val uuidState = remember { mutableStateOf("") }

    val executor: Executor = ContextCompat.getMainExecutor(context)
    val biometricPrompt = BiometricPrompt(
        context as FragmentActivity,
        executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                onAuthenticated(uuidState.value)
            }
        })
    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Login Biométrico")
        .setSubtitle("Autentique-se para continuar")
        .setNegativeButtonText("Cancelar")
        .build()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = {
            SensorUUIDGenerator(sensorManager).generateUUID { uuid ->
                uuidState.value = uuid
                biometricPrompt.authenticate(promptInfo)
            }
        }) {
            Text("Autenticar com Biometria")
        }
    }
}
