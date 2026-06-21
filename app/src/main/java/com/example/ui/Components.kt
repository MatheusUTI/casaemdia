package com.example.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.theme.*

@Composable
fun AppBottomNav(
    currentTab: String,
    onInicioClick: () -> Unit,
    onModulesClick: () -> Unit,
    onArchiveClick: () -> Unit
) {
    NavigationBar(
        containerColor = SurfaceContainerLowest,
        tonalElevation = 8.dp,
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.navigationBars)
            .height(68.dp)
    ) {
        NavigationBarItem(
            selected = currentTab == "inicio",
            onClick = onInicioClick,
            icon = { Icon(Icons.Default.Home, contentDescription = "Início") },
            label = { Text("Início", fontWeight = FontWeight.Bold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Primary,
                selectedTextColor = Primary,
                unselectedIconColor = Outline,
                unselectedTextColor = Outline,
                indicatorColor = SecondaryContainer
            ),
            modifier = Modifier.testTag("nav_tab_inicio")
        )

        NavigationBarItem(
            selected = currentTab == "modulos",
            onClick = onModulesClick,
            icon = { Icon(Icons.Default.GridView, contentDescription = "Módulos") },
            label = { Text("Módulos", fontWeight = FontWeight.Bold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Primary,
                selectedTextColor = Primary,
                unselectedIconColor = Outline,
                unselectedTextColor = Outline,
                indicatorColor = SecondaryContainer
            ),
            modifier = Modifier.testTag("nav_tab_modulos")
        )

        NavigationBarItem(
            selected = currentTab == "arquivo",
            onClick = onArchiveClick,
            icon = { Icon(Icons.Default.Inventory, contentDescription = "Arquivo") },
            label = { Text("Arquivo", fontWeight = FontWeight.Bold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Primary,
                selectedTextColor = Primary,
                unselectedIconColor = Outline,
                unselectedTextColor = Outline,
                indicatorColor = SecondaryContainer
            ),
            modifier = Modifier.testTag("nav_tab_arquivo")
        )
    }
}

@Composable
fun SectionHeader(
    title: String,
    icon: ImageVector,
    iconColor: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = iconColor,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Primary)
        )
    }
}
