package com.example.myapplication.views.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.myapplication.R
import com.example.myapplication.views.ValidationState
import com.example.myapplication.views.viewModel.LoginViewModel

@Composable
fun ValidationScreen(viewModel: LoginViewModel) {
    val validationState by viewModel.validationState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.validateLogin()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (validationState) {
            ValidationState.LOADING -> CircularProgressIndicator()
            ValidationState.SUCCESS -> Text(stringResource(R.string.validating))
            ValidationState.FAILURE -> Text(stringResource(R.string.failed))
        }
    }
}
