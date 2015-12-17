package com.example.aviram.ex2_home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import java.text.DecimalFormat;

public class MainActivity extends Activity implements View.OnClickListener,MyListener{

  private SharedPreferences sharedPref;
  private SharedPreferences.Editor editor;
  private  int level,complexity;
  private drawAnimation animation;
  private Button btSetting,btStart;
  private long start_time,elapse_time;
  private double time_difference;
  private TextView tvRecent,tvBest;
  public DAL dal;
  private Task task;
  private boolean gameRum;
  private double timeInTask;


  protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);//delete the name of the project
    setContentView(R.layout.activity_main);

    Initialization();
  }
  public void onGameFinishLisrener(){
    //the function run when the game is finish

    gameRum=false;//for task
    btSetting.setClickable(true);
    btStart.setClickable(true);
    Log.i("aviramLog", "======GameFinish======");

    long elapse_time = android.os.SystemClock.uptimeMillis() - start_time;
    time_difference = (double) elapse_time;
    time_difference = time_difference / 1000;//time in sec

    tvRecent.setText("Recent Result:\n" + time_difference + " sec");
    Log.i("aviramLog", "your time is: " + time_difference);

    boolean newRecord=dal.addScore(level, complexity, time_difference + "");// add or updata the record in db
    if (newRecord)//have a new record
    {
      tvBest.setText("Best Result:\n" + time_difference);
    }
  }
  public void onClick(View v){
    switch (v.getId()){

      case R.id.buttonSetting:
        Intent settingInetent=new Intent(this,setting.class);
        startActivity(settingInetent);
        break;

      case R.id.buttonStart:
        btStart.setClickable(false);
        gameRum=true;//for task
        timeInTask=0;

        task = new Task();//make a new task for the timer
        task.execute();

        btSetting.setClickable(false);
        start_time = android.os.SystemClock.uptimeMillis();
        Log.i("aviramLog", "start_time: "+start_time);
        animation.startGame();//play- start game
        break;

      case R.id.textViewBest://if user want to restart the record
        makeDialog();
        break;
    }
  }
  private void makeDialog(){
  //the function make a 'Dialog message' to ensor the user want to delete the record
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() { /*DialogInterface called while setting the AlertDialog Buttons */
      public void onClick(DialogInterface dialog, int which) {
        switch (which){
          case DialogInterface.BUTTON_POSITIVE://delete from DB
            dal.deletRow(level, complexity);
            tvBest.setText("Best Result:\n");
            break;


          case DialogInterface.BUTTON_NEGATIVE:
            //No button clicked
            break;
        }
      }
    };

    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("Alert Dialog");// Set the Title of Alert Dialog
    builder.setMessage("Are you sure you want delete \nlevel: "+level+" ,complexity: "+complexity+"\nfrom Data Base?")
            .setPositiveButton("Yes",dialogClickListener)
            .setNegativeButton("No", dialogClickListener).show();/* Setting the Alert message with buttons Yes and No */
  }
  public boolean onCreateOptionsMenu(Menu menu){
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }
  public boolean onOptionsItemSelected(MenuItem item){
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      Intent settingInetent=new Intent(this,setting.class);
      startActivity(settingInetent);
    }

    return super.onOptionsItemSelected(item);
  }
  private void Initialization(){
    dal = new DAL(getApplicationContext());//dal class to heandle with data-base

    btStart=(Button)findViewById(R.id.buttonStart);
    btStart.setOnClickListener(this);

    btSetting=(Button)findViewById(R.id.buttonSetting);
    btSetting.setOnClickListener(this);

    tvBest=(TextView)findViewById(R.id.textViewBest);
    tvBest.setOnClickListener(this);

    tvRecent=(TextView)findViewById(R.id.textViewRecent);

    setSetting();//get data from Shared Preferences

    animation=(drawAnimation)findViewById(R.id.layoutToDraw);
    animation.setMyListener(this);

    tvBest.setText("Best Result:\n" + dal.getBestScore(level, complexity));//get the best score from DB

    //dal.getAllTimeEntriesCursor();//print to log all DB

    gameRum=false;
  }
  private void setSetting(){

  sharedPref = getSharedPreferences("prefBestScore", MODE_PRIVATE);
  editor = sharedPref.edit();
  if (sharedPref.getInt("level",-1)==-1)//first time the app open
  {
    editor.putInt("level", 1);
    level=1;
    editor.apply();
    Log.i("aviramLog","first level");
  }
  if (sharedPref.getInt("complexity",-1)==-1)//first time app open
  {
    editor.putInt("complexity", 0);
    complexity=0;
    editor.apply();
    Log.i("aviramLog", "first complexity");
  }

  level=sharedPref.getInt("level", 1);
  complexity=sharedPref.getInt("complexity",0);
  Log.i("aviramLog", "level:" +level );//counter of click
  Log.i("aviramLog", "complexity:" + complexity);//number of circle

}
  protected void onStop(){
    super.onStop();
    gameRum=false;
    animation.stopGame();//cant click in screen
    Log.i("aviramLog", "onStop");
  }
  public void onResume(){
    super.onResume();
    Log.i("aviramLog", "onResume");

    btStart.setClickable(true);
    btSetting.setClickable(true);
    tvRecent.setText("Recent Result:\n" );

  }
  private class Task extends AsyncTask<Void, Integer, Integer> {
    @Override
    protected Integer doInBackground(Void... params) {

    int i=0;
      while(gameRum)//the button 'start' press
      {
        try {
          Thread.sleep(100);//every 0.1sec
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        publishProgress(i);
        i++;
      }

      return i;
    }
    protected void onProgressUpdate(Integer... value) {
      timeInTask=timeInTask+0.1;

      DecimalFormat df = new DecimalFormat("#.##");
      timeInTask = Double.valueOf(df.format(timeInTask));

      tvRecent.setText("current time:\n"+timeInTask);
    }
    protected void onPostExecute(Integer result) {
      Log.i("aviramLog1", "+++onPostExecute: "+result);
      tvRecent.setText("Recent Result:\n" + time_difference + " sec");
    }
  }
}