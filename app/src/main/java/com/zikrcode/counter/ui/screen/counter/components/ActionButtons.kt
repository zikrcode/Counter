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
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.zikrcode.counter.R
import com.zikrcode.counter.ui.theme.CounterTheme
import com.zikrcode.counter.ui.utils.Dimens

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CounterActionButtons(
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
        Surface {
            CounterActionButtons(
                onResetClick = { },
                onDecrementClick = { },
                onEditClick = { }
            )
        }
    }
}
