package com.ilias.otakuclub.ui.details

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.ilias.otakuclub.R
import com.ilias.otakuclub.domain.repository.AnimeRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsBottomSheet(
    show: Boolean,
    selectedAnimeId: Int?,
    repo: AnimeRepository,
    onDismiss: () -> Unit,
) {
    if (!show) return

    val detailsViewModel: DetailsViewModel =
        viewModel(factory = DetailsViewModelFactory(repo = repo))

    val detailsUiState by detailsViewModel.uiState.collectAsState()
    val context = LocalContext.current // for trailer link at Details

    LaunchedEffect(selectedAnimeId) {
        selectedAnimeId?.let { detailsViewModel.loadAnimeDetails(it) }
    }

    ModalBottomSheet(
        onDismissRequest = { onDismiss() }
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
                            ) { append("Aired  ") }
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


