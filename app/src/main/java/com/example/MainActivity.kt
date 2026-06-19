package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ui.*
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "onboarding") {
                        composable("onboarding") {
                            OnboardingScreen(
                                onStartClick = {
                                    navController.navigate("home") {
                                        popUpTo("onboarding") { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable("home") {
                            HomeScreen(
                                viewModel = viewModel,
                                onModulesClick = { navController.navigate("modules") },
                                onArchiveClick = { navController.navigate("archive_timeline") },
                                onAddClick = { navController.navigate("new_item") },
                                onSettingsClick = { /* Settings action - no-op */ },
                                onVehicleDetailClick = { navController.navigate("vehicle_detail") },
                                onHouseDetailClick = { navController.navigate("house_detail") }
                            )
                        }
                        composable("modules") {
                            ModulesScreen(
                                onInicioClick = { navController.navigate("home") },
                                onArchiveClick = { navController.navigate("archive_timeline") },
                                onVehicleClick = { navController.navigate("vehicle_detail") },
                                onHouseClick = { navController.navigate("house_detail") },
                                onSettingsClick = { /* Settings action */ }
                            )
                        }
                        composable("vehicle_detail") {
                            VehicleDetailScreen(
                                onBackClick = { navController.popBackStack() },
                                onFullHistoryClick = { navController.navigate("archive_timeline") }
                            )
                        }
                        composable("house_detail") {
                            HouseDetailScreen(
                                viewModel = viewModel,
                                onBackClick = { navController.popBackStack() },
                                onFullHistoryClick = { navController.navigate("archive_timeline") }
                            )
                        }
                        composable("new_item") {
                            NewItemScreen(
                                viewModel = viewModel,
                                onCloseClick = { navController.popBackStack() },
                                onSaveSuccess = { navController.popBackStack() }
                            )
                        }
                        composable("archive_timeline") {
                            ArchiveTimelineScreen(
                                viewModel = viewModel,
                                onInicioClick = { navController.navigate("home") },
                                onModulesClick = { navController.navigate("modules") },
                                onSearchClick = { navController.navigate("bento_archive") },
                                onSettingsClick = { /* Settings action */ }
                            )
                        }
                        composable("bento_archive") {
                            BentoArchiveScreen(
                                viewModel = viewModel,
                                onInicioClick = { navController.navigate("home") },
                                onModulesClick = { navController.navigate("modules") },
                                onBackTimelineClick = { navController.navigate("archive_timeline") },
                                onSettingsClick = { /* Settings action */ }
                            )
                        }
                    }
                }
            }
        }
    }
}
