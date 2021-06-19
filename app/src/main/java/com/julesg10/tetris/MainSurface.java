package com.julesg10.tetris;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MainSurface extends SurfaceView implements SurfaceHolder.Callback {

    private GameThread gameThread;
    private TetrisControl tetrisControl;
    private GameDraw tetrisDraw;
    private MediaPlayer mediaPlayer;
    private SettingStorage settingStorage;

    private boolean tetrisRunning = false;
    private boolean tetrisMatrixInit = false;

    private int tetrisTileSize = 0;
    private final int spaceBandSize = 2;

    private double tetrisTime = 0;
    private double gameTime = 0;
    private boolean showHiddenInfos = false;
    private boolean showScore = false;
    private boolean showGameOver = false;


    public MainSurface(Context context) {
        super(context);
        this.setFocusable(true);
        this.getHolder().addCallback(this);
        this.tetrisControl = new TetrisControl();
        this.tetrisDraw = new GameDraw();
        this.settingStorage = new SettingStorage(context);
        if(this.settingStorage.load())
        {
           if(this.settingStorage.get(0) == "true")
           {
               this.showScore = true;
           }

            if(this.settingStorage.get(1) == "true")
            {
                this.showHiddenInfos = true;
            }

            if(this.settingStorage.get(2) == "false")
            {
                this.tetrisDraw.detailDrawMode = false;
            }
        }
        this.mediaPlayer = MediaPlayer.create(context, R.raw.tetris);
        this.mediaPlayer.setLooping(true);
    }

    private long downstartTime = 0;


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP)
        {
            long pressTime = System.currentTimeMillis() - this.downstartTime;
            this.downstartTime = 0;
            if(pressTime >= 500)
            {
                if(pressTime >= 1000 && (event.getX() >= 0 && event.getX() <= 50 && event.getY() >= 0 && event.getY() <= 50))
                {
                    showHiddenInfos = !showHiddenInfos;
                    this.settingStorage.set(1,showHiddenInfos ? "true" : "false");
                }else{
                    showScore = !showScore;
                    showHiddenInfos = false;
                    this.settingStorage.set(0,showScore ? "true" : "false");
                    this.settingStorage.set(1,"false");
                }
            }
        }
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            this.downstartTime = System.currentTimeMillis();

            if(!this.tetrisRunning)
            {
                this.tetrisRunning = true;
                this.tetrisControl.update();
                this.mediaPlayer.start();
            }else{
                int x = (int) event.getX();
                int y = (int) event.getY();
                switch (TetrisClickAction.getAction(new Point(x,y))) {
                    case LEFT:
                        this.tetrisControl.left();
                        break;
                    case RIGHT:
                        this.tetrisControl.right();
                        break;
                    case ROTATE:
                        this.tetrisControl.rotate();
                        break;
                }
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
            if(this.tetrisControl.isGameOver())
            {
                this.showGameOver = true;
                this.tetrisRunning = false;
            }

            this.gameTime += deltatime;
            this.updateSpeed();

            this.tetrisTime += deltatime;
            if(tetrisTime >= this.tetrisControl.tetrisSpeed/100)
            {
                this.tetrisTime = 0;
                this.tetrisControl.update();
            }
        }
    }

    private void updateSpeed()
    {
        if(gameTime >= 30)
        {
            this.tetrisControl.tetrisSpeed  = 35;
        }

        if(gameTime >= 60)
        {
            this.tetrisControl.tetrisSpeed = 25;
        }

        if(gameTime >= 120)
        {
            this.tetrisControl.tetrisSpeed = 15;
        }

        if(gameTime >= 180)
        {
            this.tetrisControl.tetrisSpeed = 8;
        }

        if(gameTime >= 300)
        {
            this.tetrisControl.tetrisSpeed = 5;
        }

        if(gameTime >= 600)
        {
            this.tetrisControl.tetrisSpeed = 0.5;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if(!this.tetrisMatrixInit)
        {
            this.tetrisMatrixInit = true;

            this.tetrisTileSize = (int)Math.round(canvas.getWidth() / 10);
            this.tetrisControl.TETRIS_HEIGHT = (int)(canvas.getHeight()/this.tetrisTileSize) - this.spaceBandSize;

            TetrisClickAction.SURFACE_WIDTH = canvas.getWidth();
            TetrisClickAction.SECTION_HEIGHT = this.tetrisControl.TETRIS_HEIGHT * this.tetrisTileSize;

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
            if(this.showGameOver)
            {
                paint.setARGB(255,150,150,150);
                paint.setTextSize(50);
                this.tetrisDraw.drawPlayButton(canvas);
                this.tetrisDraw.drawCenterXText(canvas,"Score "+Integer.toString(this.tetrisControl.getScore()),-240,paint);
                this.tetrisDraw.drawCenterXText(canvas,"GameOver Press to restart",290,paint);

                this.tetrisTime = 0;
                this.gameTime = 0;
                this.showHiddenInfos = false;
                this.showScore = false;
                this.tetrisControl.reset();
            }else{
                paint.setARGB(255,150,150,150);
                paint.setTextSize(50);
                this.tetrisDraw.drawPlayButton(canvas);
                this.tetrisDraw.drawCenterXText(canvas,"Press to start",290,paint);
            }

        }else {
            Size s = new Size();
            s.width = canvas.getWidth();
            s.height = this.tetrisControl.TETRIS_HEIGHT * this.tetrisTileSize;

            Point pos = new Point(0, 0);
            this.tetrisDraw.drawtiles(canvas, pos, s, this.tetrisTileSize, this.tetrisControl);
            this.tetrisDraw.drawActionButtons(canvas,TetrisClickAction.SECTION_HEIGHT);

            if(showScore && !showHiddenInfos)
            {
                this.tetrisDraw.drawScore(canvas,this.tetrisControl.getScore());
            }else if(showHiddenInfos)
            {
                this.tetrisDraw.drawHiddenInfos(canvas,GameThread.getFPS(),canvas.getWidth(),canvas.getHeight(),this.tetrisTileSize,this.tetrisTime,this.gameTime,this.tetrisControl.getScore(),this.tetrisControl.tetrisSpeed);
            }
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

        this.tetrisTileSize = (int)Math.round(width / 10);
        this.tetrisControl.TETRIS_HEIGHT = (int)(height/this.tetrisTileSize) - this.spaceBandSize;

        TetrisClickAction.SURFACE_WIDTH = width;
        TetrisClickAction.SECTION_HEIGHT = this.tetrisControl.TETRIS_HEIGHT * this.tetrisTileSize;

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
            }
        }
    }

    public void saveSettings()
    {
        this.settingStorage.save();
    }
}