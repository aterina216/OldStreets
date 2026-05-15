package com.example.oldstreets.ui.navigation

import androidx.compose.runtime.Composable
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
        composable(
            route = "photo/{url}/{title}/{year}",
            arguments = listOf(
                navArgument("url") { type = NavType.StringType },
                navArgument("title") { type = NavType.StringType },
                navArgument("year") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val url = backStackEntry.arguments?.getString("url")?.let { URLDecoder.decode(it, "UTF-8") } ?: return@composable
            val title = backStackEntry.arguments?.getString("title")?.let { URLDecoder.decode(it, "UTF-8") } ?: ""
            val yearStr = backStackEntry.arguments?.getString("year")
            val year = if (yearStr == "null") null else yearStr?.toIntOrNull()

            val photo = HistoricalPhoto(
                id = 0, // id не используется, можно 0 или передать отдельным параметром если нужно
                title = title,
                imageUrl = url,
                year = year
            )
            PhotoDetailScreen(navController, photo)
        }
    }
}