package com.julesg10.tetris;
import java.lang.Math;

class Size
{
    public int width, height;
}

class Point
{
    private int x;
    private int y;

    public Point(int x,int y)
    {
        this.x = x;
        this.y = y;
    }

    public double getDistance(Point p)
    {
        int pow_x = (x - p.getX()) ^ 2;
        int pow_y = (y - p.getY()) ^ 2;
        return Math.sqrt(pow_x + pow_y);
    }

    public int getX()
    {
        return this.x;
    }

    public int getY()
    {
        return this.y;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public void setY(int y)
    {
        this.y =y;
    }
}
