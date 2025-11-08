package com.zikrcode.counter.ui.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.zikrcode.counter.ui.theme.CounterTheme

@Composable
fun AppHorizontalDivider(modifier: Modifier = Modifier) {
    HorizontalDivider(
        modifier = modifier,
        color = CounterTheme.colorScheme.divider
    )
}

@PreviewLightDark
@Composable
private fun AppHorizontalDividerPreview() {
    CounterTheme {
        AppHorizontalDivider(Modifier.padding(10.dp))
    }
}
