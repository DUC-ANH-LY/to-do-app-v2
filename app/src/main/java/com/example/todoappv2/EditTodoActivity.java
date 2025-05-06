package com.example.todoappv2;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.todoappv2.model.Category;
import com.example.todoappv2.model.Todo;
import com.example.todoappv2.model.TodoWithCategories;
import com.example.todoappv2.viewmodel.TodoViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EditTodoActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "com.example.todoappv2.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.example.todoappv2.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.example.todoappv2.EXTRA_DESCRIPTION";
    public static final String EXTRA_DUE_DATE = "com.example.todoappv2.EXTRA_DUE_DATE";
    public static final String EXTRA_PRIORITY = "com.example.todoappv2.EXTRA_PRIORITY";
    public static final String EXTRA_COMPLETED = "com.example.todoappv2.EXTRA_COMPLETED";

    private TextInputEditText editTextTitle;
    private TextInputEditText editTextDescription;
    private TextInputEditText editTextDueDate;
    private AutoCompleteTextView spinnerPriority;
    private AutoCompleteTextView spinnerCategories;
    private ChipGroup chipGroupCategories;
    private TodoViewModel todoViewModel;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private int todoId;
    private List<Category> selectedCategories = new ArrayList<>();
    private List<String> categoryNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);

        dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        calendar = Calendar.getInstance();

        // Set up toolbar
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.edit_todo);

        // Initialize views
        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        editTextDueDate = findViewById(R.id.edit_text_due_date);
        spinnerPriority = findViewById(R.id.spinner_priority);
        spinnerCategories = findViewById(R.id.spinner_categories);
        chipGroupCategories = findViewById(R.id.chip_group_categories);

        // Set up priority spinner
        ArrayAdapter<CharSequence> priorityAdapter = ArrayAdapter.createFromResource(this,
                R.array.priority_levels, android.R.layout.simple_spinner_item);
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(priorityAdapter);

        // Initialize ViewModel
        todoViewModel = new ViewModelProvider(this).get(TodoViewModel.class);

        // Observe categories
        todoViewModel.getAllCategories().observe(this, categories -> {
            categoryNames.clear();
            for (Category category : categories) {
                categoryNames.add(category.getName());
            }
            ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, categoryNames);
            categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCategories.setAdapter(categoryAdapter);
        });

        // Set up category selection
        spinnerCategories.setOnItemClickListener((parent, view, position, id) -> {
            String categoryName = parent.getItemAtPosition(position).toString();
            addCategoryChip(categoryName);
            spinnerCategories.setText(""); // Clear the text after selection
        });

        // Get intent data
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            todoId = intent.getIntExtra(EXTRA_ID, -1);
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            Date dueDate = new Date(intent.getLongExtra(EXTRA_DUE_DATE, 0));
            calendar.setTime(dueDate);
            editTextDueDate.setText(dateFormat.format(dueDate));
            
            int priority = intent.getIntExtra(EXTRA_PRIORITY, 1);
            String priorityText;
            switch (priority) {
                case 3:
                    priorityText = "High";
                    break;
                case 2:
                    priorityText = "Medium";
                    break;
                default:
                    priorityText = "Low";
            }
            spinnerPriority.setText(priorityText, false);

            // Load todo with categories
            todoViewModel.getTodoWithCategories(todoId).observe(this, todoWithCategories -> {
                if (todoWithCategories != null) {
                    for (Category category : todoWithCategories.getCategories()) {
                        addCategoryChip(category.getName());
                    }
                }
            });
        }

        // Set up due date picker
        editTextDueDate.setOnClickListener(v -> showDatePicker());

        // Set up save button
        findViewById(R.id.button_save).setOnClickListener(v -> saveTodo());
    }

    private void addCategoryChip(String categoryName) {
        // Check if category is already added
        for (int i = 0; i < chipGroupCategories.getChildCount(); i++) {
            Chip chip = (Chip) chipGroupCategories.getChildAt(i);
            if (chip.getText().toString().equals(categoryName)) {
                return; // Category already exists
            }
        }

        // Create and add new chip
        Chip chip = new Chip(this);
        chip.setText(categoryName);
        chip.setCloseIconVisible(true);
        chip.setOnCloseIconClickListener(v -> {
            chipGroupCategories.removeView(chip);
            selectedCategories.removeIf(category -> category.getName().equals(categoryName));
        });

        // Add category to selected categories
        Category category = new Category(categoryName);
        selectedCategories.add(category);

        chipGroupCategories.addView(chip);
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    editTextDueDate.setText(dateFormat.format(calendar.getTime()));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void saveTodo() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String priorityStr = spinnerPriority.getText().toString();

        if (title.isEmpty()) {
            editTextTitle.setError("Title is required");
            editTextTitle.requestFocus();
            return;
        }

        if (selectedCategories.isEmpty()) {
            Toast.makeText(this, "Please select at least one category", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert priority string to integer
        int priority;
        switch (priorityStr.toLowerCase()) {
            case "high":
                priority = 3;
                break;
            case "medium":
                priority = 2;
                break;
            default:
                priority = 1; // Low
        }

        boolean isCompleted = getIntent().getBooleanExtra(EXTRA_COMPLETED, false);

        Todo todo = new Todo(title, description, calendar.getTime(), priority);
        todo.setId(todoId);
        todo.setCompleted(isCompleted);

        todoViewModel.update(todo, selectedCategories);

        Toast.makeText(this, "Todo updated", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
} 