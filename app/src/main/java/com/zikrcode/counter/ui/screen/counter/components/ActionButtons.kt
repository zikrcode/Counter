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

package com.zikrcode.counter.ui.screen.counter.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExposureNeg1
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.zikrcode.counter.R
import com.zikrcode.counter.ui.theme.CounterTheme
import com.zikrcode.counter.ui.utils.Dimens

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ActionButtons(
    onResetClick: () -> Unit,
    onDecrementClick: () -> Unit,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingSingle),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedButtonActionButton(
            onClick = onResetClick,
            icon = Icons.Default.Replay,
            iconDescription = stringResource(R.string.reset_counter)
        )
        val largeSize = ButtonDefaults.LargeContainerHeight
        FilledTonalButton(
            onClick = onDecrementClick,
            modifier = modifier.heightIn(largeSize),
            colors = ButtonDefaults.filledTonalButtonColors().copy(
                containerColor = CounterTheme.colorScheme.mainVariant,
                contentColor = CounterTheme.colorScheme.iconDark
            ),
            contentPadding = ButtonDefaults.contentPaddingFor(largeSize),
        ) {
            Icon(
                imageVector = Icons.Default.ExposureNeg1,
                contentDescription = stringResource(R.string.decrement),
                modifier = Modifier.size(
                    ButtonDefaults.iconSizeFor(largeSize)
                )
            )
        }
        OutlinedButtonActionButton(
            onClick = onEditClick,
            icon = Icons.Default.Edit,
            iconDescription = stringResource(R.string.edit_counter)
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun OutlinedButtonActionButton(
    onClick: () -> Unit,
    icon: ImageVector,
    iconDescription: String
) {
    val mediumSize = ButtonDefaults.MediumContainerHeight
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.heightIn(mediumSize),
        colors = ButtonDefaults.outlinedButtonColors().copy(
            containerColor = Color.Transparent,
            contentColor = CounterTheme.colorScheme.icon
        ),
        border = ButtonDefaults.outlinedButtonBorder(true).copy(
            brush = SolidColor(CounterTheme.colorScheme.divider)
        ),
        contentPadding = ButtonDefaults.contentPaddingFor(mediumSize),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = iconDescription,
            modifier = Modifier.size(
                ButtonDefaults.iconSizeFor(mediumSize)
            )
        )
    }
}

@PreviewLightDark
@Composable
private fun ActionButtonsPreview() {
    CounterTheme {
        ActionButtons(
            onResetClick = { },
            onDecrementClick = { },
            onEditClick = { }
        )
    }
}
