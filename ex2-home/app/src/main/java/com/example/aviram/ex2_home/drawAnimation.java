package com.example.aviram.ex2_home;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.Random;

public class  drawAnimation extends View  {

  private SharedPreferences sharedPref;
  private SharedPreferences.Editor editor;
  private Paint red_paintbrush_fill;
  private Rect rectangle;
  private int numCircle, radiosCircle,level;
  private Context contextSAVE;
  private int x_Circle, y_Circle;
  private int[] arrCenter;
  private int rectWidth, rectHight;
  private boolean startGame;
  private final int LEVEL;
  private ArrayList<MyListener> listeners = new ArrayList<MyListener>();


  public drawAnimation(Context context, AttributeSet attrs) {
    super(context, attrs);
    contextSAVE = context;

    sharedPref = context.getSharedPreferences("prefBestScore", context.MODE_PRIVATE);

    LEVEL=sharedPref.getInt("level", 1);//get data from sharedPref
    this.level=LEVEL;
    this.numCircle = sharedPref.getInt("complexity", 0);//get data from sharedPref

    radiosCircle = 170;
    rectWidth = 400;
    rectHight = 200;
    startGame=false;
  }
  public void setMyListener(MyListener listener){
    listeners.add(listener);
  }
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    boolean flagRandom;
    //set color
    red_paintbrush_fill = new Paint();
    red_paintbrush_fill.setColor(Color.RED);
    red_paintbrush_fill.setStyle(Paint.Style.FILL);

    arrCenter = new int[numCircle * 2];

    //set the rectangle in screen
    int moveRect = (int) (Math.random() * 1000 + 0);
    rectangle = new Rect(0 + moveRect, 0 + moveRect, moveRect + rectWidth, moveRect + rectHight);
    canvas.drawRect(rectangle, red_paintbrush_fill);

    for (int i = 0; i < numCircle; i++)
    {
      while (!makeXYCenter(canvas,i));//set the circle in the screen

      //save the center
      arrCenter[i*2] = x_Circle;
      arrCenter[i*2 + 1] = y_Circle;

      canvas.drawCircle(x_Circle, y_Circle, radiosCircle, red_paintbrush_fill);//draw circle
    }
  }
  private boolean makeXYCenter(Canvas canvas,int idCircle) {
    /*the function generate (random_ point-center for the circle
            A. check if the point is outside of rectangle
            B. check if the new circle isn't in the other circle
            if OK return true
          * */

    int height=canvas.getHeight();
    int width=canvas.getWidth();
    Random rand = new Random();

    int  xCenter =rand.nextInt(width-radiosCircle*2) + radiosCircle;
    int yCenter=rand.nextInt(height-radiosCircle*2)+radiosCircle;

    if (checkIfCircleInRectangle(xCenter,yCenter)==false)//check if the point is outside of rectangle
    {
      return false;
    }

    if(checkIfCircleInCircle(xCenter,yCenter,idCircle)==false)//check if the new circle isn't in the other circle
    {
      return false;
    }

    //set the center
    x_Circle=xCenter;
    y_Circle=yCenter;

    return true;
  }
  private boolean checkIfCircleInRectangle(int xCenter,int yCenter) { //the function return false if the point is in the rectangle

    if (rectangle.contains(xCenter,yCenter))//in the rectangle
    {
      return false;
    }
    if (rectangle.contains(xCenter,yCenter+radiosCircle))//in the rectangle
    {
      return false;
    }
    if (rectangle.contains(xCenter-radiosCircle,yCenter))//in the rectangle
    {
      return false;
    }
    if (rectangle.contains(xCenter,yCenter-radiosCircle))//in the rectangle
    {
      return false;
    }
    if (rectangle.contains(xCenter+radiosCircle,yCenter))//in the rectangle
    {
      return false;
    }

    if (rectangle.contains(xCenter-radiosCircle,yCenter+radiosCircle))//in the rectangle
    {
      return false;
    }
    if (rectangle.contains(xCenter-radiosCircle,yCenter-radiosCircle))//in the rectangle
    {
      return false;
    }
    if (rectangle.contains(xCenter+radiosCircle,yCenter-radiosCircle))//in the rectangle
    {
      return false;
    }
    if (rectangle.contains(xCenter+radiosCircle,yCenter+radiosCircle))//in the rectangle
    {
      return false;
    }

    return true;
  }
  private boolean checkIfCircleInCircle(int xCenter,int yCenter,int idCircle) { //the function return false if the point is in the other circle

    int xOther,yOther,distance;

    for (int i=0;i<idCircle; i++)//run of all circle
    {
      xOther=arrCenter[i*2];
      yOther=arrCenter[i*2+1];
      distance = (int)Math.sqrt((xOther-xCenter)*(xOther-xCenter) + (yOther-yCenter)*(yOther-yCenter));
      if(distance<radiosCircle*2+10)
      {
        return false;
      }
    }

    return true;
  }
  public boolean onTouchEvent(MotionEvent event) {
    if(startGame)
    {
      if (event.getAction() == MotionEvent.ACTION_UP) {

        int x = (int) event.getX();//some math logic to plot the rect  in exact touch place
        int y = (int) event.getY();

        if (rectangle.contains(x, y)) {
          level--;
          if (level == 0) {
            level = LEVEL;
            startGame=false;
            for (MyListener listener:listeners){//send to MainActivity that finish the game
              listener.onGameFinishLisrener();
            }
          }
          invalidate();
        }
      }
    }
    return true;
  }
  public void startGame()
  {
    startGame=true;
  }
  public void stopGame()
  {
    startGame=false;
  }
}