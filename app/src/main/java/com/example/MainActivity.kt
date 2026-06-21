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
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.ui.*
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        createNotificationChannel()
        requestNotificationPermission()
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
                                onHouseDetailClick = { navController.navigate("house_detail") },
                                onItemClick = { itemId -> navController.navigate("item_detail/$itemId") }
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
                        composable(
                            route = "new_item?itemId={itemId}",
                            arguments = listOf(
                                navArgument("itemId") {
                                    type = NavType.IntType
                                    defaultValue = -1
                                }
                            )
                        ) { backStackEntry ->
                            val itemId = backStackEntry.arguments?.getInt("itemId") ?: -1
                            NewItemScreen(
                                viewModel = viewModel,
                                itemId = if (itemId != -1) itemId else null,
                                onCloseClick = { navController.popBackStack() },
                                onSaveSuccess = { navController.popBackStack() }
                            )
                        }
                        composable(
                            route = "item_detail/{itemId}",
                            arguments = listOf(navArgument("itemId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val itemId = backStackEntry.arguments?.getInt("itemId") ?: 0
                            ItemDetailScreen(
                                viewModel = viewModel,
                                itemId = itemId,
                                onBackClick = { navController.popBackStack() },
                                onEditClick = { id -> navController.navigate("new_item?itemId=$id") },
                                onResolveSuccess = { navController.popBackStack() }
                            )
                        }
                        composable("archive_timeline") {
                            ArchiveTimelineScreen(
                                viewModel = viewModel,
                                onInicioClick = { navController.navigate("home") },
                                onModulesClick = { navController.navigate("modules") },
                                onSearchClick = { navController.navigate("bento_archive") },
                                onSettingsClick = { /* Settings action */ },
                                onHistoryClick = { historyId -> navController.navigate("history_detail/$historyId") }
                            )
                        }
                        composable(
                            route = "history_detail/{historyId}",
                            arguments = listOf(navArgument("historyId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val historyId = backStackEntry.arguments?.getInt("historyId") ?: 0
                            HistoryDetailScreen(
                                viewModel = viewModel,
                                historyId = historyId,
                                onBackClick = { navController.popBackStack() },
                                onRestoreSuccess = { navController.popBackStack() },
                                onDeleteSuccess = { navController.popBackStack() }
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

    private fun createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val name = "Avisos de Manutenção"
            val descriptionText = "Notificações de lembretes e prazos de manutenção"
            val importance = android.app.NotificationManager.IMPORTANCE_DEFAULT
            val channel = android.app.NotificationChannel("maintenance_alerts", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: android.app.NotificationManager =
                getSystemService(android.content.Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun requestNotificationPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 101)
            }
        }
    }
}
