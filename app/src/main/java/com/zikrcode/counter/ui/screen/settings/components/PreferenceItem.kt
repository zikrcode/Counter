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

package com.zikrcode.counter.ui.screen.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.zikrcode.counter.ui.theme.CounterTheme
import com.zikrcode.counter.ui.utils.Dimens

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PreferenceItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    name: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingSingle),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = CounterTheme.colorScheme.icon
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(Dimens.SpacingSingle)
        ) {
            Text(
                text = name,
                modifier = Modifier.fillMaxWidth(),
                color = CounterTheme.colorScheme.text,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = description,
                modifier = Modifier.fillMaxWidth(),
                color = CounterTheme.colorScheme.text,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors().copy(
                checkedThumbColor = CounterTheme.colorScheme.iconLight,
                checkedTrackColor = CounterTheme.colorScheme.main,
                checkedBorderColor = CounterTheme.colorScheme.main,
                uncheckedThumbColor = CounterTheme.colorScheme.icon,
                uncheckedTrackColor = CounterTheme.colorScheme.container,
                uncheckedBorderColor = CounterTheme.colorScheme.icon
            )
        )
    }
}

@PreviewLightDark
@Composable
private fun PreferenceItemPreview() {
    CounterTheme {
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .background(color = CounterTheme.colorScheme.background)

        ) {
            PreferenceItem(
                icon = Icons.Outlined.Settings,
                name = "Name",
                description = "Description",
                checked = false,
                onCheckedChange = { }
            )
            PreferenceItem(
                icon = Icons.Outlined.Settings,
                name = "Name",
                description = "Description",
                checked = true,
                onCheckedChange = { }
            )
        }
    }
}
