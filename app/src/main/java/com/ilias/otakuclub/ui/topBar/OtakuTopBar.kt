package com.ilias.otakuclub.ui.topBar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.ilias.otakuclub.R
import com.ilias.otakuclub.domain.model.AnimeCategories
import com.ilias.otakuclub.ui.search.FilterAction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtakuTopBar(
    title: String = "Otaku",
    actions: @Composable RowScope.() -> Unit = {},
// Search functionality
    isSearching: Boolean,
    query: String,
    onQueryChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    onCloseClick: () -> Unit,
    onSubmit: (String) -> Unit,
// Filter functionality
    isFiltering: Boolean,
    showFilter: Boolean,
    onFilterClick: () -> Unit,
    onDismissFilter: () -> Unit,
    onSelectCategory: (AnimeCategories) -> Unit,
    selectedCategory: AnimeCategories?,
    categories: List<AnimeCategories>,
    onCloseFilter: () -> Unit
) {
    TopAppBar(
        modifier = Modifier.height(85.dp),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.DarkGray,
            actionIconContentColor = Color.LightGray,
        ),
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
        title = { Text(title) },

        actions = {
            // Search
            if (isSearching) {
                TextField(
                    modifier = Modifier.width(200.dp),
                    value = query,
                    onValueChange = onQueryChange,
                    placeholder = { Text("Search...") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { onSubmit(query) })
                )
            }
            if (isSearching) {
                IconButton(onClick = onCloseClick) {
                    Icon(
                        painterResource(R.drawable.outline_search_off_24),
                        contentDescription = "search"
                    )
                }
            } else {
                IconButton(onClick = onSearchClick) {
                    Icon(
                        painterResource(R.drawable.outline_search_24),
                        contentDescription = "search"
                    )
                }
            }
            // Filter
            FilterAction(
                showFilter = showFilter,
                onFilterClick = onFilterClick,
                onDismissFilter = onDismissFilter,
                categories = categories,
                onSelectedCategory = { cat ->
                    onSelectCategory(cat)
                    onDismissFilter()
                },
                onClearFilter = onCloseFilter,
                selectedCategory = selectedCategory
            )
        }
    )
}