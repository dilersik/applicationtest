package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.views.Screen
import com.example.myapplication.views.ui.ListScreen
import com.example.myapplication.views.ui.LoginScreen
import com.example.myapplication.views.ui.MainScreenSaver
import com.example.myapplication.views.ui.PopOverDateTimePicker
import com.example.myapplication.views.ui.ValidationScreen
import com.example.myapplication.views.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppContent(this)
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppContent(context: Context) {
    val viewModel: MainViewModel = viewModel()
    val currentScreen by viewModel.currentScreen.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My App") }
            )
        }
    ) { paddingValues ->

        when (currentScreen) {
            is Screen.Login -> LoginScreen (paddingValues) {
                viewModel.onLogin(it)
            }

            is Screen.Validation -> ValidationScreen(viewModel)

            is Screen.Posts -> ListScreen(viewModel, paddingValues)

            is Screen.ScreenSaver -> MainScreenSaver(viewModel, paddingValues)

            is Screen.DateTimePicker -> PopOverDateTimePicker(
                showDatePicker = true,
                showTimePicker = true,
                initialDate = "2025-03-14",
                initialTime = "12:00",
                onDateTimeSelected = { selectedDateTime ->
                    run {
                        Toast.makeText(
                            context,
                            "Selected date time: $selectedDateTime",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                },
                onDismissRequest = { viewModel.goToScreen(Screen.ScreenSaver) }
            )

        }
    }
}
