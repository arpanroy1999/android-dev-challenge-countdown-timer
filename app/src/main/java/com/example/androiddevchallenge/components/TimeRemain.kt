package com.example.androiddevchallenge.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androiddevchallenge.TimerViewModel
import java.util.concurrent.TimeUnit

@Composable
fun TimeRemain() {
    val viewModel: TimerViewModel = viewModel()
    val timeRemaining = viewModel.timeRemaining.observeAsState()
    val progress =
        ((timeRemaining.value!!.toFloat() / viewModel.timeStartFrom.toFloat()) * 100) / 100f
    val hour = TimeUnit.MILLISECONDS.toHours(timeRemaining.value!!)
    val minute =
        TimeUnit.MILLISECONDS.toMinutes(timeRemaining.value!! - TimeUnit.HOURS.toMillis(hour))
    val seconds = TimeUnit.MILLISECONDS.toSeconds(
        timeRemaining.value!! - (TimeUnit.HOURS.toMillis(hour) + TimeUnit.MINUTES.toMillis(minute))
    )
    var isPaused by rememberSaveable { mutableStateOf(false) }
    Column(
        Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center, modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            CircularProgressIndicator(
                progress = progress, modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
            )
            Text(buildAnnotatedString {
                append("${if (hour < 10) "0" else ""}$hour:${if (minute < 10) "0" else ""}${minute}:")
                withStyle(SpanStyle(Color.Red)) {
                    append(seconds.toString())
                }
            }, style = TextStyle(color = MaterialTheme.typography.h1.color, fontSize = 90.sp))
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Button(modifier = Modifier.weight(1f), onClick = { viewModel.cancelTimer() }) {
                Text(text = "CANCEL")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(modifier = Modifier.weight(1f), onClick = { isPaused = !isPaused; if(isPaused) viewModel.pauseTimer() else viewModel.resumeTimer() }) {
                Text(text = if(isPaused) "RESUME" else "PAUSE")
            }
        }
    }

}