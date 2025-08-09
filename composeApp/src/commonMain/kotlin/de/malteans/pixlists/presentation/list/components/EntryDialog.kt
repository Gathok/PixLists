package de.malteans.pixlists.presentation.list.components

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
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import de.malteans.pixlists.domain.PixCategory
import de.malteans.pixlists.presentation.components.CustomDialog
import de.malteans.pixlists.presentation.components.Dropdown
import de.malteans.pixlists.presentation.components.OutlinedText
import de.malteans.pixlists.presentation.components.customIcons.FilledPixIcon
import de.malteans.pixlists.presentation.components.customIcons.OutlinedPixIcon
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import pixlists.composeapp.generated.resources.Res
import pixlists.composeapp.generated.resources.category
import pixlists.composeapp.generated.resources.date
import pixlists.composeapp.generated.resources.set_entry
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class, ExperimentalMaterial3Api::class)
@Composable
fun EntryDialog(
    categories: List<PixCategory>,
    onDismiss: () -> Unit,
    onEdit: (LocalDate, PixCategory?) -> Unit,
    startDate: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
    curCategory: PixCategory? = null,
) {
    var selectedDate by remember { mutableStateOf(startDate) }
    var selectedCategory by remember { mutableStateOf(curCategory) }

    var ready by remember { mutableStateOf(false) }

    LaunchedEffect(selectedCategory) {
        ready = selectedCategory != null
    }

    var showDatePickerDialog by remember { mutableStateOf(false) }

    if (showDatePickerDialog) {
        CustomDatePickerDialog(
            onDismissRequest = { showDatePickerDialog = false },
            onSubmit = { newDate ->
                selectedDate = newDate
                showDatePickerDialog = false
            },
            initialSelectedDate = selectedDate
        )
    }

    CustomDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(Res.string.set_entry),
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
                    contentDescription = "Delete Entry",
                    tint = if (ready) MaterialTheme.colorScheme.onSurface
                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                    modifier = Modifier.clickable {
                        if (ready) {
                            onEdit(selectedDate, null)
                        }
                    }
                )
            }
        },
        rightIcon = {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Submit",
                tint = if (ready) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                modifier = Modifier.clickable {
                    if (ready) {
                        onEdit(selectedDate, selectedCategory)
                    }
                }
            )
        },
        modifier = Modifier.padding(16.dp),
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 4.dp)
                .clickable { showDatePickerDialog = true },
        ) {
            OutlinedText(
                value = selectedDate.asString(),
                label = { Text(stringResource(Res.string.date)) },
                trailingIcon = @Composable {
                    Icon(
                        imageVector = Icons.Default.EditCalendar,
                        contentDescription = "Select Date",
                    )
                },
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
                label = stringResource(Res.string.category),
                onValueChanged = { selectedCategory = it as PixCategory },
                selectedOption = Pair(selectedCategory, selectedCategory?.name ?: ""),
                optionIcon = { category ->
                    if (category != null) {
                        category as PixCategory
                        if (category.color != null) {
                            Icon(
                                imageVector = FilledPixIcon,
                                contentDescription = "Filled Pix",
                                tint = category.color.toColor(),
                            )
                        } else {
                            Icon(
                                imageVector = OutlinedPixIcon,
                                contentDescription = "Outlined Pix",
                                tint = MaterialTheme.colorScheme.error,
                            )
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun LocalDate.asString(): String {
    return "${dayOfWeek.toUiText().asString()}, " +
            "${day.toString().padStart(2, '0')}.${month.number.toString().padStart(2, '0')}.${this.year}"
}