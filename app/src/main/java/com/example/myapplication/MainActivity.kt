package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.views.Screen
import com.example.myapplication.views.ui.LoginScreen
import com.example.myapplication.views.ui.ResultScreen
import com.example.myapplication.views.ui.ValidationScreen
import com.example.myapplication.views.viewModel.LoginViewModel
import com.example.myapplication.views.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppContent()
        }
    }

}

@Composable
fun AppContent() {
    val viewModel: LoginViewModel = viewModel()
    val currentScreen by viewModel.currentScreen.collectAsState()

    when (currentScreen) {
        Screen.Login -> LoginScreen(viewModel)
        Screen.Validation -> ValidationScreen(viewModel)
        Screen.Result -> ResultScreen(viewModel)
    }
}
