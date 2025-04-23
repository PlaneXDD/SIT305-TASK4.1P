package com.example.taskmanagerapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "TaskDB.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Tasks(title TEXT PRIMARY KEY, description TEXT, dueDate TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Tasks");
        onCreate(db);
    }

    public boolean insertTask(String title, String description, String dueDate) {
        if (isTaskExists(title)) return false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("description", description);
        contentValues.put("dueDate", dueDate);
        long result = db.insert("Tasks", null, contentValues);
        return result != -1;
    }

    public boolean updateTask(String title, String description, String dueDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("description", description);
        contentValues.put("dueDate", dueDate);
        int result = db.update("Tasks", contentValues, "title=?", new String[]{title});
        return result > 0;
    }

    public boolean deleteTask(String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("Tasks", "title=?", new String[]{title});
        return result > 0;
    }

    public boolean markAsFinished(String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("Tasks", "title=?", new String[]{title});
        return result > 0;
    }

    public Cursor getAllTasks() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Tasks ORDER BY dueDate ASC", null);
    }

    public boolean isTaskExists(String title) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Tasks WHERE title = ?", new String[]{title});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }
}
