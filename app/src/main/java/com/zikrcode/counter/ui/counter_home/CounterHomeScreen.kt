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
import com.zikrcode.counter.ui.common.composables.AppIconButton
import com.zikrcode.counter.ui.common.composables.AppScreenContent
import com.zikrcode.counter.ui.common.theme.CounterTheme
import com.zikrcode.counter.ui.counter_home.components.CircleButton
import com.zikrcode.counter.ui.counter_home.components.CounterActionButtons
import com.zikrcode.counter.ui.counter_home.components.NoCounterAvailable
import com.zikrcode.counter.ui.counter_settings.ChangeScreenVisibility

@Composable
fun CounterHomeScreen(
    onSettingsClick: () -> Unit,
    onListClick: () -> Unit,
    onEditClick: (Int) -> Unit,
    viewModel: CounterHomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.navTarget) {
        when (uiState.navTarget) {
            CounterHomeNavTarget.CounterSettings -> {
                onSettingsClick.invoke()
            }
            CounterHomeNavTarget.CounterList -> {
                onListClick.invoke()
            }
            CounterHomeNavTarget.EditCounter -> {
                uiState.counter?.id?.let {
                    onEditClick.invoke(it)
                }
            }
            CounterHomeNavTarget.Idle -> {
                // no-op
            }
        }
        viewModel.onEvent(CounterHomeEvent.NavigationHandled)
    }

    CounterHomeScreenContent(
        isLoading = uiState.isLoading,
        counter = uiState.counter,
        vibrateOnTap = uiState.vibrateOnTap,
        onEvent = viewModel::onEvent
    )

    // to make preview work it is placed outside CounterHomeScreenContent
    ChangeScreenVisibility(uiState.keepScreenOn)
}

@PreviewLightDark
@Composable
private fun CounterHomeScreenContentPreview() {
    CounterTheme {
        CounterHomeScreenContent(
            isLoading = false,
            counter = Counter.instance(),
            vibrateOnTap = false,
            onEvent = { }
        )
    }
}

@Composable
private fun CounterHomeScreenContent(
    isLoading: Boolean,
    counter: Counter?,
    vibrateOnTap: Boolean,
    onEvent: (CounterHomeEvent) -> Unit
) {
    AppScreenContent(
        loading = isLoading,
        title = counter?.counterName ?: stringResource(R.string.app_name),
        topBarStartIcon = {
            AppIconButton(
                onClick = {
                    onEvent.invoke(CounterHomeEvent.Settings)
                },
                icon = Icons.Outlined.Settings,
                iconDescription = stringResource(R.string.counter_settings)
            )
        },
        topBarEndIcon = {
            AppIconButton(
                onClick = {
                    onEvent.invoke(CounterHomeEvent.List)
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
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    CircleButton(
                        modifier = Modifier.align(Alignment.Center),
                        vibrate = vibrateOnTap,
                        currentValue = counter.counterSavedValue,
                        onClick = {
                            onEvent.invoke(CounterHomeEvent.Increment)
                        }
                    )
                }
                CounterActionButtons(
                    onResetClick = {
                        onEvent.invoke(CounterHomeEvent.Reset)
                    },
                    onDecrementClick = {
                        onEvent(CounterHomeEvent.Decrement)
                    },
                    onEditClick = {
                        onEvent.invoke(CounterHomeEvent.Edit)
                    }
                )
            }
        }
    }
}
