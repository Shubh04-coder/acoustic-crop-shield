package com.example.ui.screens

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Enquiry
import com.example.model.EnquiryStatus
import com.example.ui.components.SectionEyebrow
import com.example.ui.components.StatusBadge
import com.example.ui.theme.BorderTint
import com.example.ui.theme.DeepForestGreen
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.MutedText
import com.example.ui.viewmodel.AppDestination
import com.example.ui.viewmodel.CropShieldUiState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    state: CropShieldUiState,
    onNavigate: (AppDestination) -> Unit,
    onUpdateStatus: (String, EnquiryStatus) -> Unit,
    onDeleteEnquiry: (String) -> Unit,
    onSetFilter: (EnquiryStatus?) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    val filteredList = state.enquiries.filter {
        state.adminFilter == null || it.status == state.adminFilter
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
            .testTag("admin_screen_container")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            IconButton(
                onClick = { onNavigate(AppDestination.WORKSPACE) },
                modifier = Modifier.testTag("admin_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back to workspace",
                    tint = DeepForestGreen
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            SectionEyebrow(text = "Project Team Admin")
        }

        Text(
            text = "Enquiry Management",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Review incoming grower requests, prototype collaboration proposals, and update ticket resolution statuses.",
            style = MaterialTheme.typography.bodyLarge,
            color = MutedText,
            modifier = Modifier.padding(top = 4.dp, bottom = 14.dp)
        )

        // Filter chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = state.adminFilter == null,
                onClick = { onSetFilter(null) },
                label = { Text("All (${state.enquiries.size})") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = DeepForestGreen,
                    selectedLabelColor = Color.White
                ),
                modifier = Modifier.testTag("admin_filter_all")
            )

            EnquiryStatus.values().forEach { status ->
                val count = state.enquiries.count { it.status == status }
                FilterChip(
                    selected = state.adminFilter == status,
                    onClick = { onSetFilter(status) },
                    label = { Text("${status.label} ($count)") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = DeepForestGreen,
                        selectedLabelColor = Color.White
                    ),
                    modifier = Modifier.testTag("admin_filter_${status.name.lowercase()}")
                )
            }
        }

        if (filteredList.isEmpty()) {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderTint, RoundedCornerShape(12.dp))
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "No enquiries found matching filter.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MutedText
                    )
                }
            }
        } else {
            filteredList.forEach { enquiry ->
                EnquiryAdminCard(
                    enquiry = enquiry,
                    onUpdateStatus = { newStatus -> onUpdateStatus(enquiry.id, newStatus) },
                    onDelete = { onDeleteEnquiry(enquiry.id) }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EnquiryAdminCard(
    enquiry: Enquiry,
    onUpdateStatus: (EnquiryStatus) -> Unit,
    onDelete: () -> Unit
) {
    var statusDropdownExpanded by remember { mutableStateOf(false) }
    val formattedDate = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(enquiry.createdAt))

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, BorderTint, RoundedCornerShape(14.dp))
            .testTag("enquiry_admin_card_${enquiry.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = enquiry.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = DeepForestGreen
                    )
                    Text(
                        text = enquiry.email,
                        style = MaterialTheme.typography.bodyMedium,
                        color = EmeraldGreen
                    )
                }
                StatusBadge(status = enquiry.status)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                color = DeepForestGreen.copy(alpha = 0.06f),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    text = "Category: ${enquiry.interest} • $formattedDate",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = DeepForestGreen,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = enquiry.message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Status updater dropdown
                ExposedDropdownMenuBox(
                    expanded = statusDropdownExpanded,
                    onExpandedChange = { statusDropdownExpanded = !statusDropdownExpanded },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = "Status: ${enquiry.status.label}",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = statusDropdownExpanded) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EmeraldGreen,
                            focusedLabelColor = EmeraldGreen
                        ),
                        modifier = Modifier
                            .menuAnchor()
                            .testTag("enquiry_status_dropdown_${enquiry.id}")
                    )

                    ExposedDropdownMenu(
                        expanded = statusDropdownExpanded,
                        onDismissRequest = { statusDropdownExpanded = false }
                    ) {
                        EnquiryStatus.values().forEach { s ->
                            DropdownMenuItem(
                                text = { Text(s.label) },
                                onClick = {
                                    onUpdateStatus(s)
                                    statusDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.testTag("delete_enquiry_${enquiry.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Delete enquiry",
                        tint = Color(0xFFEF4444)
                    )
                }
            }
        }
    }
}
