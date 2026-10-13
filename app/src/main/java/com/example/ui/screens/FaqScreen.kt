package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.FaqItem
import com.example.ui.components.SectionEyebrow
import com.example.ui.theme.BorderTint
import com.example.ui.theme.DeepForestGreen
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.MutedText
import com.example.ui.viewmodel.AppDestination
import com.example.ui.viewmodel.CropShieldUiState

@Composable
fun FaqScreen(
    state: CropShieldUiState,
    onNavigate: (AppDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val expandedMap = remember {
        mutableStateMapOf<Int, Boolean>().apply {
            put(0, true) // First item open by default like original website
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
            .testTag("faq_screen_container")
    ) {
        SectionEyebrow(text = "Good to Know")
        Text(
            text = "Questions, answered.",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Important scientific and operational details regarding the acoustic crop protection concept.",
            style = MaterialTheme.typography.bodyLarge,
            color = MutedText,
            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
        )

        state.faqs.forEachIndexed { index, item ->
            val isExpanded = expandedMap[index] == true
            FaqAccordionCard(
                index = index,
                item = item,
                isExpanded = isExpanded,
                onToggle = { expandedMap[index] = !isExpanded }
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BorderTint, RoundedCornerShape(12.dp))
                .testTag("faq_contact_card")
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Still have questions?",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = DeepForestGreen
                )
                Text(
                    text = "Reach out to our agronomy and electronics engineering team with your specific crop scenario.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MutedText,
                    modifier = Modifier.padding(vertical = 6.dp)
                )
                Button(
                    onClick = { onNavigate(AppDestination.ENQUIRY) },
                    colors = ButtonDefaults.buttonColors(containerColor = DeepForestGreen),
                    modifier = Modifier.testTag("faq_contact_button")
                ) {
                    Text(text = "Contact the Team")
                }
            }
        }
    }
}

@Composable
private fun FaqAccordionCard(
    index: Int,
    item: FaqItem,
    isExpanded: Boolean,
    onToggle: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, BorderTint, RoundedCornerShape(12.dp))
            .testTag("faq_card_$index")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Surface(
                onClick = onToggle,
                color = MaterialTheme.colorScheme.surface
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.question,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = DeepForestGreen,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = onToggle,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = if (isExpanded) Icons.Default.ExpandMore else Icons.Default.ChevronRight,
                            contentDescription = if (isExpanded) "Collapse" else "Expand",
                            tint = DeepForestGreen
                        )
                    }
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 10.dp)) {
                    Text(
                        text = item.answer,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MutedText,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}
