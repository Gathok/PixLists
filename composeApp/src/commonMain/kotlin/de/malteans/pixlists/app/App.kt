package de.malteans.pixlists.app

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import de.malteans.pixlists.list.presentation.view.ViewListScreen
import de.malteans.pixlists.list.presentation.view.ViewListViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    MaterialTheme {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = Route.List
        ) {
            navigation<Route.List>(
                startDestination = Route.ViewList,
            ) {
                composable<Route.ViewList> {
                    val viewModel = koinViewModel<ViewListViewModel>()
                    ViewListScreen(viewModel)
                }
            }
        }
    }
}