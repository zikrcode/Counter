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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zikrcode.counter.domain.model.Counter
import com.zikrcode.counter.ui.counter_home.components.CircleButton
import com.zikrcode.counter.ui.counter_home.components.CounterItem
import com.zikrcode.counter.ui.counter_home.components.DecrementButton
import com.zikrcode.counter.ui.counter_home.components.NoCounterAvailable
import com.zikrcode.counter.ui.counter_settings.ChangeScreenVisibility
import com.zikrcode.counter.ui.utils.Dimens

@Composable
fun CounterHomeScreen(
    onCounterEdit: (Int) -> Unit,
    viewModel: CounterHomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CounterHomeContent(
        loading = uiState.isLoading,
        counter = uiState.counter,
        vibrate = uiState.vibrateOnTap,
        onCounterEdit = onCounterEdit,
        onEventClick = viewModel::onEvent
    )

    ChangeScreenVisibility(uiState.keepScreenOn)
}

@Preview(
    showSystemUi = true,
    showBackground = true,
    device = Devices.PHONE
)
@Composable
fun CounterHomeContentPreview() {
    CounterHomeContent(
        loading = false,
        counter = Counter.instance(),
        vibrate = false,
        onCounterEdit = { },
        onEventClick = { }
    )
}

@Composable
private fun CounterHomeContent(
    loading: Boolean,
    counter: Counter?,
    vibrate: Boolean,
    onCounterEdit: (Int) -> Unit,
    onEventClick: (CounterHomeEvent) -> Unit
) {
    if (loading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.width(Dimens.SpacingQuadruple),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        }
    } else {
        if (counter == null) {
            NoCounterAvailable()
        } else {
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
                        onEditClick = { onCounterEdit(counter.id!!) },
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
    }
}