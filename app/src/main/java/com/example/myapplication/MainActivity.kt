package com.example.myapplication

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.views.Screen
import com.example.myapplication.views.ui.DetailsScreen
import com.example.myapplication.views.ui.ListScreen
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
    val activity = context as Activity
    val viewModel: MainViewModel = viewModel()
    viewModel.getList()
    val currentScreen by viewModel.currentScreen.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.my_app)) }
            )
        }
    ) { paddingValues ->

        when (currentScreen) {
            is Screen.Posts -> ListScreen(viewModel, paddingValues)

            is Screen.Details -> DetailsScreen(
                (currentScreen as Screen.Details).post,
                viewModel,
                paddingValues
            )
        }
    }

    BackHandler(enabled = true) {
        if (currentScreen is Screen.Details) {
            viewModel.goToScreen(Screen.Posts)
        } else {
            // Exit the app or perform default back behavior
            activity.finish()
        }
    }
}
