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

package com.zikrcode.counter.ui.counter_list.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zikrcode.counter.R
import com.zikrcode.counter.domain.utils.CounterOrder
import com.zikrcode.counter.domain.utils.OrderType
import com.zikrcode.counter.ui.utils.Dimens

@Composable
fun OrderSection(
    modifier: Modifier = Modifier,
    counterOrder: CounterOrder = CounterOrder.Date(OrderType.DESCENDING),
    onOrderChange: (CounterOrder) -> Unit
) {
    ElevatedCard(
        modifier = modifier.padding(Dimens.SpacingSingle)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.filter_by),
                modifier = Modifier
                    .width(82.dp)
                    .padding(start = Dimens.SpacingDouble),
                style = MaterialTheme.typography.bodyLarge
            )
            DefaultRadioButton(
                text = stringResource(R.string.name),
                selected = counterOrder is CounterOrder.Name,
                onSelect = {
                    onOrderChange(CounterOrder.Name(counterOrder.orderType))
                }
            )
            Spacer(Modifier.Companion.width(Dimens.SpacingSingle))
            DefaultRadioButton(
                text = stringResource(R.string.date),
                selected = counterOrder is CounterOrder.Date,
                onSelect = {
                    onOrderChange(CounterOrder.Date(counterOrder.orderType))
                }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.sort_by),
                modifier = Modifier
                    .width(82.dp)
                    .padding(start = Dimens.SpacingDouble),
                style = MaterialTheme.typography.bodyLarge
            )
            DefaultRadioButton(
                text = stringResource(R.string.ascending),
                selected = counterOrder.orderType == OrderType.ASCENDING,
                onSelect = {
                    onOrderChange(counterOrder.copy(OrderType.ASCENDING))
                }
            )
            Spacer( Modifier.Companion.width(Dimens.SpacingSingle))
            DefaultRadioButton(
                text = stringResource(R.string.descending),
                selected = counterOrder.orderType == OrderType.DESCENDING,
                onSelect = {
                    onOrderChange(counterOrder.copy(OrderType.DESCENDING))
                }
            )
        }
    }
}

@Preview
@Composable
fun OrderSectionPreview() {
    OrderSection { }
}

@Composable
private fun DefaultRadioButton(
    text: String,
    selected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onSelect,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.primary,
                unselectedColor = MaterialTheme.colorScheme.onBackground
            )
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}