package de.malteans.pixlists

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import de.malteans.pixlists.list.presentation.main.components.NewListDialog

@Preview
@Composable
private fun NewListDialogPrev() {
    NewListDialog(
        onDismiss = {},
        onConfirm = {}
    )
}