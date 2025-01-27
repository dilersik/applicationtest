package com.example.myapplication.views.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.views.viewModel.LoginViewModel

@Composable
fun ResultScreen(viewModel: LoginViewModel) {
    val isLoginSuccessful by viewModel.isLoginSuccessful.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLoginSuccessful) {
            Text(stringResource(R.string.login_successful))
            ListScreen()

        } else {
            Text(stringResource(R.string.login_failed))
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                viewModel.resetToLogin()
            }) {
                Text(stringResource(R.string.back_to_login))
            }
        }
    }
}