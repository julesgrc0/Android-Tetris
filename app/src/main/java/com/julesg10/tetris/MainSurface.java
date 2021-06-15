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
import android.support.annotation.RequiresApi;
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
    private TetrisControl tetrisControl;
    private boolean tetrisRunning = false;

    public MainSurface(Context context) {
        super(context);
        this.setFocusable(true);
        this.getHolder().addCallback(this);
        this.tetrisControl = new TetrisControl();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if(!this.tetrisRunning)
            {
                this.tetrisRunning = true;
            }else{
                int x = (int) event.getX();
                int y = (int) event.getY();
            }
            return true;
        }
        return false;
    }

    public void update(double deltatime) {
        if(!this.tetrisRunning)
        {
            GameDraw.btnTime +=deltatime;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        Paint paint = new Paint();
        if(!this.tetrisRunning)
        {
            paint.setARGB(255,150,150,150);
            paint.setTextSize(50);
            GameDraw.drawPlayButton(canvas);
            canvas.drawText("Press to start",canvas.getWidth()/2-145, canvas.getHeight()/2 + 290,paint);
        }else {
            int tileSize = (int) Math.round(canvas.getWidth() / 12);

            Size s = new Size();
            s.width = canvas.getWidth() - tileSize;
            s.height = canvas.getHeight() - tileSize;

            Point pos = new Point(tileSize, tileSize / 2);
            GameDraw.drawtiles(canvas, pos, s, tileSize, this.tetrisControl);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        this.gameThread = new GameThread(this, holder);
        this.gameThread.setRunning(true);
        this.gameThread.start();
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                this.gameThread.setRunning(false);
                this.gameThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}