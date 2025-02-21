package com.example.chatapp.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.chatapp.R

data class ChatSnackbarVisuals(
    // Don't use override properties in ChatSnackbar
    override val message: String = "",
    override val actionLabel: String? = null,

    @StringRes
    val messageRes: Int,
    override val duration: SnackbarDuration = SnackbarDuration.Long,
    override val withDismissAction: Boolean = true
): SnackbarVisuals

@Composable
fun ChatSnackbar(
    snackbarData: SnackbarData,
    modifier: Modifier = Modifier,
    actionOnNewLine: Boolean = false,
    shape: Shape = SnackbarDefaults.shape,
    containerColor: Color = SnackbarDefaults.color,
    contentColor: Color = SnackbarDefaults.contentColor,
    actionColor: Color = SnackbarDefaults.actionColor,
    actionContentColor: Color = SnackbarDefaults.actionContentColor,
    dismissActionContentColor: Color = SnackbarDefaults.dismissActionContentColor,
) {
    val chatSnackbarVisuals = snackbarData.visuals as? ChatSnackbarVisuals ?: return

    val dismissActionComposable: (@Composable () -> Unit)? =
        if (chatSnackbarVisuals.withDismissAction) {
            @Composable {
                TextButton(
                    colors = ButtonDefaults.textButtonColors(contentColor = actionColor),
                    onClick = { snackbarData.dismiss() },
                    content = { Text(stringResource(R.string.dismiss_snackbar_label)) }
                )
            }
        } else {
            null
        }

    Snackbar(
        modifier = modifier.padding(12.dp),
        action = null,
        dismissAction = dismissActionComposable,
        actionOnNewLine = actionOnNewLine,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        actionContentColor = actionContentColor,
        dismissActionContentColor = dismissActionContentColor,
        content = {
            Text(
                text = stringResource(chatSnackbarVisuals.messageRes)
            )
        }
    )
}
