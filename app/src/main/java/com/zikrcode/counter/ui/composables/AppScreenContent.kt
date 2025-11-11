/*
 * Copyright (C) 2023–2025 Zokirjon Mamadjonov
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

package com.zikrcode.counter.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.FeaturedPlayList
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.zikrcode.counter.ui.theme.CounterTheme
import com.zikrcode.counter.ui.utils.Dimens

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AppScreenContent(
    title: String,
    topBarStartIcon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    topBarEndIcon: (@Composable () -> Unit)? = null,
    loading: Boolean = false,
    floatingActionButton: @Composable () -> Unit = { },
    snackbarMessage: String? = null,
    onSnackbarShown: () -> Unit = { },
    contentPadding: PaddingValues = PaddingValues(Dimens.SpacingDouble),
    content: @Composable () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let { message ->
            snackbarHostState.showSnackbar(message = message)
            onSnackbarShown.invoke()
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            Column {
                AppTopBar(
                    title = title,
                    startIcon = topBarStartIcon,
                    endIcon = topBarEndIcon
                )
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        floatingActionButton = floatingActionButton,
        containerColor = CounterTheme.colorScheme.background
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(contentPadding),
            contentAlignment = Alignment.Center
        ) {
            if (loading) {
                CircularWavyProgressIndicator(
                    color = CounterTheme.colorScheme.main,
                    trackColor = CounterTheme.colorScheme.mainVariant
                )
            } else {
                content()
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun AppScreenContentPreview() {
    CounterTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            AppScreenContent(
                title = "Counter",
                topBarStartIcon = {
                    AppIconButton(
                        onClick = { },
                        icon = Icons.Outlined.Settings,
                        iconDescription = ""
                    )
                },
                topBarEndIcon = {
                    AppIconButton(
                        onClick = { },
                        icon = Icons.AutoMirrored.Outlined.FeaturedPlayList,
                        iconDescription = ""
                    )
                },
                loading = true,
                content = { }
            )
        }
    }
}
