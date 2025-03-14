package com.example.myapplication.views.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.myapplication.model.Post
import com.example.myapplication.views.Screen
import com.example.myapplication.views.viewModel.MainViewModel
import kotlinx.coroutines.delay
import java.util.Random

@Composable
fun MainScreenSaver(
    viewModel: MainViewModel,
    paddingValues: PaddingValues,
    messages: List<Post>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TextButton(onClick = {
                viewModel.goToScreen(Screen.ScreenSaver(messages))
            }) {
                Text("Dvd Screen Saver")
            }

            TextButton(onClick = {
                viewModel.goToScreen(Screen.DateTimePicker)
            }) {
                Text("Data/Hora Picker")
            }

            TextButton(onClick = {
                viewModel.logout()
            }) {
                Text("Logout")
            }
        }

        DvdScreensaver(messages = messages.map { it.title }, onMessageClick = {})
    }
}

@Composable
fun DvdScreensaver(messages: List<String>, onMessageClick: () -> Unit) {
    val positionX = remember { Animatable(100f) }
    val positionY = remember { Animatable(100f) }
    var directionX by remember { mutableFloatStateOf(1f) }
    var directionY by remember { mutableFloatStateOf(1f) }
    var color by remember { mutableStateOf(Color.Blue) }
    var currentMessageIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            positionX.animateTo(positionX.value + (5 * directionX), animationSpec = tween(50))
            positionY.animateTo(positionY.value + (5 * directionY), animationSpec = tween(50))

            if (positionX.value <= 0 || positionX.value >= 300f) {
                directionX *= -1
                color = Color(Random().nextInt(256), Random().nextInt(256), Random().nextInt(256))
                currentMessageIndex = (currentMessageIndex + 1) % messages.size
            }
            if (positionY.value <= 0 || positionY.value >= 500f) {
                directionY *= -1
                color = Color(Random().nextInt(256), Random().nextInt(256), Random().nextInt(256))
                currentMessageIndex = (currentMessageIndex + 1) % messages.size
            }
            delay(50L)
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .offset(x = positionX.value.dp, y = positionY.value.dp)
                .background(color, shape = RoundedCornerShape(8.dp))
                .clickable { onMessageClick() },
            contentAlignment = Alignment.Center
        ) {
            Text(text = messages[currentMessageIndex], color = Color.White)
        }
    }
}
