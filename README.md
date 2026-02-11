# 📱 Public – Android Take-Home Assessment

**Candidate:** Dilermando Sikora
**Position:** Sr. Android Engineer

---

## 🧩 Overview

This project implements a simple yet production-structured To-Do List application as requested in the take-home exercise.

The app allows users to:

* View all tasks
* Add a new task (title max 50 chars, description max 200 chars)
* Mark tasks as completed
* Delete tasks
* Simulate a network request when adding tasks (0.5s–2.5s delay, 75% success rate)
* Handle network failures gracefully with retry support

The project focuses on clean architecture, testability, and production-ready patterns rather than UI complexity.

---

## 🏗 Architecture

The app follows a clean MVVM structure:

```
UI (Compose)
   ↓
ViewModel (State management + validation)
   ↓
Repository (Business logic + network simulation)
   ↓
Room DAO (Local persistence)
```

### Key Design Decisions

* **Jetpack Compose** for UI
* **MVVM + StateFlow** for reactive state management
* **Room** for local storage
* **Coroutines** for async handling
* **Hilt** for dependency injection
* Network simulation isolated inside repository
* Deterministic testing through injected behavior

The goal was to keep the implementation clean, scalable, and testable.

---

## 🌐 Network Simulation

When adding a task:

* A random delay between **500ms and 2500ms** is applied
* A simulated **75% success rate**
* On failure:

  * UI shows Snackbar
  * Retry button is available
  * No task is inserted into the database

This behavior is injectable for testability.

---

## 🧪 Testing

### Unit Tests (JVM)

* ViewModel fully covered
* Repository logic covered (with mocked DAO)
* Deterministic coroutine testing
* No flaky randomness in tests

### Testing Stack

* JUnit
* MockK
* Turbine
* kotlinx-coroutines-test

---

## ✨ UI Features

* Reactive task list using `LazyColumn`
* Stable keys for proper recomposition
* Animated item placement
* Completion toggle with visual feedback
* Inline validation
* Loading indicator during simulated network call
* Snackbar with retry action
* Empty state handling

---

## 📦 Deliverables Included

At the root of the repository:

```
/README
    ├── app-release.apk
    ├── demo_video_1.mp4
    └── demo_video_2.mp4
```

These files demonstrate:

* Installation-ready APK
* Feature walkthrough
* Error handling behavior

---

## ⚙️ How to Run

1. Clone the repository
2. Open in Android Studio (AGP 8.3+)
3. Sync Gradle
4. Run on emulator or physical device

Minimum SDK: 24
Target SDK: 34

---

## 🔍 What I Focused On

As a senior engineer, I prioritized:

* Clean separation of concerns
* Testability
* Deterministic behavior
* Clear state modeling
* Proper coroutine handling
* Architecture that scales

Rather than overengineering, I aimed for clarity, correctness, and production-quality patterns.

---

## 🚀 Possible Extensions

If this were extended further:

* Pagination
* Offline-first sync strategy
* Proper remote data source abstraction
* UI tests with Compose testing
* CI configuration
* Feature modularization

---

Thank you for reviewing this submission.
I look forward to your feedback.

—
**Dilermando Sikora**
