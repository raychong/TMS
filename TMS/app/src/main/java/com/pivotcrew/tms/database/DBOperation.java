package com.pivotcrew.tms.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pivotcrew.tms.model.Task;

import java.util.ArrayList;

import static com.pivotcrew.tms.Global.COLUMN_DATE;
import static com.pivotcrew.tms.Global.COLUMN_ID;
import static com.pivotcrew.tms.Global.COLUMN_MODIFIED;
import static com.pivotcrew.tms.Global.COLUMN_NAME;
import static com.pivotcrew.tms.Global.TABLE_TASK;
/**
 * Created by RayChongJH on 5/6/17.
 */
public class DBOperation {

    InventoryDBHandler dbHandler;
    SQLiteDatabase database;
    final String[] dbColumns = {COLUMN_ID, COLUMN_NAME,COLUMN_DATE,COLUMN_MODIFIED};


    public DBOperation(Context context) {
        dbHandler = new InventoryDBHandler(context);
    }

    public void openDB() {
        try {
            database = dbHandler.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeDB() {
        try {
            dbHandler.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Task addTask(Task task) {
        
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, task.getTaskName());
        values.put(COLUMN_DATE, task.getDate());
        values.put(COLUMN_MODIFIED, task.getIsModified());
        long id = database.insert(TABLE_TASK, null, values);
        
        Task task_ = new Task(id,task.getTaskName(),task.getDate(),task.getIsModified());
        return task_;
    }

    public Task getTask(long id) {
        Task task = new Task();
        try {
            
            Cursor cursor = database.query(TABLE_TASK, dbColumns, COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
            if (cursor != null)
                cursor.moveToFirst();
            task = new Task(Long.parseLong(cursor.getString(0)),cursor.getString(1),cursor.getString(2),Integer.parseInt(cursor.getString(3)));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return task;
    }

    public ArrayList<Task> getTaskList() {
        ArrayList<Task> taskList = new ArrayList<>();
        try {
            
            Cursor cursor = database.query(TABLE_TASK, dbColumns, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    Task task = new Task();
                    task.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
                    task.setTaskName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                    task.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
                    task.setIsModified(cursor.getInt(cursor.getColumnIndex(COLUMN_MODIFIED)));
                    taskList.add(task);
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return taskList;
    }

    public int updateTask(Task task) {
        int result = -1;
        try {
            
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, task.getTaskName());
            result =  database.update(TABLE_TASK, values,
                    COLUMN_ID + "=?", new String[]{String.valueOf(task.getId())});
            
        } catch (Exception e) {
            e.printStackTrace();

        }
        return result;
    }

    public void deleteTaskEntry(Task task) {
        try {
            
            database.delete(TABLE_TASK, COLUMN_ID + "=" + task.getId(), null);
            
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public int getEntryCount(){
        int count = 0;
        try {
            
            Cursor cursor = database.query(TABLE_TASK, dbColumns, null, null, null, null, null);
            count = cursor.getCount();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }


}
