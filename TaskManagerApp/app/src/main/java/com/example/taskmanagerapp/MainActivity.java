package com.example.taskmanagerapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        // ✅ Clean modern lambda version
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                Toast.makeText(MainActivity.this, getString(R.string.nav_home), Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.nav_add) {
                startActivity(new Intent(MainActivity.this, AddTaskActivity.class));
                return true;
            } else if (id == R.id.nav_tasks) {
                startActivity(new Intent(MainActivity.this, TaskListActivity.class));
                return true;
            }
            return false;
        });
    }
}
