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

package com.zikrcode.counter.ui.counter_editor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.RestorePage
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zikrcode.counter.R
import com.zikrcode.counter.ui.common.composables.AppIconButton
import com.zikrcode.counter.ui.common.composables.AppScreenContent
import com.zikrcode.counter.ui.common.theme.CounterTheme
import com.zikrcode.counter.ui.counter_editor.component.CounterEditorForm
import com.zikrcode.counter.ui.utils.Dimens
import com.zikrcode.counter.ui.utils.UiText

@Composable
fun CounterEditorScreen(
    onNavigateBack: () -> Unit,
    viewModel: CounterEditorViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.navTarget) {
        when (uiState.navTarget) {
            CounterEditorNavTarget.NavigateBack -> {
                onNavigateBack.invoke()
            }
            CounterEditorNavTarget.Idle -> {
                // no-op
            }
        }
    }

    CounterEditorScreenContent(
        isLoading = uiState.isLoading,
        counterId = uiState.counterId,
        counterName = uiState.counterName,
        counterValue = uiState.counterValue,
        counterDescription = uiState.counterDescription,
        message = uiState.message,
        onEvent = viewModel::onEvent
    )
}

@PreviewLightDark
@Composable
private fun CounterEditorScreenContentPreview() {
    CounterTheme {
        CounterEditorScreenContent(
            isLoading = false,
            counterId = null,
            counterName = "",
            counterValue = 0,
            counterDescription = "",
            message = null,
            onEvent = { }
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun CounterEditorScreenContent(
    isLoading: Boolean,
    counterId: Int?,
    counterName: String,
    counterValue: Int,
    counterDescription: String,
    message: UiText?,
    onEvent: (CounterEditorEvent) -> Unit
) {
    AppScreenContent(
        title = if (counterId == null) {
            stringResource(R.string.new_counter)
        } else {
            stringResource(R.string.counter)
        },
        topBarStartIcon = {
            AppIconButton(
                onClick = {
                    onEvent.invoke(CounterEditorEvent.GoBack)
                },
                icon = Icons.AutoMirrored.Outlined.ArrowBack,
                iconDescription = stringResource(R.string.go_back)
            )
        },
        topBarEndIcon = {
            AppIconButton(
                onClick = {
                    onEvent.invoke(CounterEditorEvent.RestoreCounter)
                },
                icon = Icons.Outlined.RestorePage,
                iconDescription = stringResource(R.string.restore_counter)
            )
        },
        loading = isLoading,
        snackbarMessage = message?.asString(),
        onSnackbarShown = {
            onEvent.invoke(CounterEditorEvent.MessageShown)
        }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            CounterEditorForm(
                counterName = counterName,
                onCounterNameChange = { counterName ->
                    onEvent.invoke(CounterEditorEvent.NameChanged(counterName))
                },
                counterDescription = counterDescription,
                onCounterDescriptionChange = { counterDescription ->
                    onEvent.invoke(CounterEditorEvent.DescriptionChanged(counterDescription))
                },
                counterValue = counterValue,
                onCounterValueChange = { counterValue ->
                    onEvent.invoke(CounterEditorEvent.ValueChanged(counterValue))
                }
            )
            Spacer(Modifier.weight(1f))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .consumeWindowInsets(WindowInsets.navigationBars)
                    .imePadding(),
                horizontalArrangement = Arrangement.spacedBy(
                    space = Dimens.SpacingSingle,
                    alignment = Alignment.End
                )
            ) {
                OutlinedButton(
                    onClick = {
                        onEvent.invoke(CounterEditorEvent.Cancel)
                    },
                    shape = ButtonDefaults.squareShape
                ) {
                    Text(text = stringResource(R.string.cancel))
                }
                Button(
                    onClick = {
                        onEvent.invoke(CounterEditorEvent.Save)
                    },
                    shape = ButtonDefaults.squareShape
                ) {
                    Text(text = stringResource(R.string.save))
                }
            }
        }
    }
}
