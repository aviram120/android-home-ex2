package com.example.aviram.ex2_home;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBScore extends SQLiteOpenHelper {

  public static final int DATABASE_VERSION = 1;
  public static final String DATABASE_NAME = "Score.db";

  public DBScore(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }
  public void onCreate(SQLiteDatabase db) {

    db.execSQL(
            "CREATE TABLE " + scorBestTable.scorBest.TABLE_NAME + " ( " +
                    scorBestTable.scorBest._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    scorBestTable.scorBest.LEVEL + " INTEGER," +
                    scorBestTable.scorBest.COMPLEXITY + " INTEGER," +
                    scorBestTable.scorBest.TIME + " TEXT" +
                    ");"
    );
  }
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXIST " + scorBestTable.scorBest.TABLE_NAME);
    onCreate(db);

  }
}