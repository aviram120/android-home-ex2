package com.example.aviram.ex2_home;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by AVIRAM on 13/12/2015.
 */
public class DAL {

    DBScore dbScore;
    public DAL (Context context){
       dbScore=new DBScore(context);
    }
    public boolean addScore(int level,int complexity, String time)
   /*the function get level and complexity -as a key
      and check in the dbd if the raw is existing if 'YES'-check if have new record-if 'YES' updata db
      if the isn't existing in db - make a new raw in db
    * */
    {

        boolean flag=false;
        String lastRecord;

        if (isRawExist(level,complexity))//exist in db
        {
            lastRecord=getBestScore(level,complexity);//get the last record that in db
            double lastRecDouble=Double.parseDouble(lastRecord);
            double timeDouble=Double.parseDouble(time);
            if (timeDouble<lastRecDouble)//check if have new record
            {
                updataBestScore(level, complexity,time);//updata the raw
                flag=true;
                Log.i("aviramLog", "exist ,new record");
            }

        }
        else//not exist in db-new record
        {
            //get DB
            SQLiteDatabase db=dbScore.getReadableDatabase();

            //set DATA
            ContentValues valuse =  new ContentValues();
            valuse.put(scorBestTable.scorBest.LEVEL,level);
            valuse.put(scorBestTable.scorBest.COMPLEXITY,complexity);
            valuse.put(scorBestTable.scorBest.TIME,time);

            db.insert(scorBestTable.scorBest.TABLE_NAME, null, valuse);
            db.close();
            flag=true;
            Log.i("aviramLog", "exist not,new record");
        }
        return flag;
    }
    private boolean isRawExist(int level,int complexity)
    /* the function check if the raw is existing in the DB
    */
    {
        boolean flag=true;
        SQLiteDatabase db = dbScore.getReadableDatabase();

        String[] selectionArgs={level+"",complexity+""};

        //query
        Cursor cursor=db.rawQuery("SELECT * FROM " + scorBestTable.scorBest.TABLE_NAME + " WHERE " +
                scorBestTable.scorBest.LEVEL + "=? AND " +
                scorBestTable.scorBest.COMPLEXITY + "=?", selectionArgs);

        if (cursor.getCount()==0)//check the answer
            flag=false;

        db.close();
        return flag;
    }
    public String getBestScore(int level,int complexity)
    /*the function return the record
    * */
    {
        String time="";
        SQLiteDatabase db = dbScore.getReadableDatabase();

        String[] selectionArgs={level+"",complexity+""};//args

        //query
        Cursor cursor=db.rawQuery("SELECT * FROM " + scorBestTable.scorBest.TABLE_NAME + " WHERE " +
                scorBestTable.scorBest.LEVEL + "=? AND " +
                scorBestTable.scorBest.COMPLEXITY + "=?", selectionArgs);


        while(cursor.moveToNext())//run on the answer
        {
            int timeInde=cursor.getColumnIndex(scorBestTable.scorBest.TIME);
            time=cursor.getString(timeInde);
            Log.i("aviramLog", "data from getBestScore time:"+time);
        }

        db.close();
        return time;
    }
    public void updataBestScore(int level,int complexity, String time)
    /*the function updata the new record in db
    * */
    {
        SQLiteDatabase db = dbScore.getReadableDatabase();

        ContentValues valuse =  new ContentValues();
        valuse.put(scorBestTable.scorBest.TIME, time);

        String where=scorBestTable.scorBest.LEVEL+"=? AND "+scorBestTable.scorBest.COMPLEXITY+"=?";
        String[] whereARGS={level+"",complexity+""};

        db.update(scorBestTable.scorBest.TABLE_NAME,valuse,where,whereARGS);
        db.close();
    }
    public void getAllTimeEntriesCursor()
            /*the function print into log all DB*/
    {
        SQLiteDatabase db = dbScore.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + scorBestTable.scorBest.TABLE_NAME,null);

        Log.i("aviramLog", "***GET all data from DB***");
        while(cursor.moveToNext())
        {
            Log.i("aviramLog", "lev:" +cursor.getInt(cursor.getColumnIndex(scorBestTable.scorBest.LEVEL)) +
                                "com:" +cursor.getInt(cursor.getColumnIndex(scorBestTable.scorBest.COMPLEXITY))+
                                "time:" +cursor.getString(cursor.getColumnIndex(scorBestTable.scorBest.TIME)));
        }
    }
    public void deletDB()
            /*the function delete all DB*/
    {
        SQLiteDatabase db = dbScore.getReadableDatabase();
        db.execSQL("DELETE FROM " + scorBestTable.scorBest.TABLE_NAME); //delete all rows in a table
        db.close();
        Log.i("aviramLog", "delet all db");
    }
}
