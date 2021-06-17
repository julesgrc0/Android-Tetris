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
import android.media.MediaPlayer;
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
    private TetrisControl tetrisControl;
    private GameDraw tetrisDraw;
    private MediaPlayer mediaPlayer;

    private TetrisClickActionType lastacion = TetrisClickActionType.NONE;

    private boolean tetrisRunning = false;
    private boolean tetrisMatrixInit = false;

    private int tetrisTileSize = 0;
    private int spaceBandSize = 2;
    private double tetrisSpeed = 100;
    private double tetrisTime = 0;
    private double gameTime = 0;
    private int tetrisSongId = 0;


    public MainSurface(Context context) {
        super(context);
        this.setFocusable(true);
        this.getHolder().addCallback(this);
        this.tetrisControl = new TetrisControl();
        this.tetrisDraw = new GameDraw();
        
        this.mediaPlayer = MediaPlayer.create(context, R.raw.tetris);
        this.mediaPlayer.setLooping(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if(!this.tetrisRunning)
            {
                this.tetrisRunning = true;
                this.tetrisControl.update();
                this.mediaPlayer.start();
            }else{
                int x = (int) event.getX();
                int y = (int) event.getY();
                this.lastacion = TetrisClickAction.getAction(this.spaceBandSize*this.tetrisTileSize,new Point(x,y));
            }
            return true;
        }
        return false;
    }

    public void update(double deltatime) {
        if(!this.tetrisRunning)
        {
            this.tetrisDraw.btnTime +=deltatime;
        }else{
            if(this.lastacion != TetrisClickActionType.NONE)
            {
                if(this.lastacion == TetrisClickActionType.LEFT)
                {
                    this.tetrisControl.left();
                }else if(this.lastacion == TetrisClickActionType.RIGHT)
                {
                    this.tetrisControl.right();
                }else if(this.lastacion == TetrisClickActionType.ROTATE)
                {
                 this.tetrisControl.rotate();
                }

                this.lastacion = TetrisClickActionType.NONE;
            }

            this.gameTime += deltatime;
            this.updateSpeed();

            this.tetrisTime += deltatime;
            if(tetrisTime >= this.tetrisSpeed/100)
            {
                this.tetrisTime = 0;
                this.tetrisControl.update();
            }
        }
    }

    private void updateSpeed()
    {
        if(this.gameTime >= 10)
        {
            this.tetrisSpeed = 50;
        }

        if(gameTime >= 60)
        {
            this.tetrisSpeed = 20;
        }

        if(gameTime >= 90)
        {
            this.tetrisSpeed = 10;
        }

        if(gameTime >= 120)
        {
            this.tetrisSpeed = 5;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if(!this.tetrisMatrixInit)
        {
            this.tetrisMatrixInit = true;

            TetrisClickAction.SURFACE_WIDTH = canvas.getWidth();
            TetrisClickAction.SURFACE_HEIGHT = canvas.getHeight();

            this.tetrisTileSize = (int)Math.round(canvas.getWidth() / 10);
            this.tetrisControl.TETRIS_HEIGHT = (int)(canvas.getHeight()/this.tetrisTileSize) - this.spaceBandSize;

            if(this.tetrisControl.TETRIS_HEIGHT < 5)
            {
                this.tetrisControl.TETRIS_HEIGHT = 5;
            }else if(this.tetrisControl.TETRIS_HEIGHT > 20)
            {
                this.tetrisControl.TETRIS_HEIGHT = 20;
            }
            this.tetrisControl.Init();
        }

        Paint paint = new Paint();
        if(!this.tetrisRunning)
        {
            paint.setARGB(255,150,150,150);
            paint.setTextSize(50);
            this.tetrisDraw.drawPlayButton(canvas);
            canvas.drawText("Press to start",canvas.getWidth()/2-145, canvas.getHeight()/2 + 290,paint);
        }else {
            Size s = new Size();
            s.width = canvas.getWidth();
            s.height = this.tetrisControl.TETRIS_HEIGHT * this.tetrisTileSize;

            Point pos = new Point(0, 0);
            this.tetrisDraw.drawtiles(canvas, pos, s, this.tetrisTileSize, this.tetrisControl);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        this.gameThread = new GameThread(this, holder);
        this.gameThread.setRunning(true);
        this.gameThread.start();
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        this.tetrisMatrixInit = true;

        TetrisClickAction.SURFACE_WIDTH = width;
        TetrisClickAction.SURFACE_HEIGHT = height;

        this.tetrisTileSize = (int)Math.round(width / 10);
        this.tetrisControl.TETRIS_HEIGHT = (int)(height/this.tetrisTileSize) - this.spaceBandSize;

        if(this.tetrisControl.TETRIS_HEIGHT < 5)
        {
            this.tetrisControl.TETRIS_HEIGHT = 5;
        }else if(this.tetrisControl.TETRIS_HEIGHT > 20)
        {
            this.tetrisControl.TETRIS_HEIGHT = 20;
        }

        this.tetrisControl.Init();
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