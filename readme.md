# TodoAppV2

A modern Android application for managing tasks and categories, built with Material Design principles and following MVVM architecture.

## Features

- **Task Management**
  - Create, edit, and delete tasks
  - Mark tasks as complete/incomplete
  - Set task priorities (Low, Medium, High)
  - Add due dates to tasks
  - Add descriptions to tasks

- **Category Management**
  - Create and manage custom categories
  - Assign multiple categories to tasks
  - View tasks by category

- **Task Organization**
  - View all tasks
  - Filter tasks by status (Active/Completed)
  - Sort tasks by priority and due date
  - Search functionality

- **Modern UI/UX**
  - Material Design components
  - Bottom navigation for easy access
  - Floating Action Button for quick task creation
  - Intuitive category selection with chips
  - Responsive layout

## Technical Details

### Architecture
- MVVM (Model-View-ViewModel) architecture
- Room Database for local storage
- LiveData for reactive UI updates
- ViewModel for managing UI-related data

### Key Components
- **Activities**
  - MainActivity: Main task list and navigation
  - AddTodoActivity: Create new tasks
  - EditTodoActivity: Modify existing tasks
  - CategoryManagementActivity: Manage categories

- **Adapters**
  - TodoAdapter: Handles task list display
  - CategoryAdapter: Manages category list display

- **Models**
  - Todo: Task entity
  - Category: Category entity
  - TodoWithCategories: Relationship entity

### Dependencies
- AndroidX Core Libraries
- Material Design Components
- Room Database
- ViewModel and LiveData
- RecyclerView
- CardView

## Getting Started

### Prerequisites
- Android Studio Arctic Fox or newer
- Android SDK 21 or higher
- Gradle 7.0 or higher

### Installation
1. Clone the repository
```bash
git clone https://github.com/yourusername/TodoAppV2.git
```

2. Open the project in Android Studio

3. Build and run the application

## Usage

### Creating a Task
1. Tap the Floating Action Button (+)
2. Enter task details:
   - Title (required)
   - Description (optional)
   - Due date
   - Priority level
   - Categories
3. Tap "Save"

### Managing Categories
1. Navigate to Categories using bottom navigation
2. Use the + button to add new categories
3. Tap on a category to edit
4. Use the delete button to remove categories

### Filtering Tasks
- Use the bottom navigation to switch between:
  - All tasks
  - Active tasks
  - Completed tasks

### Completing Tasks
- Tap the checkbox next to a task to mark it as complete/incomplete

### Deleting Tasks
- Tap the delete icon on a task to remove it
- Use the menu option to delete all tasks

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details

## Acknowledgments

- Material Design Guidelines
- Android Jetpack Components
- Room Database Documentation
