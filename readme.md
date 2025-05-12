# To Do App v2

A modern Android To-Do application with advanced productivity features, built using Room, LiveData, and Material Design.

## Features

- **Task Management**
  - Add, edit, and delete tasks with title, description, due date, priority, and categories.
  - Mark tasks as completed or active.
  - Bulk delete all tasks.

- **Category Management**
  - Create, edit, and delete custom categories.
  - Assign multiple categories to each task.

- **Reminders**
  - Set reminders for tasks with notification support.
  - Reminders persist across device reboots.

- **Focus Mode**
  - Set a focus timer for deep work sessions.
  - Animated countdown timer with sound notification on completion.
  - Overview of daily social app usage (YouTube, Gmail, Facebook, etc.), aggregated and visualized.

- **Advanced Filtering**
  - Filter tasks by title, description, due date, priority, reminder time, and category.
  - Flexible filter dialog for easy multi-criteria search.

- **Visual Priority Indicators**
  - Each task displays a colored bar: green (Low), amber (Medium), red (High) for quick priority recognition.

- **Material Design**
  - Clean, modern UI with Material Components and smooth user experience.

## Setup Instructions

1. **Clone the repository**
   ```sh
   git clone <repo-url>
   cd ToDoAppv2
   ```
2. **Open in Android Studio**
   - Open the project folder in Android Studio.
   - Let Gradle sync and download dependencies.
3. **Configure Google Services (optional)**
   - If using Firebase features, add your `google-services.json` to `app/`.
4. **Build and Run**
   - Connect your device or start an emulator.
   - Click Run (▶️) in Android Studio.

## Code Structure
- `app/src/main/java/com/example/todoappv2/` — Main activities and logic
- `app/src/main/java/com/example/todoappv2/adapter/` — RecyclerView adapters
- `app/src/main/java/com/example/todoappv2/model/` — Data models (Todo, Category, etc.)
- `app/src/main/java/com/example/todoappv2/dao/` — Room DAOs
- `app/src/main/java/com/example/todoappv2/repository/` — Data repositories
- `app/src/main/java/com/example/todoappv2/viewmodel/` — ViewModels
- `app/src/main/res/layout/` — XML layouts
- `app/src/main/res/values/` — Colors, strings, styles

## Screenshots
_Add screenshots here to showcase the UI and features._

## License
MIT or specify your license here.
