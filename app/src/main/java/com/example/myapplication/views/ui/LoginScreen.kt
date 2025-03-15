package com.example.myapplication.views.ui

import android.hardware.SensorManager
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.myapplication.utils.SensorUUIDGenerator
import java.util.concurrent.Executor

@Composable
fun LoginScreen(paddingValues: PaddingValues, onAuthenticated: (String) -> Unit) {
    val context = LocalContext.current
    val sensorManager = context.getSystemService(SensorManager::class.java)
    val biometricManager = BiometricManager.from(context)
    val executor: Executor = ContextCompat.getMainExecutor(context)
    val uuidState = remember { mutableStateOf("") }

    val biometricPrompt =
        BiometricPrompt(context as FragmentActivity, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onAuthenticated(uuidState.value)
                Toast.makeText(context, "Autenticação bem-sucedida!", Toast.LENGTH_SHORT).show()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(context, "Erro: $errString", Toast.LENGTH_SHORT).show()
            }
        })

    val fingerprintPromptInfo = remember {
        BiometricPrompt.PromptInfo.Builder()
            .setTitle("Autenticação com Digital")
            .setSubtitle("Use sua digital para autenticar")
            .setNegativeButtonText("Cancelar")
            .build()
    }

    val facialRecognitionPromptInfo = remember {
        BiometricPrompt.PromptInfo.Builder()
            .setTitle("Autenticação Facial")
            .setSubtitle("Use o reconhecimento facial para autenticar")
            .setNegativeButtonText("Cancelar")
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
            .build()
    }

    val deviceCredentialPromptInfo = remember {
        BiometricPrompt.PromptInfo.Builder()
            .setTitle("Autenticação com Senha do Dispositivo")
            .setSubtitle("Use PIN, senha ou padrão para autenticar")
            .setAllowedAuthenticators(BiometricManager.Authenticators.DEVICE_CREDENTIAL)
            .build()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            if (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) ==
                BiometricManager.BIOMETRIC_SUCCESS) {

                SensorUUIDGenerator(sensorManager).generateUUID { uuid ->
                    uuidState.value = uuid
                    biometricPrompt.authenticate(fingerprintPromptInfo)
                }
            } else {
                Toast.makeText(context, "Leitor de digital não suportado", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Login com Digital")
        }

        Spacer(modifier = Modifier.height(16.dp))

//        Button(onClick = {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
//                biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) ==
//                BiometricManager.BIOMETRIC_SUCCESS) {
//
//                SensorUUIDGenerator(sensorManager).generateUUID { uuid ->
//                    uuidState.value = uuid
//                    biometricPrompt.authenticate(facialRecognitionPromptInfo)
//                }
//            } else {
//                Toast.makeText(context, "Reconhecimento facial não suportado", Toast.LENGTH_SHORT).show()
//            }
//        }) {
//            Text("Login com Reconhecimento Facial")
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (biometricManager.canAuthenticate(BiometricManager.Authenticators.DEVICE_CREDENTIAL) ==
                BiometricManager.BIOMETRIC_SUCCESS) {

                SensorUUIDGenerator(sensorManager).generateUUID { uuid ->
                    uuidState.value = uuid
                    biometricPrompt.authenticate(deviceCredentialPromptInfo)
                }
            } else {
                Toast.makeText(context, "Senha do dispositivo não suportada", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Login com Senha do Dispositivo")
        }
    }
}
