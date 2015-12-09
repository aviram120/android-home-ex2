package com.example.aviram.ex2_home;

/**
 * Created by AVIRAM on 06/12/2015.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class drawingView1 extends View implements OnTouchListener{

    static int x,y,r=255,g=255,b=255;
    final static int radius=30;
    Paint paint;     //using this ,we can draw on canvas

    public drawingView1(Context context)
    {
        super(context);
        paint=new Paint();
        paint.setAntiAlias(true);       //for smooth rendering
        paint.setARGB(255, r, g, b);    //setting the paint color

        //to make it focusable so that it will receive touch events properly
        setFocusable(true);

        //adding touch listener to this view
        this.setOnTouchListener(this);
    }

    //overriding the View's onDraw(..) method
    public void onDraw(Canvas canvas)
    {
        paint.setARGB(255, r, g, b);

        //drawing the circle
        canvas.drawCircle(x,y,radius,paint);

    }

    //this is the interface method of "OnTouchListener"
    public boolean onTouch(View view,MotionEvent event)
    {
        x=(int)event.getX()-(radius/2);      //some math logic to plot the circle  in exact touch place
        y=(int)event.getY()-(radius/2);
        //System.out.println("X,Y:"+"x"+","+y);      //see this output in "LogCat"
        randColor();       //calls this method to generate a color before drawing
        invalidate();      //calls onDraw method
        return true;
    }

    //this method sets a random color using Math.random()
    //Note: RGB color values ranges from 0 to 255..
    public void randColor()
    {
        r=(int)(Math.random()*255);
        g=(int)(Math.random()*255);
        b=(int)(Math.random()*255);
        //Toast.makeText(c, "r,g,b="+r+","+g+","+b,Toast.LENGTH_SHORT).show();
    }
}