package com.wazinsure.qsure.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    public static final String DATABASE_NAME = "Registration.db";
    public static final String TABLE_NAME = "registration_table";
    private static final String COL1 = "ID";
    private static final String COL2 = "FIRSTNAME";
    private static final String COL3 = "OTHERNAME";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME,null,1);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" (ID INTEGER PRIMARY KEY ,FIRSTNAME TEXT,OTHERNAME TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

        public boolean insertData(String ID,String FIRSTNAME,String OTHERNAME ){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, ID);
        contentValues.put(COL2, FIRSTNAME);
        contentValues.put(COL3, OTHERNAME);

        long result =  db.insert(TABLE_NAME,null,contentValues);

        if (result == -1)
            return false;
        else
            return true;
    }


    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor result = db.rawQuery("SELECT * from "+ TABLE_NAME,null );
        return result;
    }


}
