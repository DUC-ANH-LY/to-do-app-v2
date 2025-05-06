package com.example.todoappv2.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import com.example.todoappv2.dao.CategoryDao;
import com.example.todoappv2.dao.TodoDao;
import com.example.todoappv2.model.Category;
import com.example.todoappv2.model.Todo;
import com.example.todoappv2.model.TodoCategoryCrossRef;
import com.example.todoappv2.util.DateConverter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Todo.class, Category.class, TodoCategoryCrossRef.class}, version = 4)
@TypeConverters({DateConverter.class})
public abstract class TodoDatabase extends RoomDatabase {
    private static TodoDatabase instance;
    private static final ExecutorService databaseWriteExecutor = Executors.newSingleThreadExecutor();
    public abstract TodoDao todoDao();
    public abstract CategoryDao categoryDao();

    public static synchronized TodoDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                context.getApplicationContext(),
                TodoDatabase.class,
                "todo_database"
            )
            .fallbackToDestructiveMigration()
            .build();
        }
        return instance;
    }

    public static ExecutorService getDatabaseWriteExecutor() {
        return databaseWriteExecutor;
    }
} 