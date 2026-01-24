package com.ilias.otakuclub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.ilias.otakuclub.ui.home.HomeScreen
import com.ilias.otakuclub.ui.theme.OtakuClubTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
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
                    HomeScreen(paddingValues = innerPadding)
                }
            }
        }
    }
}