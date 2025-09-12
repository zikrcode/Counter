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

package com.zikrcode.counter.ui.add_edit_counter

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zikrcode.counter.R
import com.zikrcode.counter.ui.add_edit_counter.component.AddEditCounterForm
import com.zikrcode.counter.ui.utils.Dimens

@Composable
fun AddEditCounterScreen(
    onBack: () -> Unit,
    editCounter: Boolean,
    viewModel: AddEditCounterViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    uiState.apply {
        val context = LocalContext.current
        userMessage?.let { uiText ->
            LaunchedEffect(uiText) {
                snackbarHostState.showSnackbar(message = uiText.asString(context))
            }
        }
        LaunchedEffect(isCounterCanceled, isCounterSaved) {
            if (isCounterCanceled || isCounterSaved) {
                onBack()
            }
        }
    }

    AddEditCounterContent(
        loading = uiState.isLoading,
        snackbarHostState = snackbarHostState,
        onBack = onBack,
        editCounter = editCounter,
        counterName = uiState.counterName,
        counterDescription = uiState.counterDescription,
        counterValue = uiState.counterValue,
        onEvent = viewModel::onEvent
    )
}

@Preview(
    showSystemUi = true,
    showBackground = true,
    device = Devices.PHONE
)
@Composable
fun AddEditCounterContentPreview() {
    AddEditCounterContent(
        loading = false,
        snackbarHostState = SnackbarHostState(),
        onBack = { },
        editCounter = false,
        counterName = "",
        counterDescription = "",
        counterValue = null,
        onEvent = { }
    )
}

@Composable
private fun AddEditCounterContent(
    loading: Boolean,
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
    editCounter: Boolean,
    counterName: String,
    counterValue: Int?,
    counterDescription: String,
    onEvent: (AddEditCounterEvent) -> Unit
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
        Scaffold(
            modifier = Modifier.consumeWindowInsets(WindowInsets.systemBars),
            topBar = {
                AddEditCounterTopAppBar(
                    onBack = onBack,
                    title = stringResource(
                        if (editCounter) R.string.edit_counter else R.string.new_counter
                    ),
                    onCancel = {
                        onEvent(AddEditCounterEvent.Cancel)
                    },
                    onSave = {
                        onEvent(AddEditCounterEvent.Save)
                    }
                )
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            contentWindowInsets = WindowInsets.systemBars
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(Dimens.SpacingSingle)
            ) {
                AddEditCounterForm(
                    counterName = counterName,
                    onCounterNameChange = {
                        onEvent(AddEditCounterEvent.EnteredName(it))
                    },
                    counterDescription = counterDescription,
                    onCounterDescriptionChange = {
                        onEvent(AddEditCounterEvent.EnteredDescription(it))
                    },
                    counterValue = counterValue,
                    onCounterValueChange = {
                        onEvent(AddEditCounterEvent.EnteredValue(it))
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddEditCounterTopAppBar(
    onBack: () -> Unit,
    title: String,
    onCancel: () -> Unit,
    onSave: () -> Unit
) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = stringResource(R.string.go_back)
                )
            }
        },
        actions = {
            IconButton(onClick = onCancel) {
                Icon(
                    painter = painterResource(R.drawable.ic_cancel),
                    contentDescription = stringResource(R.string.cancel)
                )
            }
            IconButton(onClick = onSave) {
                Icon(
                    painter = painterResource(R.drawable.ic_save),
                    contentDescription = stringResource(R.string.save)
                )
            }
        }
    )
}