# Lichu
Lichu is a task management and calendar application designed to help users organize their tasks and deadlines efficiently.

### Core Features:

1. **Task Management:**
   - Users can create, edit, and delete tasks.
   - Each task has attributes such as content, start time, end time (deadline), completion status, and a category association.

2. **Category Management:**
   - Tasks can be categorized for better organization.
   - Users can create, edit, and delete categories.
   - Categories help in filtering tasks, making it easier for users to focus on specific areas of their life or work.

3. **Calendar Integration:**
   - The app includes a calendar view, allowing users to visualize their tasks on a calendar.
   - Users can see tasks that are due on specific dates, providing a clear overview of upcoming deadlines.

4. **Data Persistence:**
   - The app uses Room Database for data persistence, ensuring that tasks and categories are saved locally on the device.
   - A `Converter` class is used to handle the conversion of `LocalDate` objects to and from the database.

5. **User Interface:**
   - The app employs Material Design components and Compose UI toolkit for a modern and responsive user interface.
   - Features like navigation bars, floating action buttons, and dropdown menus enhance user interaction.

### Purpose:

The primary purpose of this app is to help users manage their tasks and deadlines more effectively. By providing a clear overview of upcoming tasks, categorizing tasks for better organization, and alerting users about approaching deadlines, the app aims to enhance productivity and ensure that users can stay on top of their responsibilities. The integration of a calendar view and the use of notifications are particularly useful for users who have a busy schedule and need a reliable way to manage their time and tasks.

## Additional Dependencies Used in Lichu

Lichu utilizes these dependencies:
- **[Kizito Nwose's Compose Calendar:](https://github.com/kizitonwose/Calendar)** Libraries for integrating calendar functionality with Jetpack Compose.
- **[Desugar JDK Libraries:](https://developer.android.com/studio/write/java8-support#library-desugaring)** Provides support for Java 8+ APIs on lower API levels.

AndroidX:
- **[Room:](https://developer.android.com/jetpack/androidx/releases/room)** Persistence library for managing SQLite databases.
- **[Google Fonts:](https://developer.android.com/reference/kotlin/androidx/compose/ui/text/googlefonts/package-summary.html)** Provides access to Google Fonts.
- **[Lifecycle LiveData KTX:](https://developer.android.com/topic/libraries/architecture/livedata)** Kotlin extensions for LiveData.
- **[Navigation Compose:](https://developer.android.com/jetpack/androidx/releases/navigation)** Navigation component for Jetpack Compose.
- **[Runtime LiveData:](https://developer.android.com/jetpack/androidx/releases/compose-runtime)** LiveData support for Compose runtime.
- **[Lifecycle ViewModel Compose:](https://developer.android.com/jetpack/androidx/releases/lifecycle)** ViewModel support for Jetpack Compose.
