package com.zikrcode.counter.ui.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.zikrcode.counter.ui.theme.CounterTheme

@Composable
fun AppIconButton(
    onClick: () -> Unit,
    icon: ImageVector,
    iconDescription: String,
    modifier: Modifier = Modifier,
    colors: IconButtonColors? = null
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
        colors = colors ?: IconButtonDefaults.iconButtonColors(
            containerColor = CounterTheme.colorScheme.background,
            contentColor = CounterTheme.colorScheme.icon
        )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = iconDescription
        )
    }
}

@PreviewLightDark
@Composable
private fun AppIconButtonPreview() {
    CounterTheme {
        AppIconButton(
            onClick = { },
            icon = Icons.Outlined.Settings,
            iconDescription = ""
        )
    }
}
