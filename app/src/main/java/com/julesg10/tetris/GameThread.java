package com.julesg10.tetris;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameThread extends Thread {

    private boolean running;
    private MainSurface gameSurface;
    private SurfaceHolder surfaceHolder;

    public GameThread(MainSurface gameSurface, SurfaceHolder surfaceHolder)  {
        this.gameSurface= gameSurface;
        this.surfaceHolder= surfaceHolder;
    }

    @Override
    public void run()  {
        long start = System.nanoTime();
        long deltastart = System.nanoTime();
        int frames = 0;
        while(running)  {
            double deltatime = (System.nanoTime() - deltastart) * Math.pow(10,-9);
            double ms = (System.nanoTime() - start) * Math.pow(10,-6);

            deltastart = System.nanoTime();
            frames++;

            if(ms >= 1000)
            {
                start = System.nanoTime();
                frames = 0;
            }

            this.canvasUpdate(deltatime);
        }
    }

    private void canvasUpdate(double deltatime)
    {
        Canvas canvas= null;
        try {
            canvas = this.surfaceHolder.lockCanvas();
            synchronized (canvas)  {
                this.gameSurface.update(deltatime);
                this.gameSurface.draw(canvas);
            }
        }catch(Exception e)  {
        } finally {
            if(canvas!= null)  {
                this.surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    public void setRunning(boolean running)  {
        this.running= running;
    }
}
