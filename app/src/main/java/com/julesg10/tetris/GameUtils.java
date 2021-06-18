package com.julesg10.tetris;
import java.lang.Math;

class Size
{
    public int width, height;
}

class Point
{
    public double x;
    public double y;
    public boolean isRotationPoint = false;

    public Point(double x,double y)
    {
        this.x = x;
        this.y = y;
    }

    public Point(double x,double y,boolean isRotationPoint)
    {
        this.x = x;
        this.y = y;
        this.isRotationPoint = isRotationPoint;
    }

    public double getDistance(Point p)
    {
        double pow_x = Math.pow(x - p.x,2);
        double pow_y = Math.pow(y - p.y,2);
        return Math.sqrt(pow_x + pow_y);
    }
}

