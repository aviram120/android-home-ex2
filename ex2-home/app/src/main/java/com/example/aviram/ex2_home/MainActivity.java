package com.example.aviram.ex2_home;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends Activity implements View.OnClickListener{

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private  int level,complexity;
    private drawAnimation animation;
    private Button btSetting,btStart;
    //private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Initialization();

        animation=(drawAnimation)findViewById(R.id.layoutToDraw);
        //setContentView(animation);

      // task = new Task();


    }
    public void onClick(View v) {
        if (v.getId()==R.id.buttonSetting)
        {
            Intent settingInetent=new Intent(this,setting.class);
            startActivity(settingInetent);
        }
        if (v.getId()==R.id.buttonStart) {
           double time=animation.startGame();
            editor.putInt("game",1);//put the date in sharedPref
            editor.apply();
           new Task().execute();
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
    private void Initialization()
    {
        btStart=(Button)findViewById(R.id.buttonStart);
        btStart.setOnClickListener(this);

        btSetting=(Button)findViewById(R.id.buttonSetting);
        btSetting.setOnClickListener(this);

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

        Log.i("aviramLog", "level:" + sharedPref.getInt("level", 100));//counter of click
        Log.i("aviramLog", "complexity:"+sharedPref.getInt("complexity",100));//number of circle

    }

    @Override
    protected void onStop() {
        super.onStop();

/*
        editor = sharedPref.edit();
        editor.putLong("time", -1);
        editor.apply();*/

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private class Task extends AsyncTask<Void, Integer, String> {

        @Override
        protected String doInBackground(Void... params) {

            int gameFlag;
            //drawAnimation myObject = params[0];
            Log.i("aviramLog", "befor while");
            while (true)
            {
                gameFlag=sharedPref.getInt("game", 100);Log.i("aviramLog", "gameFlag: "+gameFlag);
                if (gameFlag==0)
                {
                    Log.i("aviramLog", "in if");
                    break;
                }
            }
            Log.i("aviramLog", "after while");
            publishProgress(gameFlag);


            return "game is stop";

        }

        protected void onProgressUpdate(Integer... value) {

        }


        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.i("aviramLog", "result from task: "+result);

        }
    }

}