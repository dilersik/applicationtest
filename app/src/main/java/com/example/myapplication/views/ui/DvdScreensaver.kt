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
import com.example.myapplication.views.Screen
import com.example.myapplication.views.viewModel.MainViewModel
import kotlinx.coroutines.delay
import java.util.Random

@Composable
fun MainScreenSaver(
    viewModel: MainViewModel,
    paddingValues: PaddingValues
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
                viewModel.goToScreen(Screen.ScreenSaver)
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

        DvdScreensaver(messages = viewModel.list.value?.map { it.title } ?: emptyList())
    }
}

@Composable
fun DvdScreensaver(messages: List<String>) {
    val positionX = remember { Animatable(100f) }
    val positionY = remember { Animatable(100f) }
    var directionX by remember { mutableFloatStateOf(1f) }
    val directionY by remember { mutableFloatStateOf(1f) }
    var color by remember { mutableStateOf(Color.Blue) }
    var currentMessageIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            positionX.animateTo(positionX.value + (5 * directionX), animationSpec = tween(50))
            positionY.animateTo(positionY.value + (5 * directionY), animationSpec = tween(50))

            if (positionX.value <= 0 || positionX.value >= 300f) {
                val triple = changeMessage(directionX, color, currentMessageIndex, messages)
                color = triple.first
                currentMessageIndex = triple.second
                directionX = triple.third
            }
            if (positionY.value <= 0 || positionY.value >= 500f) {
                val triple = changeMessage(directionX, color, currentMessageIndex, messages)
                color = triple.first
                currentMessageIndex = triple.second
                directionX = triple.third
            }
            delay(50L)
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .size(140.dp)
                .offset(x = positionX.value.dp, y = positionY.value.dp)
                .background(color, shape = RoundedCornerShape(8.dp))
                .clickable {
                    val triple = changeMessage(directionX, color, currentMessageIndex, messages)
                    color = triple.first
                    currentMessageIndex = triple.second
                    directionX = triple.third
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier.padding(10.dp),
                text = messages[currentMessageIndex], color = Color.White,
            )
        }
    }
}

private fun changeMessage(
    directionX: Float,
    color: Color,
    currentMessageIndex: Int,
    messages: List<String>
): Triple<Color, Int, Float> {
    var directionX1 = directionX
    var color1 = color
    var currentMessageIndex1 = currentMessageIndex
    directionX1 *= -1
    color1 = Color(Random().nextInt(256), Random().nextInt(256), Random().nextInt(256))
    currentMessageIndex1 = (currentMessageIndex1 + 1) % messages.size
    return Triple(color1, currentMessageIndex1, directionX1)
}
