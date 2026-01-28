package com.ilias.otakuclub.ui.search

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.ilias.otakuclub.R
import com.ilias.otakuclub.domain.model.AnimeCategories

@Composable
fun FilterAction(
    showFilter: Boolean,
    onFilterClick: () -> Unit,
    onDismissFilter: () -> Unit,
    onClearFilter: () -> Unit,
    categories: List<AnimeCategories>,
    selectedCategory: AnimeCategories?,
    onSelectedCategory: (AnimeCategories) -> Unit,
) {
    var showCategories by remember { mutableStateOf(false) }
    var showYears by remember { mutableStateOf(false) }
    var showNSFW by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = onFilterClick) {
            Icon(
                painter = painterResource(
                    if (onSelectedCategory == null) R.drawable.outline_filter_alt_off_24
                    else R.drawable.outline_filter_alt_24
                ),
                contentDescription = null,
                tint = if (selectedCategory != null) Color.Red else LocalContentColor.current
            )
            
            // BIG MENU
            DropdownMenu(
                expanded = showFilter,
                onDismissRequest = onDismissFilter
            ) {
                DropdownMenuItem(
                    text = { Text("ClearFilters") },
                    enabled = selectedCategory != null,
                    onClick = {
                        onClearFilter()
                        onDismissFilter()
                    }
                )
                HorizontalDivider()
                // CATEGORIES MENU
                DropdownMenuItem(
                    text = { Text(if ( showCategories ) "Categories ▾" else "Categories ▸" ) },
                    onClick = { showCategories = !showCategories }
                )
                if (showCategories){
                    categories.forEach { cat ->
                        DropdownMenuItem(
                            text = { Text("${cat.category} (${cat.count})") },
                            onClick = {
                                onSelectedCategory(cat)
                                onDismissFilter()
                            }
                        )
                    }
                }
                // YEARS MENU
                DropdownMenuItem(
                    text = { Text(if (showYears)  "Year ▾" else "Year ▸" ) },
                    onClick = { showYears =! showYears }
                )
                // NSFW MENU
                DropdownMenuItem(
                    text = { Text(if (showNSFW) "NSFW ON 😉" else "NSFW OFF") },
                    onClick = { showNSFW = !showNSFW }
                )
            }
        }
    }
}
