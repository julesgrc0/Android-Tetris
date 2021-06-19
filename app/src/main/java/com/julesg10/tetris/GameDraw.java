package com.julesg10.tetris;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;

public class GameDraw {
    public GameDraw() {}

    public void drawtiles(Canvas canvas, Point pos, Size s, float tileSize, TetrisControl tetrisControl) {
        Paint p = new Paint();
        Paint b = new Paint();

        b.setARGB(255, 0, 0, 0);

        TetrisShapeType [][] points = new TetrisShapeType[tetrisControl.TETRIS_WIDTH][];
        for(int i = 0; i < tetrisControl.TETRIS_WIDTH; i++)
        {
            points[i] = tetrisControl.getActivePointsMatrix()[i].clone();
        }

        for(int x = 0;x < tetrisControl.TETRIS_WIDTH;x++)
        {
            for(int y = 0;y < tetrisControl.TETRIS_HEIGHT;y++)
            {
                int viewX = (int)((x * tileSize) + pos.x);
                int viewY = (int)((y * tileSize) + pos.y);

                Rect rect = new Rect(viewX, viewY, (int) (viewX + tileSize), (int) (viewY + tileSize));
                int[] argb = tetrisControl.getTetrisShapeColor(points[x][y]);
                p.setARGB(argb[0], argb[1], argb[2], argb[3]);
                canvas.drawRect(rect, p);

                canvas.drawLine(viewX, viewY, viewX + tileSize, viewY, b);
                canvas.drawLine(viewX, viewY, viewX, viewY + tileSize, b);
                canvas.drawLine(viewX + tileSize, viewY, viewX + tileSize, viewY + tileSize, b);
                canvas.drawLine(viewX, viewY + tileSize, viewX + tileSize, viewY + tileSize, b);
            }
        }
    }

    public double btnTime = 0.0;

    public void drawCenterXText(Canvas canvas, String text, int y, Paint paint)
    {
        canvas.drawText(text,canvas.getWidth()/2-paint.measureText(text)/2, canvas.getHeight()/2 + y,paint);
    }

    public void drawPlayButton(Canvas canvas) {
        Paint paint = new Paint();
        float x = canvas.getWidth()/2;
        float y = canvas.getHeight()/2;

        double size = 50;
        int c = (int)(btnTime * 150 )%255;
        for (int i = 1; i <= 4; i++)
        {
            paint.setARGB(255/i,c,c,c);

            canvas.drawCircle(x,y,(float)size*i,paint);
        }
    }

    public void drawHiddenInfos(Canvas canvas,int fps,int height,int width,double tileSize,double time,double globalTime,int score,double speed)
    {
        Paint paint = new Paint();
        int textSize = 30;
        paint.setTextSize(textSize);
        paint.setARGB(255,255,255,255);
        String[] texts = {
                "FPS "+Integer.toString(fps),
                Integer.toString(width)+"x"+Integer.toString(width),
                "Tile "+Double.toString(tileSize),
                "Time "+Double.toString(time),
                "Global Time "+Double.toString(globalTime),
                "Score "+Integer.toString(score),
                "Speed "+Double.toString(speed),
        };

        for (int i = 1; i <= texts.length;i++)
        {
            canvas.drawText(texts[i-1],10,textSize * i,paint);
        }
    }

    public void drawScore(Canvas canvas,int score)
    {
        String textScore = "Score: "+Integer.toString(score);
        Paint paint = new Paint();
        paint.setTextSize(40);
        paint.setARGB(255,255,255,255);
        canvas.drawText(textScore,10,40,paint);
    }
}
