package com.julesg10.tetris;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;

public class GameDraw {

    public static void drawtiles(Canvas canvas, Point pos, Size s, float tileSize, TetrisControl tetrisControl) {
        Paint p = new Paint();
        Paint b = new Paint();

        b.setARGB(255, 0, 0, 0);
        TetrisShapeType[][] points = tetrisControl.getPointsMatrix();

        for(int x = 0;x < tetrisControl.TETRIS_WIDTH;x++)
        {
            for(int y = 0;y < tetrisControl.TETRIS_HEIGHT;y++)
            {
                int viewX = (int)((x * tileSize) + pos.getX());
                int viewY = (int)((y * tileSize) + pos.getY());

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

    public static double btnTime = 0.0;
    public static boolean reverseBtnTime = false;

    public static void drawPlayButton(Canvas canvas) {
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
}
