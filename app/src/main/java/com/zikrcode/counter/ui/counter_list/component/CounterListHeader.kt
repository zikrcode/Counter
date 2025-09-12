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

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zikrcode.counter.R
import com.zikrcode.counter.ui.utils.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CounterListHeader(
    orderSectionVisible: Boolean,
    onToggleOrderSection: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.SpacingSingle),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ElevatedCard(
            modifier = Modifier
                .height(50.dp)
                .weight(1f)
        ) {
            Image(
                painter = painterResource(R.drawable.filter_background),
                contentDescription = null,
                modifier = Modifier.height(50.dp),
                contentScale = ContentScale.FillHeight
            )
        }
        Spacer(Modifier.width(Dimens.SpacingDouble))
        ElevatedCard(
            onClick = onToggleOrderSection,
            modifier = Modifier.size(width = 100.dp, height = 50.dp)
        ) {
            Icon(
                painter = painterResource(
                    if (orderSectionVisible) R.drawable.ic_filter_shrink
                    else R.drawable.ic_filter_expand
                ),
                contentDescription = stringResource(R.string.filter),
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Preview
@Composable
fun CounterListHeaderPreview() {
    CounterListHeader(
        orderSectionVisible = false,
        onToggleOrderSection = { }
    )
}