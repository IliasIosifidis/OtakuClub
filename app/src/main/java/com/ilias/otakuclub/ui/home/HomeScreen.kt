package com.ilias.otakuclub.ui.home

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.ilias.otakuclub.R
import com.ilias.otakuclub.data.repository.AnimeRepositoryImpl
import com.ilias.otakuclub.ui.details.DetailsViewModel
import com.ilias.otakuclub.ui.details.DetailsViewModelFactory
import com.ilias.otakuclub.ui.search.SearchViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    paddingValues: PaddingValues,
    homeViewModel: HomeViewModel,
    searchViewModel: SearchViewModel,
    query: String,
    isSearching: Boolean,
    repo: AnimeRepositoryImpl
) {
    val homeUiState by homeViewModel.uiState.collectAsState()
    val searchState by searchViewModel.uiState.collectAsState()
    val listOfAnime = if (isSearching) searchState.searchResults else homeUiState.anime // for the Grid
    val gridState = rememberLazyGridState() // for pagination
    val showScrollToTop by remember { derivedStateOf { gridState.firstVisibleItemIndex > 0 } } // for the scroll-up arrow
    val scope = rememberCoroutineScope() // for infinite scroll
    val context = LocalContext.current // for trailer link at Details
    var selectedAnimeId by remember { mutableStateOf<Int?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }// for clickable

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(MaterialTheme.colorScheme.background)
    ) {
        when {
            homeUiState.isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            homeUiState.errorMessage != null -> {
                Text(
                    text = homeUiState.errorMessage ?: "Error",
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            else -> {
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
                if (showBottomSheet) {

                    val detailsViewModel: DetailsViewModel =
                        viewModel(factory = DetailsViewModelFactory(repo = repo))
                    val detailsUiState by detailsViewModel.uiState.collectAsState()

                    LaunchedEffect(selectedAnimeId) {
                        detailsViewModel.loadAnimeDetails(selectedAnimeId!!)
                    }

                    ModalBottomSheet(
                        onDismissRequest = { showBottomSheet = false }
                    ) {
                        when {
                            // waiting indicator
                            detailsUiState.isLoading -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(32.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                            // in case of error
                            detailsUiState.errorMessage != null -> {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    Text(detailsUiState.errorMessage!!)
                                    Text(text = "Retry")
                                }
                            }
                            // all went well, make the Bottom Sheet Modal appear
                            detailsUiState.animeDetails != null -> {
                                // BOTTOM SHEET MODAL INTERFACE
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                        .verticalScroll(rememberScrollState())
                                ) {
                                    // title
                                    Text(
                                        text = detailsUiState.animeDetails!!.title,
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                    Text(
                                        text = detailsUiState.animeDetails!!.titleJap.toString(),
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                    // image
                                    AsyncImage(
                                        contentScale = ContentScale.FillWidth,
                                        model = detailsUiState.animeDetails!!.imageUrlLarge,
                                        contentDescription = detailsUiState.animeDetails!!.title,
                                        onError = { println(detailsUiState.errorMessage) },
                                        modifier = Modifier.padding(10.dp)
                                    )
                                    // score
                                    Row(
                                        horizontalArrangement = Arrangement.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = buildAnnotatedString {
                                                withStyle(
                                                    SpanStyle(
                                                        fontSize = 18.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color(0xFF59CC5D)
                                                    )
                                                )
                                                { append("Score: ") }
                                                append("${detailsUiState.animeDetails!!.score}")
                                            },
                                            style = MaterialTheme.typography.bodyMedium
                                        )

                                        Icon(
                                            painterResource(R.drawable.outline_star_rate_24),
                                            contentDescription = "star",
                                            tint = Color.Yellow
                                        )
                                    }
                                    // genre
                                    Text(
                                        text = buildAnnotatedString {
                                            withStyle(
                                                SpanStyle(
                                                    fontSize = 18.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color(0xFFE91E63)
                                                )
                                            )
                                            { append("Genre: ") }
                                            append("${detailsUiState.animeDetails!!.genres}")
                                        },
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    // aired from-to
                                    Text(
                                        text = buildAnnotatedString {
                                            withStyle(
                                                SpanStyle(
                                                    fontSize = 18.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color(0xFF00B0FF)
                                                )
                                            ){append("Aired  ")}
                                            append("from: ${detailsUiState.animeDetails!!.airedFrom}, to: ${detailsUiState.animeDetails!!.airedTo}")
                                        }
                                    )
                                    // number of episodes
                                    Text(text = "Number of episodes:${detailsUiState.animeDetails!!.episodes}")
                                    // duration
                                    Text(text = "Duration: ${detailsUiState.animeDetails!!.duration}")
                                    Spacer(Modifier.height(10.dp))
                                    // synopsis
                                    Text(
                                        text = "Synopsis",
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                    HorizontalDivider(Modifier.padding(5.dp))
                                    Text(text = "${detailsUiState.animeDetails!!.synopsis}")
                                    Spacer(Modifier.height(10.dp))
                                    // background
                                    Text(
                                        text = "Background",
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                    HorizontalDivider(Modifier.padding(5.dp))
                                    Text(text = "${detailsUiState.animeDetails!!.background}")
                                    // trailer
                                    Text(text = "Trailer")
                                    HorizontalDivider(Modifier.padding(5.dp))
                                    TextButton(
                                        onClick = {
                                            detailsUiState.animeDetails!!.trailer?.let {
                                                val intent = Intent(Intent.ACTION_VIEW, it.toUri())
                                                context.startActivity(intent)
                                            }
                                        },
                                        enabled = !detailsUiState.animeDetails!!.trailer.isNullOrBlank()
                                    ) {
                                        Text(text = "Watch Trailer")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}



























