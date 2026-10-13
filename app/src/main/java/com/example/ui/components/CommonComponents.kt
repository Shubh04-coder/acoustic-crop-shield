package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.EnquiryStatus
import com.example.ui.theme.AlertAmber
import com.example.ui.theme.BorderTint
import com.example.ui.theme.DeepForestGreen
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.LightForestGreen
import com.example.ui.theme.MutedText

@Composable
fun SectionEyebrow(
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(bottom = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .width(16.dp)
                .height(2.dp)
                .background(EmeraldGreen, RoundedCornerShape(1.dp))
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = EmeraldGreen,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.2.sp
        )
    }
}

@Composable
fun StatusBadge(
    status: EnquiryStatus,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor) = when (status) {
        EnquiryStatus.NEW -> Color(0xFFEFF6FF) to Color(0xFF1D4ED8)
        EnquiryStatus.IN_PROGRESS -> Color(0xFFFEF3C7) to AlertAmber
        EnquiryStatus.CLOSED -> Color(0xFFE8F5E9) to DeepForestGreen
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .testTag("status_badge_${status.name.lowercase()}")
            .border(1.dp, textColor.copy(alpha = 0.25f), RoundedCornerShape(12.dp))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(textColor)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = status.label,
                color = textColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun HardwareStatusIndicator(
    isConnected: Boolean,
    modifier: Modifier = Modifier
) {
    val dotColor = if (isConnected) EmeraldGreen else Color(0xFFEF4444)
    val text = if (isConnected) "Controller Online" else "Awaiting Hardware Connection"
    val containerBg = if (isConnected) Color(0xFFE8F5E9) else Color(0xFFFEE2E2)

    Surface(
        color = containerBg,
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
            .testTag("hardware_status_indicator")
            .border(1.dp, dotColor.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(dotColor)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = text,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = if (isConnected) DeepForestGreen else Color(0xFF991B1B)
            )
        }
    }
}
