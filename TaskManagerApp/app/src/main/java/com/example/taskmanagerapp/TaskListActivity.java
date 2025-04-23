package com.example.taskmanagerapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class TaskListActivity extends AppCompatActivity {

    ListView taskListView;
    DBHelper dbHelper;
    ArrayList<String> titles = new ArrayList<>();
    ArrayList<String> descriptions = new ArrayList<>();
    ArrayList<String> dueDates = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        taskListView = findViewById(R.id.taskListView);
        dbHelper = new DBHelper(this);

        loadTasks();

        taskListView.setOnItemClickListener((adapter, view, i, l) -> {
            Intent intent = new Intent(TaskListActivity.this, AddTaskActivity.class);
            intent.putExtra("title", titles.get(i));
            intent.putExtra("description", descriptions.get(i));
            intent.putExtra("dueDate", dueDates.get(i));
            startActivity(intent);
        });

        taskListView.setOnItemLongClickListener((adapterView, view, i, l) -> {
            String titleToDelete = titles.get(i);
            new AlertDialog.Builder(this)
                    .setTitle("Mark as Finished")
                    .setMessage("Do you want to mark this task as finished?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        boolean deleted = dbHelper.markAsFinished(titleToDelete);
                        Toast.makeText(this,
                                deleted ? getString(R.string.task_deleted) : getString(R.string.task_delete_failed),
                                Toast.LENGTH_SHORT).show();
                        if (deleted) loadTasks();
                    })
                    .setNegativeButton("No", null)
                    .show();
            return true;
        });
    }

    private void loadTasks() {
        ArrayList<String> taskList = new ArrayList<>();
        titles.clear();
        descriptions.clear();
        dueDates.clear();

        Cursor cursor = dbHelper.getAllTasks();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, getString(R.string.no_tasks_found), Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                String title = cursor.getString(0);
                String desc = cursor.getString(1);
                String date = cursor.getString(2);
                titles.add(title);
                descriptions.add(desc);
                dueDates.add(date);
                taskList.add("Title: " + title + "\nDescription: " + desc + "\nDue: " + date);
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, taskList);
        taskListView.setAdapter(adapter);
    }
}
