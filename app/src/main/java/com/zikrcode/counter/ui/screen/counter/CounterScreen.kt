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

package com.zikrcode.counter.ui.screen.counter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.FeaturedPlayList
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zikrcode.counter.R
import com.zikrcode.counter.domain.model.Counter
import com.zikrcode.counter.ui.composables.AppIconButton
import com.zikrcode.counter.ui.composables.AppScreenContent
import com.zikrcode.counter.ui.theme.CounterTheme
import com.zikrcode.counter.ui.screen.counter.components.CircleButton
import com.zikrcode.counter.ui.screen.counter.components.ActionButtons
import com.zikrcode.counter.ui.screen.counter.components.NoCounterAvailable
import com.zikrcode.counter.ui.screen.settings.ChangeScreenVisibility

@Composable
fun CounterScreen(
    onNavigateToSettings: () -> Unit,
    onNavigateToCounterList: () -> Unit,
    onNavigateToCounterEditor: (Int) -> Unit,
    viewModel: CounterViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.navTarget) {
        when (uiState.navTarget) {
            CounterNavTarget.Settings -> {
                onNavigateToSettings.invoke()
            }
            CounterNavTarget.CounterList -> {
                onNavigateToCounterList.invoke()
            }
            CounterNavTarget.CounterEditor -> {
                uiState.counter?.id?.let {
                    onNavigateToCounterEditor.invoke(it)
                }
            }
            CounterNavTarget.Idle -> {
                // no-op
            }
        }
        viewModel.onEvent(CounterEvent.NavigationHandled)
    }

    CounterScreenContent(
        isLoading = uiState.isLoading,
        counter = uiState.counter,
        vibrateOnTap = uiState.vibrateOnTap,
        onEvent = viewModel::onEvent
    )

    // to make preview work it is placed outside CounterScreenContent
    ChangeScreenVisibility(uiState.keepScreenOn)
}

@PreviewLightDark
@Composable
private fun CounterScreenContentPreview() {
    CounterTheme {
        CounterScreenContent(
            isLoading = false,
            counter = Counter.instance(),
            vibrateOnTap = false,
            onEvent = { }
        )
    }
}

@Composable
private fun CounterScreenContent(
    isLoading: Boolean,
    counter: Counter?,
    vibrateOnTap: Boolean,
    onEvent: (CounterEvent) -> Unit
) {
    AppScreenContent(
        loading = isLoading,
        title = counter?.counterName ?: stringResource(R.string.counter),
        topBarStartIcon = {
            AppIconButton(
                onClick = {
                    onEvent.invoke(CounterEvent.Settings)
                },
                icon = Icons.Outlined.Settings,
                iconDescription = stringResource(R.string.settings)
            )
        },
        topBarEndIcon = {
            AppIconButton(
                onClick = {
                    onEvent.invoke(CounterEvent.Counters)
                },
                icon = Icons.AutoMirrored.Outlined.FeaturedPlayList,
                iconDescription = stringResource(R.string.counter_list)
            )
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (counter == null) {
                NoCounterAvailable()
            } else {
                Box(modifier = Modifier.weight(1f)) {
                    Text(
                        text = counter.counterDescription,
                        color = CounterTheme.colorScheme.text,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 4,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    CircleButton(
                        modifier = Modifier.align(Alignment.Center),
                        vibrate = vibrateOnTap,
                        currentValue = counter.counterSavedValue,
                        onClick = {
                            onEvent.invoke(CounterEvent.Increment)
                        }
                    )
                }
                ActionButtons(
                    onResetClick = {
                        onEvent.invoke(CounterEvent.Reset)
                    },
                    onDecrementClick = {
                        onEvent(CounterEvent.Decrement)
                    },
                    onEditClick = {
                        onEvent.invoke(CounterEvent.Edit)
                    }
                )
            }
        }
    }
}
