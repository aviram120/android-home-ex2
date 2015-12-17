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

public class setting extends Activity implements View.OnClickListener {
    private EditText etComplexNum,etLevelNum;
    private Button save;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.setting);
        Initialization();
    }
    public void onClick(View v){
        boolean flag=true;
        if (v.getId()==R.id.buttonSave)
        {
            //====level field====
            String stLevelNum=etLevelNum.getText().toString();
            if (!stLevelNum.contains(".")&&(!stLevelNum.isEmpty()))
            {
                int levelNum=Integer.parseInt(stLevelNum);//get the date form the field
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
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Level must be between 1-10 \nand Integer number", Toast.LENGTH_LONG).show();
                flag=false;
            }

            //====Complex field====
            String stComplexNum=etComplexNum.getText().toString();
            if (!stComplexNum.contains(".")&&(!stComplexNum.isEmpty()))
            {
                int complexNum=Integer.parseInt(stComplexNum);//get the date form the field
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
            }
            else
            {
                Toast.makeText(getApplicationContext(), "complexity must be between 0-4 \nand Integer number", Toast.LENGTH_LONG).show();
                flag=false;
            }

            if (flag)//goto main page
            {
                Intent Inetent = new Intent(this,MainActivity.class);
                startActivity(Inetent);
            }
        }
    }
    private void Initialization(){

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