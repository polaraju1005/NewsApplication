# NewsApp - Jetpack Compose MVVM

## Project Description
NewsApplication is an Android application built with **Jetpack Compose** and **MVVM architecture**. It fetches the latest news articles using the **NewsAPI** and displays them in a sleek and intuitive user interface. The app allows users to read articles within an in-app WebView, save articles for offline access, and manage saved content easily.

This application ensures seamless performance with an offline-first approach, leveraging local data storage to provide a complete news experience regardless of connectivity status.

---

## Steps to Set Up the Project

1. **Clone the Repository**
   ```bash
   git clone https://github.com/polaraju1005/NewsApplication.git
   cd NewsApplication
   ```

2. **Obtain an API Key**
   - Register at [NewsAPI](https://newsapi.org/) and create a free account.
   - Generate an API key from the dashboard.

3. **Add API Key**
   - Open the `local.properties` file (or create it if missing).
   - Add your API key:
     ```properties
     NEWS_API_KEY="your_api_key"
     ```

4. **Configure Environment**
   - Install [Android Studio](https://developer.android.com/studio).
   - Open the project and sync the Gradle files.

5. **Run the App**
   - Connect an Android 11+ emulator or device.
   - Build and run the project from Android Studio.

---

## Libraries and Their Purposes

| Library             | Purpose                                                                                  |
|---------------------|------------------------------------------------------------------------------------------|
| **Retrofit**        | For making HTTP requests to the NewsAPI and fetching JSON data.                          |
| **Moshi**           | For converting JSON responses into Kotlin data classes.                                  |
| **Room**            | To implement the local database for saving articles and providing offline access.        |
| **Coil**            | For efficient loading and caching of article images.                                     |
| **Jetpack Compose** | For building modern, declarative user interfaces with composables.                       |
| **Hilt**            | For dependency injection to maintain a clean and testable codebase.                      |
| **StateFlow**       | For reactive state management between the ViewModel and Compose UI.                      |
| **Navigation Component** | For seamless navigation between the news list, article details, and saved articles screens. |

---

## Explanation of Architecture and Design Choices

**1. MVVM Architecture**  
- Ensures a **clean separation of concerns**:
  - **Model**: Data classes for API responses and database entities.
  - **ViewModel**: Handles business logic, state management, and data flow to the UI.
  - **View (UI)**: Built with Jetpack Compose, the UI observes reactive state flows.

**2. Repository Pattern**  
- Acts as a **single source of truth**, mediating between:
  - Remote data from the **NewsAPI**.
  - Local data from the **Room database**.

**3. Offline-First Approach**  
- Cached articles allow users to access previously loaded data without an internet connection.
- Saved articles stored in Room enable offline reading.

**4. Jetpack Compose**  
- Declarative UI makes building dynamic layouts simpler and more scalable.
- Ensures efficient and responsive UIs, leveraging Kotlin.

**5. Dependency Injection with Hilt**  
- Simplifies object creation and lifecycle management.
- Reduces boilerplate code and improves testability.

**6. Modular Design**  
- Code is organized into features, layers, and modules:
  - **Data Layer**: Handles API and database operations.
  - **Domain Layer**: Implements business logic and mediates data access.
  - **UI Layer**: Composables and navigation for user interaction.

---

Feel free to contribute or raise issues in the GitHub repository. Your feedback is always appreciated!

