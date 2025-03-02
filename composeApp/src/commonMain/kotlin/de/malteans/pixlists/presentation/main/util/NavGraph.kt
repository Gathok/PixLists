package de.malteans.pixlists.presentation.main.util

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import de.malteans.pixlists.app.Route
import de.malteans.pixlists.presentation.list.ListScreen
import de.malteans.pixlists.presentation.list.LoadingScreen
import de.malteans.pixlists.presentation.manageColors.ManageColorsScreen

@Composable
fun NavGraph(
    navController: NavHostController, openDrawer: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Route.ListScreen(),
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None },
    ) {
        // List Screen
        composable<Route.ListScreen> {
            val args = it.toRoute<Route.ListScreen>()
            ListScreen(
                openDrawer = openDrawer,
                curPixListId = args.curPixListId,
            )
        }
        // Manage Colors Screen
        composable<Route.ManageColorsScreen> {
            ManageColorsScreen(
                openDrawer = openDrawer,
            )
        }
        // Loading Screen
        composable<Route.LoadingScreen> {
            LoadingScreen(openDrawer = openDrawer)
        }
    }
}