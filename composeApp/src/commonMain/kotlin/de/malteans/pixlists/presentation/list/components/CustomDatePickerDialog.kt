@file:OptIn(ExperimentalTime::class)

package de.malteans.pixlists.presentation.list.components

import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import pixlists.composeapp.generated.resources.Res
import pixlists.composeapp.generated.resources.cancel
import pixlists.composeapp.generated.resources.select_date
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePickerDialog(
    onDismissRequest: () -> Unit,
    onSubmit: (LocalDate) -> Unit,
    initialSelectedDate: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
) {
    val datePickerState = rememberDatePickerState(
        yearRange = 2025..2025,
        initialSelectedDateMillis = initialSelectedDate.toEpochDays() * 24L * 60L * 60L * 1000L,
    )

    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            Button(
                onClick = { onSubmit(datePickerState.selectedDateMillis!!.toLocalDate()) },
            ) {
                Text(
                    text = stringResource(Res.string.select_date),
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest,
            ) {
                Text(
                    text = stringResource(Res.string.cancel),
                )
            }
        },
    ) {
        DatePicker(
            state = datePickerState,
        )
    }
}

fun Long.toLocalDate(timeZone: TimeZone = TimeZone.UTC): LocalDate {
    return Instant.fromEpochMilliseconds(this).toLocalDateTime(timeZone).date
}