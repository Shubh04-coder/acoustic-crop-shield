package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeveloperBoard
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.SectionEyebrow
import com.example.ui.theme.AlertAmber
import com.example.ui.theme.BorderTint
import com.example.ui.theme.DeepForestGreen
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.LightForestGreen
import com.example.ui.theme.MutedText
import com.example.ui.viewmodel.AppDestination
import com.example.ui.viewmodel.CropShieldUiState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    state: CropShieldUiState,
    onNavigate: (AppDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
            .testTag("home_screen_container")
    ) {
        // Announcement Strip
        Surface(
            color = DeepForestGreen.copy(alpha = 0.08f),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .testTag("announcement_banner")
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(EmeraldGreen)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "A developing prototype for smarter, practical crop protection",
                    style = MaterialTheme.typography.bodyMedium,
                    color = DeepForestGreen,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Hero Card with Gradient and Agricultural Visual Theme
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DeepForestGreen),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("hero_card")
        ) {
            Column(
                modifier = Modifier
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(DeepForestGreen, LightForestGreen)
                        )
                    )
                    .padding(20.dp)
            ) {
                SectionEyebrow(text = "Field-Focused Innovation")

                Text(
                    text = "Give your crops a smarter shield.",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Text(
                    text = "An acoustic crop-protection concept exploring how sound-based deterrence and practical electronics can help farmers respond to unwanted visitors in the field.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFFD7E5DC),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { onNavigate(AppDestination.DASHBOARD) },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("hero_dashboard_button")
                    ) {
                        Text(
                            text = "Dashboard",
                            color = DeepForestGreen,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Go to dashboard",
                            tint = DeepForestGreen,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    OutlinedButton(
                        onClick = { onNavigate(AppDestination.ENQUIRY) },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("hero_contact_button")
                    ) {
                        Text(text = "Discuss Prototype", color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Hero Badges
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White.copy(alpha = 0.08f), RoundedCornerShape(8.dp))
                        .padding(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Eco,
                        contentDescription = "Farming challenge",
                        tint = EmeraldGreen,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Built around real farming challenges",
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Prototype-led • Field testing needed",
                            color = Color(0xFFA5C5B1),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Trust strip
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BorderTint, RoundedCornerShape(12.dp))
                .testTag("trust_strip_card")
        ) {
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TrustBadge(icon = Icons.Default.Shield, label = "Responsible-by-design")
                TrustBadge(icon = Icons.Default.DeveloperBoard, label = "Electronics + acoustics")
                TrustBadge(icon = Icons.Default.Sensors, label = "Monitoring-ready")
                TrustBadge(icon = Icons.Default.Eco, label = "Farmer-focused")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Section: The Idea
        SectionEyebrow(text = "The Idea")
        Text(
            text = "A practical idea for a persistent problem.",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Birds and other animals can damage crops and reduce harvest quality. Acoustic Crop Shield explores a sound-based deterrence approach as one possible part of a wider crop-protection strategy.",
            style = MaterialTheme.typography.bodyLarge,
            color = MutedText,
            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
        )

        // Feature cards grid
        state.features.forEach { feature ->
            FeatureCard(
                index = feature.index,
                title = feature.title,
                description = feature.description
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Honest by design note
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF9C3)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .testTag("honest_notice_card")
        ) {
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier.padding(14.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.HelpOutline,
                    contentDescription = "Notice",
                    tint = AlertAmber,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Honest by design: this is a developing concept, not a guarantee of crop protection. Effectiveness must be validated through safe, species-aware field testing.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF78350F),
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Section: From Idea to Field (Process)
        SectionEyebrow(text = "From Idea To Field")
        Text(
            text = "How the concept comes together",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "A simple overview of the intended system flow. Exact components and behaviour depend on the prototype hardware.",
            style = MaterialTheme.typography.bodyLarge,
            color = MutedText,
            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
        )

        state.processSteps.forEach { step ->
            ProcessStepCard(
                step = step.stepNumber,
                title = step.title,
                description = step.description
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Quick Action Footer Card
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = LightForestGreen),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("action_footer_card")
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Ready to test the system?",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )
                Text(
                    text = "Switch to the Digital Control Centre to simulate acoustic pulses, configure sweep bands, or review filed enquiries.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFD1E3D8),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Button(
                    onClick = { onNavigate(AppDestination.DASHBOARD) },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                    modifier = Modifier.testTag("footer_open_dashboard_button")
                ) {
                    Text(
                        text = "Open Control Dashboard",
                        color = DeepForestGreen,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun TrustBadge(icon: ImageVector, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = DeepForestGreen,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = DeepForestGreen,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun FeatureCard(
    index: String,
    title: String,
    description: String
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, BorderTint, RoundedCornerShape(12.dp))
            .testTag("feature_card_$index")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = DeepForestGreen
                )
                Text(
                    text = index,
                    style = MaterialTheme.typography.labelSmall,
                    color = EmeraldGreen,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MutedText
            )
        }
    }
}

@Composable
private fun ProcessStepCard(
    step: String,
    title: String,
    description: String
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, BorderTint, RoundedCornerShape(12.dp))
            .testTag("process_step_card_$step")
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Surface(
                color = DeepForestGreen,
                shape = CircleShape,
                modifier = Modifier.size(32.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = step,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MutedText
                )
            }
        }
    }
}
