package de.malteans.pixlists.presentation.util.customDialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun CustomDialog(
    onDismissRequest: () -> Unit,
    title: @Composable () -> Unit,
    leftIcon: @Composable () -> Unit = {},
    rightIcon: @Composable () -> Unit = {},
    modifier: Modifier = Modifier,
    properties: DialogProperties = DialogProperties(),
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties,
    ) {
        Box (
            modifier = modifier
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colors.surface)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column (
                        modifier = Modifier
                            .weight(1f),
                        horizontalAlignment = Alignment.Start
                    ) {
                        leftIcon()
                    }
                    Column (
                        modifier = Modifier
                            .wrapContentWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        title()
                    }
                    Column (
                        modifier = Modifier
                            .weight(1f),
                        horizontalAlignment = Alignment.End
                    ) {
                        rightIcon()
                    }
                }
                content()
            }
        }
    }
}