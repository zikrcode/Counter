/*
 * Copyright (C) 2023 Zokirjon Mamadjonov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zikrcode.counter.ui.counter_home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.zikrcode.counter.R
import com.zikrcode.counter.domain.model.Counter
import com.zikrcode.counter.ui.counter_home.components.CircleButton
import com.zikrcode.counter.ui.counter_home.components.CounterItem
import com.zikrcode.counter.ui.counter_home.components.DecrementButton
import com.zikrcode.counter.ui.counter_home.components.NoCounterAvailable
import com.zikrcode.counter.ui.counter_settings.ChangeScreenVisibility
import com.zikrcode.counter.ui.utils.Dimens
import com.zikrcode.counter.ui.utils.navigation.Screen
import com.zikrcode.counter.ui.utils.navigation.MainNavigationArgs
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CounterHomeScreen(
    navController: NavController,
    viewModel: CounterHomeViewModel = hiltViewModel()
) {
    var noCounter by rememberSaveable {
        mutableStateOf(false)
    }
    val editCounterTitle = stringResource(R.string.edit_counter)

    LaunchedEffect(true) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                CounterHomeViewModel.UiEvent.NoCounter -> {
                    noCounter = true
                }
                is CounterHomeViewModel.UiEvent.EditCounter -> {
                    navController.navigate(
                        Screen.AddEditCounterScreen.route +
                                "?${MainNavigationArgs.TITLE_ARG}=${editCounterTitle}&${MainNavigationArgs.COUNTER_ID_ARG}=${event.counter.id}"
                    )
                }
            }
        }
    }

    if (noCounter) {
        NoCounterAvailable()
    }

    viewModel.counter.value?.let {
        CounterHomeContent(
            counter = it,
            vibrate = viewModel.vibrateOnTap.value,
            onEventClick = viewModel::onEvent
        )
    }

    ChangeScreenVisibility(viewModel.keepScreenOn.value)
}

@Preview(
    showSystemUi = true,
    showBackground = true,
    device = Devices.PHONE
)
@Composable
fun CounterHomeContentPreview() {
    CounterHomeContent(
        counter = Counter.instance(),
        vibrate = false,
        onEventClick = { }
    )
}

@Composable
private fun CounterHomeContent(
    counter: Counter,
    vibrate: Boolean,
    onEventClick: (CounterHomeEvent) -> Unit
) {
    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimens.SpacingSingle),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CounterItem(
                counter = counter,
                onEditClick = {
                    onEventClick(CounterHomeEvent.Edit(counter))
                },
                onResetClick = {
                    onEventClick(CounterHomeEvent.Reset)
                }
            )
            Spacer(Modifier.height(Dimens.SpacingSingle))
            CircleButton(
                modifier = Modifier.weight(1f),
                vibrate = vibrate,
                currentValue = counter.counterSavedValue
            ) {
                onEventClick(CounterHomeEvent.Increment)
            }
            Spacer(Modifier.height(Dimens.SpacingSingle))
            DecrementButton {
                onEventClick(CounterHomeEvent.Decrement)
            }
        }
    }
}