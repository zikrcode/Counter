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

package com.zikrcode.counter.ui.counter_settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Brightness5
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Vibration
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zikrcode.counter.R
import com.zikrcode.counter.ui.common.composables.AppIconButton
import com.zikrcode.counter.ui.common.composables.AppScreenContent
import com.zikrcode.counter.ui.common.theme.CounterTheme
import com.zikrcode.counter.ui.counter_settings.components.PreferenceItem
import com.zikrcode.counter.ui.utils.Dimens

@Composable
fun CounterSettingsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToAbout: () -> Unit,
    viewModel: CounterSettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.navTarget) {
        when (uiState.navTarget) {
            CounterSettingsNavTarget.NavigateBack -> {
                onNavigateBack.invoke()
            }
            CounterSettingsNavTarget.About -> {
                onNavigateToAbout.invoke()
            }
            CounterSettingsNavTarget.Idle -> {
                // no-op
            }
        }
        viewModel.onEvent(CounterSettingsEvent.NavigationHandled)
    }

    CounterSettingsScreenContent(
        isLoading = uiState.isLoading,
        vibrateOnTap = uiState.vibrateOnTap,
        keepScreenOn = uiState.keepScreenOn,
        onEvent = viewModel::onEvent
    )

    // to make preview work it is placed outside CounterSettingsScreenContent
    ChangeScreenVisibility(uiState.keepScreenOn)
}

@PreviewLightDark
@Composable
fun CounterSettingsContentPreview() {
    CounterTheme {
        CounterSettingsScreenContent(
            isLoading = false,
            vibrateOnTap = false,
            keepScreenOn = false,
            onEvent = { }
        )
    }
}

@Composable
private fun CounterSettingsScreenContent(
    isLoading: Boolean,
    vibrateOnTap: Boolean,
    keepScreenOn: Boolean,
    onEvent: (CounterSettingsEvent) -> Unit
) {
    AppScreenContent(
        title = stringResource(R.string.counter_settings),
        topBarStartIcon = {
            AppIconButton(
                onClick = {
                    onEvent.invoke(CounterSettingsEvent.GoBack)
                },
                icon = Icons.AutoMirrored.Outlined.ArrowBack,
                iconDescription = stringResource(R.string.go_back)
            )
        },
        topBarEndIcon = {
            AppIconButton(
                onClick = {
                    onEvent.invoke(CounterSettingsEvent.About)
                },
                icon = Icons.Outlined.Info,
                iconDescription = stringResource(R.string.about)
            )
        },
        loading = isLoading
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            ElevatedCard{
                Column(
                    modifier = Modifier.padding(Dimens.SpacingDouble),
                    verticalArrangement = Arrangement.spacedBy(Dimens.SpacingSingle),
                ) {
                    PreferenceItem(
                        icon = Icons.Outlined.Vibration,
                        name = stringResource(R.string.vibrate_on_tap_name),
                        description = stringResource(R.string.vibrate_on_tap_description),
                        checked = vibrateOnTap
                    ) {
                        onEvent.invoke(CounterSettingsEvent.VibrateOnTapPreferenceChanged)
                    }
                    HorizontalDivider()
                    PreferenceItem(
                        icon = Icons.Outlined.Brightness5,
                        name = stringResource(R.string.keep_screen_on_name),
                        description = stringResource(R.string.keep_screen_on_description),
                        checked = keepScreenOn
                    ) {
                        onEvent.invoke(CounterSettingsEvent.KeepScreenOnPreferenceChanged)
                    }
                }
            }
        }
    }
}
