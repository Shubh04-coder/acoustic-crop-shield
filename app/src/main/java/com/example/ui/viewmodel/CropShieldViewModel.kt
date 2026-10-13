package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.CropShieldRepository
import com.example.model.DeviceTelemetry
import com.example.model.Enquiry
import com.example.model.EnquiryStatus
import com.example.model.FaqItem
import com.example.model.FrequencyBand
import com.example.model.ProcessStep
import com.example.model.ProjectFile
import com.example.model.SystemFeature
import com.example.model.UserProfile
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class AppDestination(val route: String, val label: String) {
    HOME("home", "Concept"),
    DASHBOARD("dashboard", "Dashboard"),
    ENQUIRY("enquiry", "Contact"),
    WORKSPACE("workspace", "Workspace"),
    ADMIN("admin", "Admin"),
    FAQ("faq", "FAQ")
}

data class CropShieldUiState(
    val currentDestination: AppDestination = AppDestination.HOME,
    val features: List<SystemFeature> = emptyList(),
    val processSteps: List<ProcessStep> = emptyList(),
    val faqs: List<FaqItem> = emptyList(),
    val enquiries: List<Enquiry> = emptyList(),
    val telemetry: DeviceTelemetry = DeviceTelemetry(),
    val projectFiles: List<ProjectFile> = emptyList(),
    val userProfile: UserProfile = UserProfile(),
    val formName: String = "",
    val formEmail: String = "",
    val formInterest: String = "Product demonstration",
    val formMessage: String = "",
    val formSubmitting: Boolean = false,
    val formError: String? = null,
    val formSuccess: String? = null,
    val adminFilter: EnquiryStatus? = null,
    val infoMessage: String? = null
)

class CropShieldViewModel(
    private val repository: CropShieldRepository = CropShieldRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        CropShieldUiState(
            features = repository.features,
            processSteps = repository.processSteps,
            faqs = repository.faqs
        )
    )

    val uiState: StateFlow<CropShieldUiState> = combine(
        _uiState,
        repository.enquiries,
        repository.telemetry,
        repository.projectFiles,
        repository.userProfile
    ) { state, enquiries, telemetry, files, profile ->
        state.copy(
            enquiries = enquiries,
            telemetry = telemetry,
            projectFiles = files,
            userProfile = profile
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        _uiState.value
    )

    fun navigateTo(destination: AppDestination) {
        _uiState.update { it.copy(currentDestination = destination, infoMessage = null) }
    }

    fun onFormNameChanged(name: String) {
        _uiState.update { it.copy(formName = name, formError = null) }
    }

    fun onFormEmailChanged(email: String) {
        _uiState.update { it.copy(formEmail = email, formError = null) }
    }

    fun onFormInterestChanged(interest: String) {
        _uiState.update { it.copy(formInterest = interest) }
    }

    fun onFormMessageChanged(message: String) {
        _uiState.update { it.copy(formMessage = message, formError = null) }
    }

    fun submitEnquiry() {
        val current = _uiState.value
        _uiState.update { it.copy(formSubmitting = true, formError = null, formSuccess = null) }
        
        val result = repository.submitEnquiry(
            name = current.formName,
            email = current.formEmail,
            interest = current.formInterest,
            message = current.formMessage
        )

        result.fold(
            onSuccess = {
                _uiState.update {
                    it.copy(
                        formSubmitting = false,
                        formName = "",
                        formEmail = "",
                        formMessage = "",
                        formSuccess = "Thank you — your enquiry has been submitted. We will follow up using the email provided."
                    )
                }
            },
            onFailure = { err ->
                _uiState.update {
                    it.copy(
                        formSubmitting = false,
                        formError = err.message ?: "Failed to submit enquiry"
                    )
                }
            }
        )
    }

    fun updateEnquiryStatus(id: String, status: EnquiryStatus) {
        repository.updateEnquiryStatus(id, status)
    }

    fun deleteEnquiry(id: String) {
        repository.deleteEnquiry(id)
    }

    fun toggleHardwareConnection(connected: Boolean) {
        repository.toggleHardwareConnection(connected)
        val msg = if (connected) "Field hardware controller connected" else "Hardware connection paused"
        _uiState.update { it.copy(infoMessage = msg) }
    }

    fun setFrequencyBand(band: FrequencyBand) {
        repository.setFrequencyBand(band)
    }

    fun updateTiming(intervalSeconds: Int, durationSeconds: Int) {
        repository.updateTiming(intervalSeconds, durationSeconds)
    }

    fun triggerPulse() {
        viewModelScope.launch {
            repository.triggerPulse()
            _uiState.update { it.copy(infoMessage = "Acoustic deterrent burst activated (92 dB)") }
            delay(1500)
            repository.clearPulseActive()
        }
    }

    fun uploadSimulatedFile(fileName: String, category: String) {
        repository.uploadSimulatedFile(fileName, category)
        _uiState.update { it.copy(infoMessage = "Uploaded document: $fileName") }
    }

    fun deleteFile(id: String) {
        repository.deleteFile(id)
        _uiState.update { it.copy(infoMessage = "File removed from workspace") }
    }

    fun toggleUserRole() {
        repository.toggleUserRole()
        val role = repository.userProfile.value.role
        _uiState.update { it.copy(infoMessage = "Switched view to $role") }
    }

    fun setAdminFilter(status: EnquiryStatus?) {
        _uiState.update { it.copy(adminFilter = status) }
    }

    fun clearInfoMessage() {
        _uiState.update { it.copy(infoMessage = null) }
    }
}
