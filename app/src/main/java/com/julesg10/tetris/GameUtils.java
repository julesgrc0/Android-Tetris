package com.julesg10.tetris;
import java.lang.Math;

class Size
{
    public int width, height;
}

class Point
{
    private double x;
    private double y;

    public Point(double x,double y)
    {
        this.x = x;
        this.y = y;
    }

    public double getDistance(Point p)
    {
        double pow_x = Math.pow(x - p.getX(),2);
        double pow_y = Math.pow(y - p.getY(),2);
        return Math.sqrt(pow_x + pow_y);
    }

    public double getX()
    {
        return this.x;
    }

    public double getY()
    {
        return this.y;
    }

    public void setX(double x)
    {
        this.x = x;
    }

    public void setY(double y)
    {
        this.y =y;
    }
}

