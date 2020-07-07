package com.example.androidlabs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyOpener extends SQLiteOpenHelper {
    public final static String TABLE_NAME = "CHATMESSAGE";
    public final static String COL_SENDER = "SENDER";
    public final static String COL_MSG = "MSG";
    protected final static String DATABASE_NAME = "messages";
    protected final static int VERSION_NUM = 1;

    public MyOpener(Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_SENDER + " text,"
                + COL_MSG + " text);"); //add or remove columns
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME); // drop the old table
        onCreate(db); // create a new one
    }
}
