package com.example.aviram.ex2_home;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by AVIRAM on 05/12/2015.
 */
public class setting extends Activity implements View.OnClickListener {
    EditText etComplexNum,etLevelNum;
    Button save;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.setting);
        Initialization();
    }

    public void onClick(View v) {
        boolean flag=true;
        if (v.getId()==R.id.buttonSave)
        {
            int levelNum=Integer.parseInt(etLevelNum.getText().toString());//get the date form the field
            if ((levelNum>0)&(levelNum<11))//check if the user add ok number
            {
                editor.putInt("level",levelNum);//put the date in sharedPref
                editor.apply();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Level must be between 1-10", Toast.LENGTH_LONG).show();
                flag=false;
            }


            int complexNum=Integer.parseInt(etComplexNum.getText().toString());//get the date form the field
            if ((complexNum>-1)&(complexNum<5))//check if the user add ok number
            {
                editor.putInt("complexity",complexNum);//put the date in sharedPref
                editor.apply();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "complexity must be between 0-4", Toast.LENGTH_LONG).show();
                flag=false;
            }

            if (flag)//goto main page
            {
                Intent Inetent = new Intent(this,MainActivity.class);
                startActivity(Inetent);
            }

        }
        Log.i("aviramLog", "level:" + sharedPref.getInt("level", 100));
        Log.i("aviramLog", "complexity:"+sharedPref.getInt("complexity",100));
    }
    private void Initialization()
    {
        etComplexNum=(EditText)findViewById(R.id.editTextComplex);
        etLevelNum=(EditText)findViewById(R.id.editTextLevel);
        save=(Button)findViewById(R.id.buttonSave);
        save.setOnClickListener(this);
        sharedPref = getSharedPreferences("prefBestScore", MODE_PRIVATE);
        editor = sharedPref.edit();

        etLevelNum.setText(Integer.toString(sharedPref.getInt("level", -1)));//get the date from sharedPref
        etComplexNum.setText(Integer.toString(sharedPref.getInt("complexity", -1)));//get the date from sharedPref
    }
}