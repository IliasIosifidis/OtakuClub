package com.ilias.otakuclub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.ilias.otakuclub.data.remote.ApiClient
import com.ilias.otakuclub.data.repository.AnimeRepositoryImpl
import com.ilias.otakuclub.ui.home.HomeScreen
import com.ilias.otakuclub.ui.home.HomeViewModel
import com.ilias.otakuclub.ui.home.HomeViewModelFactory
import com.ilias.otakuclub.ui.theme.OtakuClubTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val repo = remember { AnimeRepositoryImpl(ApiClient.jikanApi) }
            val homeVm: HomeViewModel = viewModel(
                factory = HomeViewModelFactory(repo)
            )
            OtakuClubTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            colors = TopAppBarColors(
                                scrolledContainerColor = Color.Red,
                                navigationIconContentColor = Color.Red,
                                titleContentColor = Color.Black,
                                actionIconContentColor = Color.Red,
                                containerColor = Color.DarkGray
                            ),
                            title = { Text("Otaku Club")

                            }) },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    HomeScreen(paddingValues = innerPadding, homeVm)
                }
            }
        }
    }
}