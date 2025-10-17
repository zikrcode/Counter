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

package com.zikrcode.counter.ui.screen.counter_list.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.zikrcode.counter.R
import com.zikrcode.counter.domain.model.Counter
import com.zikrcode.counter.ui.composables.AppIconButton
import com.zikrcode.counter.ui.theme.CounterTheme
import com.zikrcode.counter.ui.utils.AppConstants
import com.zikrcode.counter.ui.utils.Dimens
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

val CounterListItemHeight = 160.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CounterListItem(
    counter: Counter,
    onClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        onClick = onClick,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(CounterListItemHeight)
                .padding(Dimens.SpacingDouble),
            horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingDouble),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Column {
                        Text(
                            text = counter.counterName,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            style = MaterialTheme.typography.headlineSmall
                        )
                        if (counter.counterDescription.isNotBlank()) {
                            Text(
                                text = counter.counterDescription,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 3,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
                Text(
                    text = formatDate(counter.counterDate),
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    maxLines = 1,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(color = MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = counter.counterSavedValue.toString(),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(Dimens.SpacingDouble),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AppIconButton(
                    onClick = onEditClick,
                    icon = Icons.Outlined.Edit,
                    iconDescription = stringResource(R.string.edit_counter),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f),
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                )
                AppIconButton(
                    onClick = onDeleteClick,
                    icon = Icons.Outlined.Delete,
                    iconDescription = stringResource(R.string.delete_counter),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f),
                        contentColor = MaterialTheme.colorScheme.error
                    )
                )
            }
        }
    }
}

private fun formatDate(timestamp: Long): String {
    val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return formatter.format(Date(timestamp))
}

@PreviewLightDark
@Composable
private fun CounterListItemPreview() {
    CounterTheme {
        Column {
            CounterListItem(
                counter = Counter(
                    id = 1,
                    counterName = "Very Long Counter Name That Tests Single Line Truncation Behavior",
                    counterDescription = "",
                    counterDate = 0L,
                    counterSavedValue = 0
                ),
                onClick = { },
                onEditClick = { },
                onDeleteClick = { }
            )
            Spacer(Modifier.height(Dimens.SpacingDouble))
            CounterListItem(
                counter = Counter(
                    id = 0,
                    counterName = "This is an extremely long counter name that should test text truncation and ellipsis behavior in the UI component",
                    counterDescription = "This is an extremely long counter description that spans multiple lines and should test how the text wrapping and maximum line limits work in the UI. It contains a lot of text to ensure we can see how the component handles very long descriptions that might overflow or need to be truncated with ellipsis.",
                    counterDate = System.currentTimeMillis(),
                    counterSavedValue = AppConstants.COUNTER_VALUE_RANGE.last
                ),
                onClick = { },
                onEditClick = { },
                onDeleteClick = { }
            )
        }
    }
}
