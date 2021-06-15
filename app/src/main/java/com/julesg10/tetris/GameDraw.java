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
        for (float x = (float) pos.getX(); x < s.width; x += tileSize) {
            for (float y = (float) pos.getY(); y < s.height; y += tileSize) {


                int t_x = (int) (x / tileSize);
                int t_y = (int) (y / tileSize);
                if(t_x < tetrisControl.TETRIS_WIDTH && t_y < tetrisControl.TETRIS_HEIGHT)
                {
                    Rect rect = new Rect((int) x, (int) y, (int) (x + tileSize), (int) (y + tileSize));

                    int[] argb = tetrisControl.getTetrisShapeColor(points[t_x][t_y]);
                    p.setARGB(argb[0], argb[1], argb[2], argb[3]);

                    canvas.drawRect(rect, p);

                    canvas.drawLine(x, y, x + tileSize, y, b);
                    canvas.drawLine(x, y, x, y + tileSize, b);
                    canvas.drawLine(x + tileSize, y, x + tileSize, y + tileSize, b);
                    canvas.drawLine(x, y + tileSize, x + tileSize, y + tileSize, b);
                }

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
