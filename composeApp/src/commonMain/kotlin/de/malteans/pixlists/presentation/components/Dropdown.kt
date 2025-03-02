package de.malteans.pixlists.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dropdown(
    modifier: Modifier,
    label: String,
    selectedOption: Pair<Any?, String>,
    options: Map<Any, String>,
    onValueChanged: (Any) -> Unit,
    optionIcon: @Composable ((Any?) -> Unit)? = null,
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier
    ) {
        OutlinedTextField(
            readOnly = true,
            value = selectedOption.second,
            onValueChange = { },
            leadingIcon = if (optionIcon != null) { { optionIcon(selectedOption.first) } }
                else null,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            label = { Text(label) },
            colors = OutlinedTextFieldDefaults.colors(),
            modifier = Modifier
                .menuAnchor(type = MenuAnchorType.PrimaryEditable, enabled = true)
                .fillMaxWidth(),
            singleLine = true,
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.toList().forEach { pair ->
                val (option, text) = pair
                DropdownMenuItem(
                    text = { Text(text = text) },
                    onClick = {
                        expanded = false
                        onValueChanged(option)
                    },
                    leadingIcon = if (optionIcon != null) { { optionIcon(option) } }
                        else null
                )
            }
        }
    }
}