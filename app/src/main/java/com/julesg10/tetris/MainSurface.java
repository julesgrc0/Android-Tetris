package com.julesg10.tetris;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainSurface extends SurfaceView implements SurfaceHolder.Callback {

    private GameThread gameThread;
    private AudioManager audioManager;

    public MainSurface(Context context) {
        super(context);
        this.setFocusable(true);
        this.getHolder().addCallback(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            int x = (int) event.getX();
            int y = (int) event.getY();

            return true;
        }
        return false;
    }

    private double c = 1;

    private double del = 0;

    public void update(double deltatime) {
        this.c += 30 * deltatime;
        this.del = 30 * deltatime;
        if(this.c>50)
        {
            this.c = 1;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        int tileSize = 0;
        tileSize += Math.round(canvas.getWidth()/c);
        Size s = new Size();
        s.width = canvas.getWidth() - tileSize*2;
        s.height = canvas.getHeight() - tileSize*2;
        Point pos = new Point(tileSize,tileSize);
        this.drawtiles(canvas,pos,s,tileSize);

        Paint p = new Paint();
        p.setARGB(255, 255, 255, 255);
        p.setStyle(Paint.Style.FILL);
        p.setTextSize(40);
        canvas.drawText(Double.toString(this.del),10,30,p);
    }

    private void drawtiles(Canvas canvas,Point pos,Size s,int tileSize)
    {
        Paint p = new Paint();
        Paint b = new Paint();
        b.setARGB(255, 0, 0, 0);

        for(int x = pos.getX();x < s.width;x+= tileSize)
        {
            for(int y = pos.getY();y < s.height;y+= tileSize)
            {
                Rect rect = new Rect(x,y,x+tileSize,y+tileSize);
                p.setARGB(255,140,140,140);
                canvas.drawRect(rect,p);

                canvas.drawLine(x,y,x+tileSize,y,b);
                canvas.drawLine(x,y,x,y+tileSize,b);
                canvas.drawLine(x+tileSize,y,x+tileSize,y+tileSize,b);
                canvas.drawLine(x,y+tileSize,x+tileSize,y+tileSize,b);
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        this.gameThread = new GameThread(this,holder);
        this.gameThread.setRunning(true);
        this.gameThread.start();
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while(retry)
        {
            try {
                this.gameThread.setRunning(false);
                this.gameThread.join();
            }catch(InterruptedException e)  {
                e.printStackTrace();
            }
        }
    }

}