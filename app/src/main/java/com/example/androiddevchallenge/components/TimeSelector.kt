package com.example.androiddevchallenge.components

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androiddevchallenge.TimerViewModel
import java.util.concurrent.TimeUnit

@Composable
fun TimeSelector() {
    val context = LocalContext.current
    val viewModel: TimerViewModel = viewModel()
    var hour by remember { mutableStateOf(0L) }
    var minute by remember { mutableStateOf(5L) }
    var seconds by remember { mutableStateOf(0L) }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            modifier = Modifier
                .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f)
            ) {
                OutlinedTextField(
                    value = hour.toString(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = { Text(text = "Hours") },
                    onValueChange = { hour = it.toLongOrNull() ?: 0L })
            }
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f)
            ) {
                OutlinedTextField(
                    value = minute.toString(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = { Text(text = "Minutes") },
                    onValueChange = { minute = it.toLongOrNull() ?: 0L })
            }
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f)
            ) {
                OutlinedTextField(
                    value = seconds.toString(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = { Text(text = "Seconds") },
                    onValueChange = { seconds = it.toLongOrNull() ?: 0L })
            }

        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(modifier = Modifier.width(100.dp), onClick = {
            val time = TimeUnit.HOURS.toMillis(hour) + TimeUnit.MINUTES.toMillis(minute) + TimeUnit.SECONDS.toMillis(seconds)
            var message: String? = null
            if(hour > 24 || hour < 0){
               message = "Hour should be between 0 to 24"
            }else if(minute > 59 || minute < 0){
                message = "Minute should be between 0 to 59"
            }else if(seconds > 59 || seconds < 0){
                message = "Seconds should be between 0 to 59"
            }
            if(message != null || time < 1000L) Toast.makeText(
                context,
                message ?: "Time should be greater than 1 second",
                Toast.LENGTH_LONG
            ).show() else viewModel.startTimer(time)
        }) {
            Text("START")
        }
    }
}