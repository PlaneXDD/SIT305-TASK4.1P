package com.example.taskmanagerapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AddTaskActivity extends AppCompatActivity {

    EditText edtTitle, edtDescription, edtDueDate;
    Button btnSave, btnCancel;
    DBHelper dbHelper;
    boolean isEdit = false;
    String originalTitle = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        edtTitle = findViewById(R.id.edtTitle);
        edtDescription = findViewById(R.id.edtDescription);
        edtDueDate = findViewById(R.id.edtDueDate);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        dbHelper = new DBHelper(this);

        // Show DatePicker when dueDate field is clicked
        edtDueDate.setOnClickListener(v -> showDatePicker());

        if (getIntent().hasExtra("title")) {
            isEdit = true;
            originalTitle = getIntent().getStringExtra("title");
            edtTitle.setText(originalTitle);
            edtTitle.setEnabled(false);
            edtDescription.setText(getIntent().getStringExtra("description"));
            edtDueDate.setText(getIntent().getStringExtra("dueDate"));
        }

        btnSave.setOnClickListener(v -> {
            String title = edtTitle.getText().toString().trim();
            String description = edtDescription.getText().toString().trim();
            String dueDate = edtDueDate.getText().toString().trim();

            if (title.isEmpty() || dueDate.isEmpty()) {
                Toast.makeText(this, getString(R.string.task_required), Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isEdit && dbHelper.isTaskExists(title)) {
                Toast.makeText(this, getString(R.string.task_duplicate), Toast.LENGTH_SHORT).show();
                return;
            }

            boolean success = isEdit
                    ? dbHelper.updateTask(title, description, dueDate)
                    : dbHelper.insertTask(title, description, dueDate);

            Toast.makeText(this,
                    success ? getString(R.string.task_saved) : getString(R.string.task_failed),
                    Toast.LENGTH_SHORT).show();

            if (success) finish();
        });

        btnCancel.setOnClickListener(v -> finish());
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this,
                (DatePicker view, int selectedYear, int selectedMonth, int selectedDay) -> {
                    String dateStr = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                    edtDueDate.setText(dateStr);
                }, year, month, day);
        dialog.show();
    }
}
