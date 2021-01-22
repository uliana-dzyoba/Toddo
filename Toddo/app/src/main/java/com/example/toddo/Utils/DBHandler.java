package com.example.toddo.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.toddo.tasks.TaskContent;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String NAME = "ToddoDB";
    private static final String TABLE = "tasks";
    private static final String ID = "id";
    private static final String TASK_NAME = "task_name";
    private static final String TIME = "time";
    private static final String DATE = "date";
    private static final String LIST = "list";
    private static final String PRIORITY = "priority";
    private static final String IS_COMPLETED = "is_completed";
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK_NAME +
    " TEXT, " + TIME + " TEXT, " + DATE + " TEXT, " + LIST + " TEXT, " + PRIORITY + " TEXT, " + IS_COMPLETED + " INTEGER)";
    private SQLiteDatabase db;


    public DBHandler(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    public void openDatabase(){
        db = this.getWritableDatabase();
    }

    public void insertTask(TaskContent task) {
        ContentValues cv = new ContentValues();
        cv.put(TASK_NAME, task.getTask_name());
        cv.put(TIME, task.getTime());
        cv.put(DATE, task.getDate());
        cv.put(LIST, task.getList());
        cv.put(PRIORITY, task.getPriority());
        cv.put(IS_COMPLETED, 0);
        db.insert(TABLE, null, cv);
    }

    public List<TaskContent> getAllTasks() {
        List<TaskContent> taskList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try {
            cur = db.query(TABLE, null, null, null, null, null, null, null);
            if(cur != null) {
                if(cur.moveToFirst()) {
                    do {
                        TaskContent task = new TaskContent();
                        task.setId(cur.getInt(cur.getColumnIndex(ID)));
                        task.setTask_name(cur.getString(cur.getColumnIndex(TASK_NAME)));
                        task.setTime(cur.getString(cur.getColumnIndex(TIME)));
                        task.setDate(cur.getString(cur.getColumnIndex(DATE)));
                        task.setList(cur.getString(cur.getColumnIndex(LIST)));
                        task.setPriority(cur.getString(cur.getColumnIndex(PRIORITY)));
                        task.setIs_completed(cur.getInt(cur.getColumnIndex(IS_COMPLETED)));
                        taskList.add(task);
                    } while(cur.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            cur.close();
        }
        return taskList;
    }

    public void updateStatus(int id, int is_completed) {
        ContentValues cv = new ContentValues();
        cv.put(IS_COMPLETED, is_completed);
        db.update(TABLE, cv, ID + "=?", new String[] {String.valueOf(id)});
    }

    public void updateTask(int id, String task_name, String time, String date, String priority) {
        ContentValues cv = new ContentValues();
        cv.put(TASK_NAME, task_name);
        cv.put(TIME, time);
        cv.put(DATE, date);
        cv.put(PRIORITY, priority);
        db.update(TABLE, cv, ID + "=?", new String[] {String.valueOf(id)});
    }

    public void deleteTask(int id) {
        db.delete(TABLE, ID + "=?", new String[] {String.valueOf(id)});
    }
}
