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
import androidx.compose.ui.platform.LocalContext
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
    val boxSize = 140f
    val velocity = 5f
    val positionX = remember { Animatable(100f) }
    val positionY = remember { Animatable(100f) }
    var directionX by remember { mutableFloatStateOf(1f) }
    var directionY by remember { mutableFloatStateOf(1f) }
    var color by remember { mutableStateOf(Color.Blue) }
    var currentMessageIndex by remember { mutableIntStateOf(0) }
    val context = LocalContext.current
    val displayMetrics = context.resources.displayMetrics
    val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
    val screenHeightDp = displayMetrics.heightPixels / displayMetrics.density

    LaunchedEffect(Unit) {
        while (true) {
            positionX.animateTo(positionX.value + (velocity * directionX), animationSpec = tween(50))
            positionY.animateTo(positionY.value + (velocity * directionY), animationSpec = tween(50))

            if (positionX.value <= 0 || positionX.value + boxSize >= screenWidthDp) {
                val result = changeMessage(directionX, directionY, currentMessageIndex, messages)
                color = result.first
                currentMessageIndex = result.second
                directionX = result.third
            }
            if (positionY.value <= 0 || positionY.value + boxSize >= screenHeightDp) {
                val result = changeMessage(directionX, directionY, currentMessageIndex, messages)
                color = result.first
                currentMessageIndex = result.second
                directionY = result.fourth
            }
            delay(50L)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .size(boxSize.dp)
                .offset(x = positionX.value.dp, y = positionY.value.dp)
                .background(color, shape = RoundedCornerShape(8.dp))
                .clickable {
                    val result = changeMessage(
                        directionX,
                        directionY,
                        currentMessageIndex,
                        messages
                    )
                    color = result.first
                    currentMessageIndex = result.second
                    directionX = result.third
                    directionY = result.fourth
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
    directionY: Float,
    currentMessageIndex: Int,
    messages: List<String>
): Quadruple<Color, Int, Float, Float> {
    val newDirectionX = directionX * -1
    val newDirectionY = directionY * -1
    val newColor = Color(Random().nextInt(256), Random().nextInt(256), Random().nextInt(256))
    val newIndex = (currentMessageIndex + 1) % messages.size
    return Quadruple(newColor, newIndex, newDirectionX, newDirectionY)
}

private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

