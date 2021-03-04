/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge.components

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
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
                    onValueChange = { hour = it.toLongOrNull() ?: 0L }
                )
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
                    onValueChange = { minute = it.toLongOrNull() ?: 0L }
                )
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
                    onValueChange = { seconds = it.toLongOrNull() ?: 0L }
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            modifier = Modifier.width(100.dp),
            onClick = {
                val time = TimeUnit.HOURS.toMillis(hour) + TimeUnit.MINUTES.toMillis(minute) + TimeUnit.SECONDS.toMillis(seconds)
                var message: String? = null
                if (hour > 24 || hour < 0) {
                    message = "Hour should be between 0 to 24"
                } else if (minute > 59 || minute < 0) {
                    message = "Minute should be between 0 to 59"
                } else if (seconds > 59 || seconds < 0) {
                    message = "Seconds should be between 0 to 59"
                }
                if (message != null || time < 1000L) Toast.makeText(
                    context,
                    message ?: "Time should be greater than 1 second",
                    Toast.LENGTH_LONG
                ).show() else viewModel.startTimer(time)
            }
        ) {
            Text("START")
        }
    }
}
