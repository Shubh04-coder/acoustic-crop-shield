package com.example.data

import com.example.model.DeviceTelemetry
import com.example.model.Enquiry
import com.example.model.EnquiryStatus
import com.example.model.FaqItem
import com.example.model.FrequencyBand
import com.example.model.ProcessStep
import com.example.model.ProjectFile
import com.example.model.SystemFeature
import com.example.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

class CropShieldRepository {

    val features = listOf(
        SystemFeature(
            index = "01",
            title = "Acoustic deterrence",
            description = "A sound-based approach designed to discourage unwanted bird and animal visits around crops."
        ),
        SystemFeature(
            index = "02",
            title = "Electronics-led design",
            description = "A practical prototype concept combining electronic control with an acoustic output system."
        ),
        SystemFeature(
            index = "03",
            title = "Monitoring-ready",
            description = "A dashboard foundation for real device readings when a controller and telemetry link are connected."
        ),
        SystemFeature(
            index = "04",
            title = "Farmer-focused",
            description = "Designed around practical crop-protection needs, with a focus on usability and responsible deployment."
        )
    )

    val processSteps = listOf(
        ProcessStep(
            stepNumber = "01",
            title = "Observe the field",
            description = "Identify the crop, target animals, field conditions, and the situations where crop damage occurs."
        ),
        ProcessStep(
            stepNumber = "02",
            title = "Control unit",
            description = "A controller can manage the prototype's operating logic once the selected electronics are integrated."
        ),
        ProcessStep(
            stepNumber = "03",
            title = "Acoustic output",
            description = "The system produces a planned acoustic output intended to discourage unwanted visits, subject to testing."
        ),
        ProcessStep(
            stepNumber = "04",
            title = "Review and improve",
            description = "Future telemetry can help record device status and evaluate results after hardware integration."
        )
    )

    val faqs = listOf(
        FaqItem(
            question = "What is Acoustic Crop Shield?",
            answer = "It is a developing crop-protection prototype concept that uses acoustic deterrence to help discourage birds and other crop-raiding animals."
        ),
        FaqItem(
            question = "Does the app show live device data?",
            answer = "Live readings appear once a compatible field controller and communication link are connected. The dashboard features a real-time simulator mode to preview telemetry and deterrence triggers before field hardware deployment."
        ),
        FaqItem(
            question = "Will it protect every crop from every animal?",
            answer = "No system can promise that. Results depend on the species, crop, environment, sound design, placement, and animal habituation. Field testing is necessary."
        ),
        FaqItem(
            question = "Can I request a demonstration or collaboration?",
            answer = "Yes. Use the enquiry form and share what you are interested in, such as a prototype demo, collaboration, or technical discussion."
        )
    )

    private val _enquiries = MutableStateFlow<List<Enquiry>>(
        listOf(
            Enquiry(
                id = "ENQ-101",
                name = "Dr. Marcus Vance",
                email = "m.vance@agri-research.org",
                interest = "Academic project",
                message = "Interested in evaluating ultrasonic frequency attenuation across 5-hectare corn canopies.",
                status = EnquiryStatus.NEW,
                createdAt = System.currentTimeMillis() - 86400000L * 2
            ),
            Enquiry(
                id = "ENQ-102",
                name = "Elena Rostova",
                email = "elena@vineyardholdings.com",
                interest = "Field testing",
                message = "We have starling damage in block 4 grapevines. Would like to test the bioacoustic speaker node.",
                status = EnquiryStatus.IN_PROGRESS,
                createdAt = System.currentTimeMillis() - 86400000L * 4
            )
        )
    )
    val enquiries: StateFlow<List<Enquiry>> = _enquiries.asStateFlow()

    private val _telemetry = MutableStateFlow(DeviceTelemetry())
    val telemetry: StateFlow<DeviceTelemetry> = _telemetry.asStateFlow()

    private val _projectFiles = MutableStateFlow<List<ProjectFile>>(
        listOf(
            ProjectFile(
                id = "FILE-01",
                name = "acoustic_spectrum_v1.pdf",
                sizeBytes = 2411724,
                mimeType = "application/pdf",
                category = "Technical Spec"
            ),
            ProjectFile(
                id = "FILE-02",
                name = "field_deployment_map.png",
                sizeBytes = 4194304,
                mimeType = "image/png",
                category = "Site Survey"
            )
        )
    )
    val projectFiles: StateFlow<List<ProjectFile>> = _projectFiles.asStateFlow()

    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    fun submitEnquiry(name: String, email: String, interest: String, message: String): Result<Enquiry> {
        if (name.isBlank() || email.isBlank() || message.isBlank()) {
            return Result.failure(IllegalArgumentException("Please complete all required fields."))
        }
        val enquiry = Enquiry(
            id = "ENQ-" + UUID.randomUUID().toString().take(6).uppercase(),
            name = name.trim(),
            email = email.trim(),
            interest = interest,
            message = message.trim(),
            status = EnquiryStatus.NEW
        )
        _enquiries.update { listOf(enquiry) + it }
        return Result.success(enquiry)
    }

    fun updateEnquiryStatus(id: String, newStatus: EnquiryStatus) {
        _enquiries.update { list ->
            list.map { if (it.id == id) it.copy(status = newStatus) else it }
        }
    }

    fun deleteEnquiry(id: String) {
        _enquiries.update { list -> list.filterNot { it.id == id } }
    }

    fun toggleHardwareConnection(connected: Boolean) {
        _telemetry.update {
            it.copy(
                isConnected = connected,
                lastPingTimestamp = System.currentTimeMillis()
            )
        }
    }

    fun setFrequencyBand(band: FrequencyBand) {
        _telemetry.update { it.copy(selectedBand = band) }
    }

    fun updateTiming(intervalSeconds: Int, durationSeconds: Int) {
        _telemetry.update {
            it.copy(
                pulseIntervalSeconds = intervalSeconds,
                pulseDurationSeconds = durationSeconds
            )
        }
    }

    fun triggerPulse() {
        _telemetry.update {
            it.copy(
                isPulsingActive = true,
                totalDeterrentPulses = it.totalDeterrentPulses + 1,
                lastPingTimestamp = System.currentTimeMillis()
            )
        }
    }

    fun clearPulseActive() {
        _telemetry.update { it.copy(isPulsingActive = false) }
    }

    fun uploadSimulatedFile(fileName: String, category: String) {
        val newFile = ProjectFile(
            id = "FILE-" + UUID.randomUUID().toString().take(5).uppercase(),
            name = fileName,
            sizeBytes = (1024 * 1024 * 1.5).toLong(),
            mimeType = if (fileName.endsWith(".pdf")) "application/pdf" else "image/jpeg",
            category = category
        )
        _projectFiles.update { listOf(newFile) + it }
    }

    fun deleteFile(id: String) {
        _projectFiles.update { list -> list.filterNot { it.id == id } }
    }

    fun toggleUserRole() {
        _userProfile.update {
            val newRole = if (it.role == "admin") "user" else "admin"
            it.copy(role = newRole)
        }
    }

    fun signIn(email: String, role: String) {
        _userProfile.update { it.copy(email = email, role = role) }
    }

    fun signOut() {
        _userProfile.update { it.copy(email = "guest@cropdefense.org", role = "user") }
    }
}
