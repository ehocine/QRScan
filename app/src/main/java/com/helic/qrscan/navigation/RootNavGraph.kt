package com.helic.qrscan.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.material.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.helic.qrscan.viewmodel.MainViewModel
import com.helic.qrscan.views.*


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RootNavGraph(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    snackbar: (String, SnackbarDuration) -> Unit
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = Screens.Home.route,
        route = "root"
    ) {
        composable(
            route = Screens.Home.route,
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -300 },
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    )
                ) + fadeOut(animationSpec = tween(300))
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -300 },
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    )
                ) + fadeIn(animationSpec = tween(300))
            }
        ) {
            Home(
                navController = navController,
                mainViewModel = mainViewModel,
                snackbar = snackbar
            )
        }

        composable(
            route = Screens.ScanQRCode.route,
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -300 },
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    )
                ) + fadeOut(animationSpec = tween(300))
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -300 },
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    )
                ) + fadeIn(animationSpec = tween(300))
            }
        ) {
            ScanQRCode(
                navController = navController,
                mainViewModel = mainViewModel
            )
        }

        composable(
            route = Screens.GenerateQRScreen.route,
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -300 },
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    )
                ) + fadeOut(animationSpec = tween(300))
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -300 },
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    )
                ) + fadeIn(animationSpec = tween(300))
            }
        ) {
            GenerateQRScreen(
                navController = navController,
                mainViewModel = mainViewModel,
                snackbar = snackbar
            )
        }
        composable(
            route = Screens.Details.route,
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -300 },
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    )
                ) + fadeOut(animationSpec = tween(300))
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -300 },
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    )
                ) + fadeIn(animationSpec = tween(300))
            }
        ) {
            Details(
                navController = navController,
                mainViewModel = mainViewModel,
                snackbar = snackbar
            )
        }
    }
}
