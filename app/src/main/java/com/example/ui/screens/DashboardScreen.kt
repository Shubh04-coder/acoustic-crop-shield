package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.SolarPower
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.FrequencyBand
import com.example.ui.components.HardwareStatusIndicator
import com.example.ui.components.SectionEyebrow
import com.example.ui.theme.AlertAmber
import com.example.ui.theme.BorderTint
import com.example.ui.theme.DeepForestGreen
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.LightForestGreen
import com.example.ui.theme.MutedText
import com.example.ui.viewmodel.CropShieldUiState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DashboardScreen(
    state: CropShieldUiState,
    onToggleConnection: (Boolean) -> Unit,
    onSelectBand: (FrequencyBand) -> Unit,
    onTimingChanged: (Int, Int) -> Unit,
    onTriggerPulse: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val telemetry = state.telemetry

    val infiniteTransition = rememberInfiniteTransition(label = "pulse_orbit")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
            .testTag("dashboard_screen_container")
    ) {
        SectionEyebrow(text = "Digital Control Centre")
        Text(
            text = "Device Overview & Telemetry",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Monitor live sensor readings, simulate acoustic deterrent broadcasts, and configure frequency sweep intervals.",
            style = MaterialTheme.typography.bodyLarge,
            color = MutedText,
            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
        )

        // Main Hardware Status Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BorderTint, RoundedCornerShape(16.dp))
                .testTag("device_overview_card")
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "ACOUSTIC CROP SHIELD",
                            style = MaterialTheme.typography.labelSmall,
                            color = EmeraldGreen,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Node ACS-Alpha-01",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = DeepForestGreen
                        )
                    }
                    HardwareStatusIndicator(isConnected = telemetry.isConnected)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Visual Orbit Representation
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .background(
                            if (telemetry.isConnected) DeepForestGreen.copy(alpha = 0.05f) else Color(0xFFF9FAFB),
                            RoundedCornerShape(12.dp)
                        )
                        .testTag("device_illustration_box")
                ) {
                    if (telemetry.isPulsingActive) {
                        Box(
                            modifier = Modifier
                                .size(110.dp)
                                .scale(pulseScale)
                                .border(2.dp, EmeraldGreen.copy(alpha = 0.6f), CircleShape)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape)
                            .background(if (telemetry.isConnected) DeepForestGreen else Color(0xFF9CA3AF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.GraphicEq,
                            contentDescription = "Acoustic Node",
                            tint = Color.White,
                            modifier = Modifier.size(34.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Hardware Simulator Toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            if (telemetry.isConnected) Color(0xFFEFFBF3) else Color(0xFFF3F4F6),
                            RoundedCornerShape(10.dp)
                        )
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Hardware Link Simulator",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = DeepForestGreen
                        )
                        Text(
                            text = if (telemetry.isConnected) "Transmitting telemetry link via radio" else "Disconnected (Field hardware offline)",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MutedText
                        )
                    }

                    Switch(
                        checked = telemetry.isConnected,
                        onCheckedChange = { onToggleConnection(it) },
                        colors = SwitchDefaults.colors(checkedThumbColor = EmeraldGreen),
                        modifier = Modifier.testTag("hardware_connection_toggle")
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Telemetry Quick Stats
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MetricBox(
                        title = "Battery",
                        value = if (telemetry.isConnected) "${telemetry.batteryPercent}%" else "—",
                        sub = if (telemetry.isConnected) "Optimal" else "Offline",
                        icon = Icons.Default.BatteryChargingFull,
                        modifier = Modifier.weight(1f)
                    )
                    MetricBox(
                        title = "Solar Input",
                        value = if (telemetry.isConnected) "${telemetry.solarInputWatts} W" else "—",
                        sub = if (telemetry.isConnected) "Sunlight active" else "No reading",
                        icon = Icons.Default.SolarPower,
                        modifier = Modifier.weight(1f)
                    )
                    MetricBox(
                        title = "Sound Output",
                        value = if (telemetry.isConnected) "${telemetry.decibelLevel} dB" else "—",
                        sub = if (telemetry.isConnected) "Calibrated" else "Silenced",
                        icon = Icons.Default.VolumeUp,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Deterrent Control Panel
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BorderTint, RoundedCornerShape(16.dp))
                .testTag("deterrent_control_panel")
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "Acoustic Deterrence Configuration",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = DeepForestGreen
                )
                Text(
                    text = "Select frequencies optimized for targeted crop pests to minimize animal habituation.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MutedText,
                    modifier = Modifier.padding(top = 2.dp, bottom = 14.dp)
                )

                // Frequency Bands Selection
                FrequencyBand.values().forEach { band ->
                    val isSelected = band == telemetry.selectedBand
                    val bandBorder = if (isSelected) EmeraldGreen else BorderTint
                    val bandBg = if (isSelected) DeepForestGreen.copy(alpha = 0.05f) else Color.Transparent

                    Surface(
                        onClick = { onSelectBand(band) },
                        shape = RoundedCornerShape(10.dp),
                        color = bandBg,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .border(if (isSelected) 2.dp else 1.dp, bandBorder, RoundedCornerShape(10.dp))
                            .testTag("frequency_band_chip_${band.name.lowercase()}")
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = band.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) DeepForestGreen else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "${band.range} • Targets: ${band.targetPest}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MutedText
                                )
                            }
                            if (isSelected) {
                                Surface(
                                    color = EmeraldGreen,
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "ACTIVE",
                                        color = DeepForestGreen,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Pulse Timing Slider
                Text(
                    text = "Broadcast Interval: ${telemetry.pulseIntervalSeconds}s (Pulse: ${telemetry.pulseDurationSeconds}s)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = DeepForestGreen
                )
                Slider(
                    value = telemetry.pulseIntervalSeconds.toFloat(),
                    onValueChange = { onTimingChanged(it.toInt(), telemetry.pulseDurationSeconds) },
                    valueRange = 10f..120f,
                    steps = 10,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("pulse_interval_slider")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Trigger Test Pulse Button
                Button(
                    onClick = onTriggerPulse,
                    enabled = telemetry.isConnected,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (telemetry.isPulsingActive) AlertAmber else DeepForestGreen
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("trigger_pulse_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = "Trigger pulse",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (telemetry.isPulsingActive) "BURSTING DETERRENT SOUND (92 dB)..." else "Trigger Acoustic Test Pulse",
                        fontWeight = FontWeight.Bold
                    )
                }

                if (!telemetry.isConnected) {
                    Text(
                        text = "Connect the hardware link simulator above to enable live test triggers.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = AlertAmber,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Historical pulse log
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF9FAFB), RoundedCornerShape(8.dp))
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total Bursts Recorded:",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MutedText
                    )
                    Text(
                        text = "${telemetry.totalDeterrentPulses} acoustic pulses",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = DeepForestGreen
                    )
                }
            }
        }
    }
}

@Composable
private fun MetricBox(
    title: String,
    value: String,
    sub: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color(0xFFF9FAFB),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier.border(1.dp, BorderTint, RoundedCornerShape(10.dp))
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = EmeraldGreen,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = MutedText
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = DeepForestGreen
            )
            Text(
                text = sub,
                fontSize = 10.sp,
                color = MutedText
            )
        }
    }
}
