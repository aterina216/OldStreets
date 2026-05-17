package com.example.oldstreets.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.oldstreets.domain.model.HistoricalPhoto
import com.example.oldstreets.ui.screen.LocationScreen
import com.example.oldstreets.ui.screen.PhotoDetailScreen
import com.example.oldstreets.ui.viewmodel.MainViewModel
import java.net.URLDecoder

@Composable
fun InitNavigation(viewModel: MainViewModel) {

    val navController = rememberNavController()

    NavHost(navController, "home") {
        composable("home") {
            LocationScreen(viewModel, navController)
        }
        composable("photo") {
            val photo by viewModel.selectedPhoto.collectAsState()
            photo?.let {
                PhotoDetailScreen(navController, it)
            } ?: run {
                LaunchedEffect(Unit) {
                    navController.navigate("home")
                }
            }
        }
    }
}