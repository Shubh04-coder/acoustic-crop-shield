package com.example.model

enum class EnquiryStatus(val label: String) {
    NEW("New"),
    IN_PROGRESS("In Progress"),
    CLOSED("Closed")
}

data class Enquiry(
    val id: String,
    val name: String,
    val email: String,
    val interest: String,
    val message: String,
    val status: EnquiryStatus = EnquiryStatus.NEW,
    val createdAt: Long = System.currentTimeMillis()
)

enum class FrequencyBand(val title: String, val range: String, val targetPest: String) {
    ULTRASONIC("Ultrasonic Band", "18 - 25 kHz", "Rodents, wild boar & deer"),
    SONIC_BIOACOUSTIC("Sonic Bioacoustic", "2 - 8 kHz", "Crows, starlings & birds"),
    FREQUENCY_SWEEP("Multi-Sweep Mode", "2 - 25 kHz Sweep", "Variable habituation prevention")
}

data class DeviceTelemetry(
    val isConnected: Boolean = false,
    val isSimulationMode: Boolean = false,
    val batteryPercent: Int = 88,
    val solarInputWatts: Double = 14.5,
    val selectedBand: FrequencyBand = FrequencyBand.FREQUENCY_SWEEP,
    val pulseIntervalSeconds: Int = 30,
    val pulseDurationSeconds: Int = 5,
    val decibelLevel: Int = 92,
    val lastPingTimestamp: Long = System.currentTimeMillis(),
    val totalDeterrentPulses: Int = 142,
    val isPulsingActive: Boolean = false
)

data class ProjectFile(
    val id: String,
    val name: String,
    val sizeBytes: Long,
    val mimeType: String,
    val uploadedAt: Long = System.currentTimeMillis(),
    val category: String = "Field Report"
)

data class UserProfile(
    val id: String = "usr-01",
    val email: String = "farmer@cropdefense.org",
    val role: String = "admin" // "user" or "admin"
)

data class FaqItem(
    val question: String,
    val answer: String
)

data class SystemFeature(
    val index: String,
    val title: String,
    val description: String
)

data class ProcessStep(
    val stepNumber: String,
    val title: String,
    val description: String
)
