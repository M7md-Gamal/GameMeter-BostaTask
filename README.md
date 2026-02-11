# GameMeter

**GameMeter** is a modern Android application designed for browsing video games using the RAWG API. It serves as a demonstration of Clean Architecture, MVI (Model-View-Intent) pattern, and modern Android development practices using Jetpack Compose.

## 📱 Features

*   **Browse Games:** View a paginated list of video games fetched from the RAWG API.
*   **Genre Filtering:** Filter games by category using an interactive chip selector.
*   **Game Details:** Access detailed information about specific games, including release dates, ratings, and descriptions.
*   **Local Search:** Filter currently loaded games instantly using a local search mechanism.
*   **Offline Support:** Robust offline capabilities using Room database as the single source of truth.
*   **State Handling:** distinct UI states for Loading, Error (with retry), and Empty results.
*   **Dark/Light Mode:** Fully supports system theme settings.

## 🛠 Tech Stack

The application is built using the following modern Android technologies:

*   **Language:** [Kotlin](https://kotlinlang.org/)
*   **UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material 3)
*   **Architecture:** Clean Architecture + MVI (Model-View-Intent)
*   **Asynchrony:** [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) & [Flow](https://kotlinlang.org/docs/flow.html)
*   **Networking:** [Ktor Client](https://ktor.io/)
    *   Content Negotiation
    *   Logging
    *   Serialization (Kotlinx Serialization)
*   **Dependency Injection:** [Koin](https://insert-koin.io/)
*   **Image Loading:** [Coil](https://coil-kt.github.io/coil/)
*   **Local Storage:** [Room](https://developer.android.com/training/data-storage/room)
*   **Pagination:** [Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3) (with RemoteMediator)

## 🏗 Architecture & Rationale

This project adheres to **Clean Architecture** principles to ensure separation of concerns, testability, and maintainability.

### Layers
1.  **Domain Layer:** Contains business logic and UseCases. It is purely Kotlin and independent of Android frameworks.
2.  **Data Layer:** Handles data retrieval from Remote (API) and Local (Room) sources. It implements the repositories defined in the Domain layer.
3.  **Presentation Layer:** Contains UI components (Composables) and ViewModels. It observes data from the Domain layer and maps it to UI states.

### Key Decisions
*   **MVI & UDF:** This project uses the **Model-View-Intent (MVI)** pattern along with **Unidirectional Data Flow (UDF)**.
    *   **Model (State)**: A single, immutable source of truth for the UI state.
    *   **View**: Jetpack Compose functions that observe the state and render the UI.
    *   **Intent (Action)**: User actions (e.g., search query updates, category selection) are sent as intents to the ViewModel, ensuring a predictable and traceable state management flow.
*   **Ktor vs Retrofit:** Ktor was chosen for its lightweight nature, native Kotlin support, and potential for Multiplatform (KMP) expansion in the future.
*   **Koin vs Hilt:** Koin is used for Dependency Injection due to its simplicity, lack of code generation (annotation processing), and ease of setup in pure Kotlin projects.
*   **Single Source of Truth (SSOT):** The repository coordinates data. When data is fetched from the network, it is persisted in the Room database. The UI always observes the database, ensuring that the user sees consistent data even when offline. `RemoteMediator` handles this synchronization automatically with Paging 3.

## 📝 Assumptions & Shortcuts

*   **Genre Selection:** For the scope of this assignment, the game list fetches a default set of popular games or a specific genre if hardcoded, rather than a full dynamic genre selector UI, focusing on the core list browsing experience.

*   **API Key:** The project assumes a valid RAWG API key is configured.

## 🚀 Setup & Build Instructions

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/M7md-Gamal/GameMeter
    ```
2.  **Open in Android Studio:**
    Open the project folder in the latest version of Android Studio (Koala or later recommended).
3.  **API Key Configuration:**
    *   Obtain an API key from [RAWG.io](https://rawg.io/apidocs).
    *   Add your API key to `local.properties`:
        ```properties
        API_KEY="your_api_key_here"
        ```
        *(Note: Ensure your BuildConfig is set up to read this, or hardcode it temporarily for testing if strictly necessary per the provided source code)*.
4.  **Build and Run:**
    *   Sync Gradle files.
    *   Run the app on an Emulator or Physical device via Android Studio, or use the command line:
    ```bash
    ./gradlew installDebug
    ```

## 📦 Deliverables Checklist

*   [x] **Tech Stack:** Confirmed usage of Kotlin, Compose, Ktor, Koin, Room.
*   [x] **Architecture:** Implemented MVI with Clean Architecture.
*   [x] **Pagination:** Implemented using Paging 3.
*   [x] **Offline Support:** Implemented using Room caching.
*   [x] **UI:** Material 3 design with handling for various states.
