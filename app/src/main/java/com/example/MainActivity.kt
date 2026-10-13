package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.FolderShared
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.AdminScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.EnquiryScreen
import com.example.ui.screens.FaqScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.WorkspaceScreen
import com.example.ui.theme.CropShieldTheme
import com.example.ui.theme.DeepForestGreen
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.MutedText
import com.example.ui.viewmodel.AppDestination
import com.example.ui.viewmodel.CropShieldViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CropShieldTheme {
                CropShieldApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CropShieldApp(viewModel: CropShieldViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.infoMessage) {
        state.infoMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearInfoMessage()
        }
    }

    // Back handling to return to home if not on home screen
    if (state.currentDestination != AppDestination.HOME) {
        BackHandler {
            if (state.currentDestination == AppDestination.ADMIN) {
                viewModel.navigateTo(AppDestination.WORKSPACE)
            } else {
                viewModel.navigateTo(AppDestination.HOME)
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = EmeraldGreen,
                            shape = CircleShape,
                            modifier = Modifier.size(34.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.GraphicEq,
                                    contentDescription = "Brand icon",
                                    tint = DeepForestGreen,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Acoustic",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "Crop Shield",
                                    fontWeight = FontWeight.Light,
                                    fontSize = 17.sp,
                                    color = Color(0xFFD1E8D5)
                                )
                            }
                            Text(
                                text = "SMARTER CROP PROTECTION",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp,
                                color = EmeraldGreen
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DeepForestGreen,
                    titleContentColor = Color.White
                ),
                modifier = Modifier.testTag("app_top_bar")
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier.testTag("bottom_navigation_bar")
            ) {
                NavigationBarItem(
                    selected = state.currentDestination == AppDestination.HOME,
                    onClick = { viewModel.navigateTo(AppDestination.HOME) },
                    icon = { Icon(Icons.Default.Eco, contentDescription = "Concept") },
                    label = { Text("Concept", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = DeepForestGreen,
                        selectedTextColor = DeepForestGreen,
                        indicatorColor = EmeraldGreen.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.testTag("nav_item_home")
                )

                NavigationBarItem(
                    selected = state.currentDestination == AppDestination.DASHBOARD,
                    onClick = { viewModel.navigateTo(AppDestination.DASHBOARD) },
                    icon = { Icon(Icons.Default.Speed, contentDescription = "Dashboard") },
                    label = { Text("Dashboard", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = DeepForestGreen,
                        selectedTextColor = DeepForestGreen,
                        indicatorColor = EmeraldGreen.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.testTag("nav_item_dashboard")
                )

                NavigationBarItem(
                    selected = state.currentDestination == AppDestination.ENQUIRY,
                    onClick = { viewModel.navigateTo(AppDestination.ENQUIRY) },
                    icon = { Icon(Icons.Default.MailOutline, contentDescription = "Enquiry") },
                    label = { Text("Enquiry", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = DeepForestGreen,
                        selectedTextColor = DeepForestGreen,
                        indicatorColor = EmeraldGreen.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.testTag("nav_item_enquiry")
                )

                NavigationBarItem(
                    selected = state.currentDestination == AppDestination.WORKSPACE || state.currentDestination == AppDestination.ADMIN,
                    onClick = { viewModel.navigateTo(AppDestination.WORKSPACE) },
                    icon = { Icon(Icons.Default.FolderShared, contentDescription = "Workspace") },
                    label = { Text("Workspace", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = DeepForestGreen,
                        selectedTextColor = DeepForestGreen,
                        indicatorColor = EmeraldGreen.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.testTag("nav_item_workspace")
                )

                NavigationBarItem(
                    selected = state.currentDestination == AppDestination.FAQ,
                    onClick = { viewModel.navigateTo(AppDestination.FAQ) },
                    icon = { Icon(Icons.Default.HelpOutline, contentDescription = "FAQ") },
                    label = { Text("FAQ", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = DeepForestGreen,
                        selectedTextColor = DeepForestGreen,
                        indicatorColor = EmeraldGreen.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.testTag("nav_item_faq")
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (state.currentDestination) {
                AppDestination.HOME -> HomeScreen(
                    state = state,
                    onNavigate = { viewModel.navigateTo(it) }
                )
                AppDestination.DASHBOARD -> DashboardScreen(
                    state = state,
                    onToggleConnection = { viewModel.toggleHardwareConnection(it) },
                    onSelectBand = { viewModel.setFrequencyBand(it) },
                    onTimingChanged = { interval, dur -> viewModel.updateTiming(interval, dur) },
                    onTriggerPulse = { viewModel.triggerPulse() }
                )
                AppDestination.ENQUIRY -> EnquiryScreen(
                    state = state,
                    onNameChange = { viewModel.onFormNameChanged(it) },
                    onEmailChange = { viewModel.onFormEmailChanged(it) },
                    onInterestChange = { viewModel.onFormInterestChanged(it) },
                    onMessageChange = { viewModel.onFormMessageChanged(it) },
                    onSubmit = { viewModel.submitEnquiry() }
                )
                AppDestination.WORKSPACE -> WorkspaceScreen(
                    state = state,
                    onNavigate = { viewModel.navigateTo(it) },
                    onToggleRole = { viewModel.toggleUserRole() },
                    onUploadFile = { name, cat -> viewModel.uploadSimulatedFile(name, cat) },
                    onDeleteFile = { viewModel.deleteFile(it) }
                )
                AppDestination.ADMIN -> AdminScreen(
                    state = state,
                    onNavigate = { viewModel.navigateTo(it) },
                    onUpdateStatus = { id, s -> viewModel.updateEnquiryStatus(id, s) },
                    onDeleteEnquiry = { viewModel.deleteEnquiry(it) },
                    onSetFilter = { viewModel.setAdminFilter(it) }
                )
                AppDestination.FAQ -> FaqScreen(
                    state = state,
                    onNavigate = { viewModel.navigateTo(it) }
                )
            }
        }
    }
}
