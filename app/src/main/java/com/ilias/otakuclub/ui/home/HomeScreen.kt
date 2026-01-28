package com.ilias.otakuclub.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.ilias.otakuclub.R
import com.ilias.otakuclub.data.repository.AnimeRepositoryImpl
import com.ilias.otakuclub.ui.details.DetailsBottomSheet
import com.ilias.otakuclub.ui.search.SearchViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    paddingValues: PaddingValues,
    homeViewModel: HomeViewModel,
    searchViewModel: SearchViewModel,
    isSearching: Boolean,
    isFiltering: Boolean,
    repo: AnimeRepositoryImpl,
    filteredCategory: String?
) {
    val homeUiState by homeViewModel.uiState.collectAsState()
    val searchState by searchViewModel.uiState.collectAsState()

    val showingResults = isSearching || isFiltering
    val listOfAnime = if (showingResults) searchState.searchResults else homeUiState.anime // for the Grid
    val activeLoading = if (showingResults) searchState.isLoading else homeUiState.isLoading
    val activeError = if (showingResults) searchState.errorMessage else homeUiState.errorMessage

    val gridState = rememberLazyGridState() // for pagination
    val scope = rememberCoroutineScope() // for infinite scroll
    val showScrollToTop by remember { derivedStateOf { gridState.firstVisibleItemIndex > 0 } } // for the scroll-up arrow
    var selectedAnimeId by remember { mutableStateOf<Int?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }// for clickable

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(MaterialTheme.colorScheme.background)
    ) {
        when {
            activeLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            activeError != null -> {
                Text(
                    text = activeError,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            else -> {
                Column {
                    if (isFiltering) Text("Filtered category: $filteredCategory")
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        state = gridState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = listOfAnime,
                            key = { it.id }
                        ) {
                            // THE WHOLE CARD
                            Card(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable {
                                        showBottomSheet = true
                                        selectedAnimeId = it.id
                                    }
                                    .aspectRatio(.65f)
                                    .background(MaterialTheme.colorScheme.background)
                                    .border(
                                        1.dp,
                                        color = Color.DarkGray,
                                        shape = RoundedCornerShape(10)
                                    )
                            ) {
                                // THE CARD'S ARRANGEMENTS
                                Column(
                                    verticalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.Black)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .aspectRatio(2.2f / 3f)
                                            .weight(.7f)
                                            .background(Color.Black)
                                    ) {
                                        // THE IMAGE
                                        AsyncImage(
                                            model = it.imageUrlSmall,
                                            contentDescription = it.title,
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .aspectRatio(2.2f / 3f),
                                            contentScale = ContentScale.Crop,
                                            onError = { println("Image error") }
                                        )
                                        // THE VIGNETTE
                                        Box(
                                            modifier = Modifier
                                                .matchParentSize()
                                                .background(
                                                    Brush.verticalGradient(
                                                        colors = listOf(
                                                            Color.Transparent,
                                                            Color.Black.copy(alpha = 1f)
                                                        ),
                                                        startY = 280f
                                                    )
                                                )
                                        )
                                    }
                                    // THE TITLE
                                    Text(
                                        textAlign = TextAlign.Center,
                                        text = it.title,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(2.dp)
                                            .weight(.25f),
                                        maxLines = 2,
                                        color = Color.LightGray,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                }
                // INFINITE SCROLL
                LaunchedEffect(gridState) {
                    snapshotFlow { gridState.layoutInfo }
                        .collect { layoutInfo ->
                            val total = layoutInfo.totalItemsCount
                            val lastVisible = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

                            if (total > 0 && lastVisible >= total - 6) {
                                homeViewModel.loadNextPage()
                            }
                        }
                }

                // ACTION OF CLICKING THE SCROLL-TOP ARROW
                if (showScrollToTop) {
                    FloatingActionButton(
                        onClick = {
                            scope.launch {
                                gridState.animateScrollToItem(0)
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp),
                        containerColor = Color.Gray
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.outline_arrow_upward_24),
                            contentDescription = null,
                        )
                    }
                }

                // OPEN THE ANIME-DETAILS MODAL
                DetailsBottomSheet(
                    show = showBottomSheet,
                    selectedAnimeId = selectedAnimeId,
                    repo = repo,
                    onDismiss = { showBottomSheet = false }
                )
            }
        }
    }
}