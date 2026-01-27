package com.ilias.otakuclub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ilias.otakuclub.data.remote.ApiClient
import com.ilias.otakuclub.data.repository.AnimeRepositoryImpl
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
            var searchQuery by remember { mutableStateOf("") }
            var showFilter by remember { mutableStateOf(false) }

            val repo = remember { AnimeRepositoryImpl(ApiClient.jikanApi) }
            val searchVm: SearchViewModel = viewModel(factory = SearchViewModelFactory(repo = repo))
            val homeVm: HomeViewModel = viewModel(factory = HomeViewModelFactory(repo))
            OtakuClubTheme {
                Scaffold(
                    topBar = {
                        OtakuTopBar(
                            modifier = Modifier,
                            isSearching = isSearching,
                            query = searchQuery,
                            // search
                            onQueryChange = { searchQuery = it },
                            onSearchClick = { isSearching = true },
                            onCloseClick = {
                                isSearching = false
                                searchQuery = ""
                            },
                            onSubmit = { q ->
                                searchVm.loadSearchAnime(q)
                            },
                            // filter
                            onFilterClick = { showFilter = true },
                        )
                    }
                ) { innerPadding ->
                    HomeScreen(
                        paddingValues = innerPadding, homeViewModel = homeVm, repo = repo,
                        searchViewModel = searchVm,
                        query = searchQuery,
                        isSearching = isSearching
                    )
                }
            }
        }
    }
}


