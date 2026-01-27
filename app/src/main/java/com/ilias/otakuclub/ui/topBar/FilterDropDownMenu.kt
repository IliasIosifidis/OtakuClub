package com.ilias.otakuclub.ui.topBar

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun FilterDropDownMenu(
    onDismiss: () -> Unit,
    onFilterSelected: () -> Unit
){
    DropdownMenu(
        expanded = true,
        onDismissRequest = onDismiss
    ) {
        DropdownMenuItem(
            text = { Text("Categories") },
            onClick = {}
        )
    }

}