package de.malteans.pixlists.list.presentation.main.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import de.malteans.pixlists.list.presentation.util.CustomDialog

// NewListDialog ----------------------------------------------------------------
@Composable
fun NewListDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
    invalidNames: List<String> = emptyList()
) {
    var name by remember { mutableStateOf("") }

    CustomDialog(
        onDismissRequest = onDismiss,
        leftIcon = {
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = null,
                modifier = Modifier.clickable { onDismiss() }
            )
        },
        title = { Text("New List") },
        rightIcon = {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                modifier = Modifier.clickable { onConfirm(name) }
            )
        },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Column {
            if (invalidNames.contains(name.trim())) {
                Row {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Error",
                        tint = MaterialTheme.colors.error,
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .height(16.dp)
                    )
                    Text(
                        text = "Name already in use",
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.error
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 0.dp, bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier
                        .fillMaxWidth(),
                    isError = invalidNames.contains(name.trim()),
                )
            }
        }
    }
}