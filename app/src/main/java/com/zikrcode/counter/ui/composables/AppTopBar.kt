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

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.FeaturedPlayList
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.zikrcode.counter.ui.theme.CounterTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String,
    startIcon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    endIcon: (@Composable () -> Unit)? = null
) {
    CenterAlignedTopAppBar(
        title = {
            Text(text = title)
        },
        modifier = modifier,
        navigationIcon = { startIcon.invoke() },
        actions = { endIcon?.invoke() },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = CounterTheme.colorScheme.background,
            titleContentColor = CounterTheme.colorScheme.text,
        )
    )
}

@PreviewLightDark
@Composable
private fun AppTopBarPreview() {
    CounterTheme {
        AppTopBar(
            title = "Counter",
            startIcon = {
                AppIconButton(
                    onClick = { },
                    icon = Icons.Outlined.Settings,
                    iconDescription = ""
                )
            },
            endIcon = {
                AppIconButton(
                    onClick = { },
                    icon = Icons.AutoMirrored.Outlined.FeaturedPlayList,
                    iconDescription = ""
                )
            }
        )
    }
}
