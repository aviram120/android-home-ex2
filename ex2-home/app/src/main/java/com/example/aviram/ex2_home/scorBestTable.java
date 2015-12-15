package com.example.aviram.ex2_home;

import android.provider.BaseColumns;

/**
 * Created by AVIRAM on 13/12/2015.
 */
public class scorBestTable {
  public scorBestTable(){}

  public static abstract class  scorBest implements BaseColumns
  {
    public static final String TABLE_NAME = "scoreBest";

    public static final String LEVEL="level";
    public static final String COMPLEXITY="complexity";
    public static final String TIME="time";
  }
}