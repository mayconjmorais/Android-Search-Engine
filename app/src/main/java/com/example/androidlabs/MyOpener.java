package com.example.androidlabs;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyOpener extends SQLiteOpenHelper {
    public final static String TABLE_NAME = "CHATMESSAGE";
    public final static String COL_SENDER = "SENDER";
    public final static String COL_MSG = "MSG";
    public final static String COL_ID = "_id";
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

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    // This function will be used for debug purposes for your final project
    public Cursor viewData(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        printCursor(cursor, VERSION_NUM);
        return cursor;
    }
    // TODO follow the requirement
    public void printCursor(Cursor cursor, int version){
        SQLiteDatabase db = this.getReadableDatabase();
        Log.v("Database Version:", String.valueOf(db.getVersion()));
        Log.v("Number of columns: ", String.valueOf(cursor.getColumnCount()));
        for (int i = 0; i < cursor.getColumnCount(); i++){
            Log.v("Column "+(i+1)+": ", cursor.getColumnName(i));
        }
        Log.v("Number of rows:", String.valueOf(cursor.getCount()));
        Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(cursor));
    }
}
