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
        if(this.position.x < obj.getPosition().x + obj.getSize().width &&
                this.position.x + size.width > obj.getPosition().x &&
                this.position.y < obj.getPosition().y + obj.getSize().height &&
                this.position.y + size.height > obj.getPosition().y)
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
