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
  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    //set color
    red_paintbrush_fill = new Paint();
    red_paintbrush_fill.setColor(Color.RED);
    red_paintbrush_fill.setStyle(Paint.Style.FILL);

    arrCenter = new int[numCircle * 2];

    //set the rectangle in screen
    int moveRect = (int) (Math.random() * 1000 + 0);
    //Log.i("aviramLog", "moveRect:" + moveRect);
    rectangle = new Rect(0 + moveRect, 0 + moveRect, moveRect + rectWidth, moveRect + rectHight);
    canvas.drawRect(rectangle, red_paintbrush_fill);

    for (int i = 0; i < numCircle; i++)
    {
     /* while (true)
      {
        if (!generateXY(canvas, i))
        {
          break;
        }
      }*/
      while (generateXY(canvas, i));//check is the circle isn't in any shape

      //Log.i("aviramLog", "i:"+i+" ,x:" + x_Circle + ",y:" + y_Circle);
      arrCenter[i*2] = x_Circle;
      arrCenter[i*2 + 1] = y_Circle;
      canvas.drawCircle(x_Circle, y_Circle, radiosCircle, red_paintbrush_fill);//darw circle
    }
  }

  private boolean generateXY(Canvas canvas,int idOfCircle)
  {//the function make random x and y for the cirlce , and check if is valid

    Random rand = new Random();


    //select random number for the center of the circle
    x_Circle=rand.nextInt(canvas.getWidth()-radiosCircle*2)+radiosCircle;
    y_Circle=rand.nextInt(canvas.getHeight()-radiosCircle*2)+radiosCircle;
    //Log.i("aviramLog", "from while x:" + x_Circle + ",y:" + y_Circle);


    if (checkIfPointIsInRectangel())//if the cordntion isn't in the rectangle
    {
      return false;
    }

    for(int i=0; i<idOfCircle; i++)//run of arr of the other circle
    {
      int x=arrCenter[i*2];
      int y=arrCenter[i*2+1];

      double des= (Math.pow((x-x_Circle), 2)+Math.pow((y-y_Circle), 2));
      des=Math.pow(des,0.5);
     // Log.i("aviramLog", "i:"+i+" ,x_Circle:" + x_Circle + " ,y_Circle:" + y_Circle+ " ,x:"+x+" ,y:"+y+" ,des:"+des);
      if (des<=radiosCircle*2)//if the cordntion is in the other circle
      {
        return false;
      }

    }
    return true;
  }
  private boolean checkIfPointIsInRectangel()
  {//the fucntion check if the circe isn't in the rectangle

    int leftTopX,leftTopY,rightBottomX,rightBottomY;
    int rightTopX,rightTopY,leftBottomX,leftBottomY;

    leftTopX=rectangle.left;
    leftTopY=rectangle.top;

    rightBottomX=rectangle.right;
    rightBottomY=rectangle.bottom;

    rightTopX=leftTopX+rectWidth;
    rightTopY=leftTopY;

    leftBottomX=leftTopX;
    leftBottomY=leftTopY+rectHight;

    //left top point
    if (!checkDistance(leftTopX, leftTopY))
      return false;

    //right bottom point
    if (!checkDistance(rightBottomX,rightBottomY))
      return false;

    //right top point
    if (!checkDistance(rightTopX,rightTopY))
      return false;

    //left bottom point
    if (!checkDistance(leftBottomX,leftBottomY))
      return false;

    if (rectangle.contains(x_Circle,y_Circle))
      return false;

    if (rectangle.contains(x_Circle,y_Circle+radiosCircle))
      return false;

    if (rectangle.contains(x_Circle-radiosCircle,y_Circle))
      return false;

    if (rectangle.contains(x_Circle,y_Circle-radiosCircle))
      return false;

    if (rectangle.contains(x_Circle+radiosCircle,y_Circle))
      return false;

    return true;
  }
  private boolean checkDistance(int pointX,int pointY)
  {//check the dis between the point to the radios-circle

    double dis= (Math.pow((pointX-x_Circle), 2)+Math.pow((pointY-y_Circle), 2));
    dis=Math.pow(dis,0.5);
    if (dis<=radiosCircle)
    {
      return false;
    }

    return true;
  }
  public boolean onTouchEvent(MotionEvent event)
  {
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