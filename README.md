# Acoustic Crop Shield - Android Application

Acoustic Crop Shield is an Android application built with **Kotlin** and **Jetpack Compose (Material 3)**. It implements an acoustic crop-protection concept designed to deter birds and pest animals through targeted sound frequencies and practical telemetry.

## Key Features

- **Concept Showcase**: Product overview, trust strip, electronic design highlights, and multi-step implementation flow.
- **Digital Control Centre**: Real-time hardware telemetry dashboard, acoustic frequency band switcher (Ultrasonic 18–25 kHz, Sonic Bioacoustic 2–8 kHz, Multi-Sweep), interval scheduling, and test burst triggers (92 dB sound burst simulation).
- **Grower Enquiries**: Interactive inquiry submission with categorized interest topics (Field testing, Product demo, Academic research, Technical collaboration).
- **Workspace & File Hub**: Role-based access (Farmer User vs. Field Admin) and private project document management (specifications, site maps, acoustic logs).
- **Admin Management**: Dedicated dashboard for reviewing, filtering (New, In Progress, Closed), and updating customer and trial enquiries.
- **Knowledge Base (FAQ)**: Expandable accordion items explaining acoustic deterrence science, hardware integration, and field validation expectations.

## Architecture & Tech Stack

- **UI Framework**: Jetpack Compose with Material Design 3
- **Architecture**: Model-View-ViewModel (MVVM) with reactive `StateFlow`
- **Application ID**: `com.aistudio.cropshield.kxmpzq`
- **Adaptive Icon**: Custom adaptive launcher icon with green agricultural shield and acoustic waveforms
