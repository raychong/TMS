package com.pivotcrew.tms.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.pivotcrew.tms.Global.COLUMN_DATE;
import static com.pivotcrew.tms.Global.COLUMN_ID;
import static com.pivotcrew.tms.Global.COLUMN_MODIFIED;
import static com.pivotcrew.tms.Global.COLUMN_NAME;
import static com.pivotcrew.tms.Global.DATABASE_NAME;
import static com.pivotcrew.tms.Global.TABLE_TASK;


/**
 * Created by RayChongJH on 5/6/17.
 */

public class InventoryDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_CREATE =
            "CREATE TABLE " +
                    TABLE_TASK + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_DATE + " TEXT, " +
                    COLUMN_MODIFIED + " INTEGER " +
                    ")";

    public InventoryDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK);
        sqLiteDatabase.execSQL(TABLE_CREATE);
    }
}
