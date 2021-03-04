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
package com.example.androiddevchallenge

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androiddevchallenge.components.TimeRemain
import com.example.androiddevchallenge.components.TimeSelector
import com.example.androiddevchallenge.ui.theme.MyTheme
import kotlinx.coroutines.*
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                val viewModel: TimerViewModel = viewModel()
                val state = viewModel.state.observeAsState()
                Box(
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight().padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    AnimatedVisibility(
                        visible = state.value == TimerViewModel.UiMode.TIMER,
                        enter = slideInVertically(initialOffsetY = { -40 }) + expandVertically(
                            expandFrom = Alignment.Top
                        )
                    ) {
                        TimeRemain()
                    }
                    if (state.value == TimerViewModel.UiMode.SELECT_TIME) {
                        TimeSelector()
                    }
                }
            }
        }
    }
}

class TimerViewModel : ViewModel() {
    private var mTimeRemaining = MutableLiveData(0L)
    val timeRemaining: LiveData<Long>
        get() = mTimeRemaining
    private var mState = MutableLiveData(UiMode.SELECT_TIME)
    val state: LiveData<Int>
        get() = mState
    var timeStartFrom: Long = 0L

    private var job: Job? = null

    @UiThread
    fun cancelTimer() {
        viewModelScope.launch {
            job?.cancelAndJoin()
            mState.value = UiMode.SELECT_TIME
            mTimeRemaining.value = 0L
        }
    }

    @UiThread
    fun pauseTimer() {
        job?.cancel()
    }

    @UiThread
    fun resumeTimer() {
        startInternal()
    }

    @UiThread
    fun startTimer(totalTime: Long) {
        mTimeRemaining.value = totalTime
        timeStartFrom = totalTime
        startInternal()
    }

    private fun startInternal() {
        viewModelScope.launch(Executors.newSingleThreadExecutor().asCoroutineDispatcher()) {
            job?.cancelAndJoin()
            job = launch {
                mState.postValue(UiMode.TIMER)
                var timeLeft = mTimeRemaining.value!!
                while (isActive && timeLeft > 0) {
                    timeLeft -= 1000L
                    mTimeRemaining.postValue(timeLeft)
                    delay(1000L)
                }
                if (isActive) mState.postValue(UiMode.SELECT_TIME)
            }
        }
    }

    object UiMode {
        const val SELECT_TIME = 0
        const val TIMER = 1
    }
}
