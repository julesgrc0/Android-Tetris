package com.julesg10.tetris;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public abstract class GameObject {

    private Size size;
    private Point position;

    public GameObject(Point position,Size size)
    {
        this.position = position;
        this.size = size;
    }

    public boolean checkCollision(GameObject obj)
    {
        if(this.position.getX() < obj.getPosition().getX() + obj.getSize().width &&
                this.position.getX() + size.width > obj.getPosition().getX() &&
                this.position.getY() < obj.getPosition().getY() + obj.getSize().height &&
                this.position.getY() + size.height > obj.getPosition().getY())
        {
            return true;
        }

        return false;
    }

    public void update(float deltatime)
    {

    }

    public void draw(Canvas canvas)
    {

    }

    public Point getPosition() {
        return position;
    }

    public Size getSize() {
        return size;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public void setSize(Size size) {
        this.size = size;
    }
}
