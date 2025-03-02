package de.malteans.pixlists.presentation.util.customDialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import de.malteans.pixlists.domain.PixCategory
import de.malteans.pixlists.presentation.util.Dropdown
import de.malteans.pixlists.presentation.util.customIcons.FilledPixIcon
import de.malteans.pixlists.presentation.util.customIcons.OutlinedPixIcon
import de.malteans.pixlists.util.Months
import de.malteans.pixlists.util.isValidDate
import de.malteans.pixlists.util.toStringDate
import kotlinx.datetime.Clock

@Composable
fun EntryDialog(
    categories: List<PixCategory>,
    onDismiss: () -> Unit,
    onEdit: (Int, Months, PixCategory?) -> Unit,
    startDate: Long = Clock.System.now().toEpochMilliseconds(),
    curCategory: PixCategory? = null,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        )
    ) {
        var selectedDate by remember { mutableStateOf(startDate.toStringDate()) }
        var selectedCategory by remember { mutableStateOf(curCategory) }

        var ready by remember { mutableStateOf(false) }

        LaunchedEffect(selectedDate, selectedCategory) {
            ready = selectedDate.isValidDate() && selectedCategory != null
        }

        CustomDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    "Set Entry",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            },
            leftIcon = {
                Row {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.clickable { onDismiss() }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Edit Date",
                        tint = if (ready) MaterialTheme.colorScheme.onSurface
                            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                        modifier = Modifier.clickable {
                            if (ready) {
                                val day = selectedDate.split(".")[0].toInt()
                                val month = Months.getByIndex(selectedDate.split(".")[1].toInt())
                                onEdit(day, month, null)
                            }
                        }
                    )
                }
            },
            rightIcon = {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Add",
                    tint = if (ready) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                    modifier = Modifier.clickable {
                        if (ready) {
                            val day = selectedDate.split(".")[0].toInt()
                            val month = Months.getByIndex(selectedDate.split(".")[1].toInt())
                            onEdit(day, month, selectedCategory)
                        }
                    }
                )
            },
            modifier = Modifier.padding(16.dp),
        ) {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = selectedDate,
                    onValueChange = { selectedDate = it },
                    label = { Text("Date") },
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Dropdown(
                    modifier = Modifier.fillMaxWidth(),
                    options = categories.associateBy({ it }, { it.name }),
                    label = "Category",
                    onValueChanged = { selectedCategory = it as PixCategory },
                    selectedOption = Pair(selectedCategory, selectedCategory?.name ?: ""),
                    optionIcon = { category ->
                        if (category != null) {
                            category as PixCategory
                            if (category.color != null) {
                                Icon(
                                    imageVector = FilledPixIcon,
                                    contentDescription = "Color",
                                    tint = category.color.toColor(),
                                )
                            } else {
                                Icon(
                                    imageVector = OutlinedPixIcon,
                                    contentDescription = "Color",
                                    tint = MaterialTheme.colorScheme.error,
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}