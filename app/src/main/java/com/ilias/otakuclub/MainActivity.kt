package com.ilias.otakuclub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ilias.otakuclub.data.remote.ApiClient
import com.ilias.otakuclub.data.repository.AnimeRepositoryImpl
import com.ilias.otakuclub.domain.model.AnimeCategories
import com.ilias.otakuclub.ui.category.CategoryViewModel
import com.ilias.otakuclub.ui.category.CategoryViewModelFactory
import com.ilias.otakuclub.ui.home.HomeScreen
import com.ilias.otakuclub.ui.home.HomeViewModel
import com.ilias.otakuclub.ui.home.HomeViewModelFactory
import com.ilias.otakuclub.ui.search.SearchViewModel
import com.ilias.otakuclub.ui.search.SearchViewModelFactory
import com.ilias.otakuclub.ui.theme.OtakuClubTheme
import com.ilias.otakuclub.ui.topBar.OtakuTopBar

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var isSearching by remember { mutableStateOf(false) }
            var isFiltering by remember { mutableStateOf(false) }
            var searchQuery by remember { mutableStateOf("") }
            var showFilter by remember { mutableStateOf(false) }
            var filteredCategory by remember { mutableStateOf<String?>(null) }
            var selectedCategory by remember { mutableStateOf<AnimeCategories?>(null) }
            var sfwEnabled by remember { mutableStateOf(true) }

            val repo = remember { AnimeRepositoryImpl(ApiClient.jikanApi) }
            val searchVm: SearchViewModel = viewModel(factory = SearchViewModelFactory(repo = repo))
            val homeVm: HomeViewModel = viewModel(factory = HomeViewModelFactory(repo))
            val categoryVm: CategoryViewModel = viewModel(factory = CategoryViewModelFactory(repo))
            val categoryState by categoryVm.uiState.collectAsState()
            OtakuClubTheme {
                Scaffold(
                    topBar = {
                        OtakuTopBar(
                            isSearching = isSearching,
                            query = searchQuery,
                            // search
                            onQueryChange = { searchQuery = it },
                            onSearchClick = {
                                isSearching = true
                                isFiltering = false
                                filteredCategory = null
                            },
                            onCloseClick = {
                                isSearching = false
                                searchQuery = ""
                            },
                            onSubmit = { q ->
                                isSearching = true
                                isFiltering = false
                                filteredCategory = null
                                searchVm.loadSearchAnime(
                                    q, genreId = null,
                                    sfw = sfwEnabled,
                                    startDate = null,
                                    endDate = null
                                )
                            },
                            // filter
                            showFilter = showFilter,
                            onFilterClick = { showFilter = true },
                            onDismissFilter = { showFilter = false },
                            onSelectCategory = { cat ->
                                selectedCategory = cat
                                isSearching = false
                                isFiltering = true
                                showFilter = false
                                filteredCategory = cat.category
                                searchVm.loadSearchAnime(
                                    q = searchQuery, genreId = cat.id,
                                    sfw = sfwEnabled,
                                    startDate = null,
                                    endDate = null
                                )
                            },
                            onCloseFilter = {
                                selectedCategory = null
                                isFiltering = false
                                filteredCategory = null
                            },
                            onSelectSFW = { newValue ->
                                sfwEnabled = newValue

                                if(!searchQuery.isBlank() && selectedCategory == null) return@OtakuTopBar

                                searchVm.loadSearchAnime(
                                    q = searchQuery,
                                    genreId = selectedCategory?.id,
                                    sfw = newValue,
                                    startDate = null,
                                    endDate = null
                                )
                            },
                            categories = categoryState.categories,
                            isFiltering = isFiltering,
                            selectedCategory = selectedCategory,
                            sfwEnabled = sfwEnabled,
                        )
                    }
                ) { innerPadding ->
                    HomeScreen(
                        paddingValues = innerPadding,
                        homeViewModel = homeVm,
                        repo = repo,
                        searchViewModel = searchVm,
                        isSearching = isSearching,
                        isFiltering = isFiltering,
                        filteredCategory = filteredCategory,
                    )
                }
            }
        }
    }
}


