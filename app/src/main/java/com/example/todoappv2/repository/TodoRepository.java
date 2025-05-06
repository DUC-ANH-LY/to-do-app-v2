package com.example.todoappv2.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.example.todoappv2.dao.TodoDao;
import com.example.todoappv2.database.TodoDatabase;
import com.example.todoappv2.model.Category;
import com.example.todoappv2.model.Todo;
import com.example.todoappv2.model.TodoCategoryCrossRef;
import com.example.todoappv2.model.TodoWithCategories;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TodoRepository {
    private TodoDao todoDao;
    private LiveData<List<TodoWithCategories>> allTodos;
    private LiveData<List<TodoWithCategories>> activeTodos;
    private LiveData<List<TodoWithCategories>> completedTodos;
    private ExecutorService executorService;

    public TodoRepository(Application application) {
        TodoDatabase database = TodoDatabase.getInstance(application);
        todoDao = database.todoDao();
        allTodos = todoDao.getTodosWithCategories();
        activeTodos = todoDao.getActiveTodosWithCategories();
        completedTodos = todoDao.getCompletedTodosWithCategories();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(Todo todo, List<Category> categories) {
        executorService.execute(() -> {
            // First insert the todo
            long todoId = todoDao.insert(todo);
            
            // Then handle each category
            for (Category category : categories) {
                // Check if category exists
                Category existingCategory = todoDao.getCategoryByName(category.getName());
                long categoryId;
                
                if (existingCategory == null) {
                    // If category doesn't exist, insert it
                    categoryId = todoDao.insertCategory(category);
                } else {
                    // If category exists, use its ID
                    categoryId = existingCategory.getId();
                }
                
                // Create the relationship
                todoDao.insertTodoCategoryCrossRef(new TodoCategoryCrossRef((int) todoId, (int) categoryId));
            }
        });
    }

    public void update(Todo todo, List<Category> categories) {
        executorService.execute(() -> {
            todoDao.update(todo);
            // Delete existing category relationships
            todoDao.deleteTodoCategoryCrossRefs(todo.getId());
            // Add new category relationships
            for (Category category : categories) {
                // Check if category exists
                Category existingCategory = todoDao.getCategoryByName(category.getName());
                long categoryId;
                
                if (existingCategory == null) {
                    // If category doesn't exist, insert it
                    categoryId = todoDao.insertCategory(category);
                } else {
                    // If category exists, use its ID
                    categoryId = existingCategory.getId();
                }
                
                // Create the relationship
                todoDao.insertTodoCategoryCrossRef(new TodoCategoryCrossRef(todo.getId(), (int) categoryId));
            }
        });
    }

    public void delete(Todo todo) {
        executorService.execute(() -> todoDao.delete(todo));
    }

    public void deleteAll() {
        executorService.execute(() -> todoDao.deleteAll());
    }

    public LiveData<List<TodoWithCategories>> getAllTodos() {
        return allTodos;
    }

    public LiveData<List<TodoWithCategories>> getActiveTodos() {
        return activeTodos;
    }

    public LiveData<List<TodoWithCategories>> getCompletedTodos() {
        return completedTodos;
    }

    public LiveData<Todo> getTodoById(int id) {
        return todoDao.getTodoById(id);
    }

    public LiveData<TodoWithCategories> getTodoWithCategories(int id) {
        return todoDao.getTodoWithCategories(id);
    }
} 