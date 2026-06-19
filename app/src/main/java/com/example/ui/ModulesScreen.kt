package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModulesScreen(
    onInicioClick: () -> Unit,
    onArchiveClick: () -> Unit,
    onVehicleClick: () -> Unit,
    onHouseClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Casa em Dia",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Primary
                            )
                        )
                    }
                },
                navigationIcon = {
                    Box(
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(PrimaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("AM", color = Color.White, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                    }
                },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Default.Settings, contentDescription = "Configurações", tint = Primary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
            )
        },
        bottomBar = {
            AppBottomNav(
                currentTab = "modulos",
                onInicioClick = onInicioClick,
                onModulesClick = {},
                onArchiveClick = onArchiveClick
            )
        },
        containerColor = Background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text(
                text = "Seus Módulos",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Primary
                )
            )

            Text(
                text = "Gerencie suas responsabilidades de forma organizada e eficiente.",
                style = MaterialTheme.typography.bodyMedium.copy(color = OnSurfaceVariant),
                modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
            )

            // Module Card - Carro
            ModuleSelectionCard(
                title = "Carro",
                description = "Manutenções, IPVA, seguro e histórico do seu veículo.",
                icon = Icons.Default.DirectionsCar,
                iconBg = PrimaryContainer,
                iconColor = OnPrimaryContainer,
                onClick = onVehicleClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Module Card - Casa
            ModuleSelectionCard(
                title = "Casa",
                description = "Rotinas de limpeza, reformas, pagamentos recorrentes e controle de garantias.",
                icon = Icons.Default.Home,
                iconBg = Color(0xFFDCFCE7),
                iconColor = Color(0xFF15803D),
                onClick = onHouseClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Module Card - Arquivo Vivo
            ModuleSelectionCard(
                title = "Arquivo Vivo",
                description = "Documentos importantes, manuais, notas fiscais e registros médicos.",
                icon = Icons.Default.Inventory,
                iconBg = Color(0xFFFEF3C7),
                iconColor = Color(0xFFB45309),
                onClick = onArchiveClick
            )
        }
    }
}
