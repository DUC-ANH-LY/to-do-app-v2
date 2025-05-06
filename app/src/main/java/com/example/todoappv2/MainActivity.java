package com.example.todoappv2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoappv2.adapter.TodoAdapter;
import com.example.todoappv2.model.Todo;
import com.example.todoappv2.model.TodoWithCategories;
import com.example.todoappv2.viewmodel.TodoViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TodoViewModel todoViewModel;
    private TodoAdapter adapter;
    private List<TodoWithCategories> allTodos = new ArrayList<>();
    private int currentFilter = R.id.navigation_all;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize ViewModel first
        todoViewModel = new ViewModelProvider(this).get(TodoViewModel.class);

        // Set up RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new TodoAdapter();
        recyclerView.setAdapter(adapter);

        // Set up toolbar
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setTitle(R.string.app_name);

        // Set up FAB
        FloatingActionButton fab = findViewById(R.id.fabAddTodo);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddTodoActivity.class);
            startActivity(intent);
        });

        // Set up bottom navigation
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_categories) {
                startActivity(new Intent(MainActivity.this, CategoryManagementActivity.class));
                return false; // Don't change the selected item
            }
            
            currentFilter = itemId;
            filterTodos();
            return true;
        });

        // Set up adapter click listeners
        adapter.setOnItemClickListener(todoWithCategories -> {
            Todo todo = todoWithCategories.getTodo();
            Intent intent = new Intent(MainActivity.this, EditTodoActivity.class);
            intent.putExtra(EditTodoActivity.EXTRA_ID, todo.getId());
            intent.putExtra(EditTodoActivity.EXTRA_TITLE, todo.getTitle());
            intent.putExtra(EditTodoActivity.EXTRA_DESCRIPTION, todo.getDescription());
            intent.putExtra(EditTodoActivity.EXTRA_DUE_DATE, todo.getDueDate().getTime());
            intent.putExtra(EditTodoActivity.EXTRA_PRIORITY, todo.getPriority());
            intent.putExtra(EditTodoActivity.EXTRA_COMPLETED, todo.isCompleted());
            startActivity(intent);
        });

        adapter.setOnCheckBoxClickListener(todoWithCategories -> {
            Todo todo = todoWithCategories.getTodo();
            todo.setCompleted(!todo.isCompleted());
            todoViewModel.update(todo, todoWithCategories.getCategories());
        });

        adapter.setOnDeleteClickListener(todoWithCategories -> {
            showDeleteConfirmationDialog(todoWithCategories);
        });

        // Observe todos after setting up all UI components
        todoViewModel.getAllTodos().observe(this, todos -> {
            allTodos = todos;
            filterTodos();
        });
    }

    private void showDeleteConfirmationDialog(TodoWithCategories todoWithCategories) {
        new AlertDialog.Builder(this)
            .setTitle(R.string.delete_todo)
            .setMessage(getString(R.string.delete_todo_confirmation, todoWithCategories.getTodo().getTitle()))
            .setPositiveButton(R.string.delete, (dialog, which) -> {
                todoViewModel.delete(todoWithCategories.getTodo());
                Toast.makeText(this, R.string.todo_deleted, Toast.LENGTH_SHORT).show();
            })
            .setNegativeButton(R.string.cancel, null)
            .show();
    }

    private void filterTodos() {
        List<TodoWithCategories> filteredTodos = new ArrayList<>();
        for (TodoWithCategories todoWithCategories : allTodos) {
            Todo todo = todoWithCategories.getTodo();
            if (currentFilter == R.id.navigation_all ||
                (currentFilter == R.id.navigation_active && !todo.isCompleted()) ||
                (currentFilter == R.id.navigation_completed && todo.isCompleted())) {
                filteredTodos.add(todoWithCategories);
            }
        }
        adapter.submitList(filteredTodos);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_delete_all) {
            showDeleteAllConfirmationDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteAllConfirmationDialog() {
        new AlertDialog.Builder(this)
            .setTitle(R.string.delete_all)
            .setMessage(R.string.delete_all_confirmation)
            .setPositiveButton(R.string.delete, (dialog, which) -> {
                todoViewModel.deleteAll();
                Toast.makeText(this, R.string.all_todos_deleted, Toast.LENGTH_SHORT).show();
            })
            .setNegativeButton(R.string.cancel, null)
            .show();
    }
}