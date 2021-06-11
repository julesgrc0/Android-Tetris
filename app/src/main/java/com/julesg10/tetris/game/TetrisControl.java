package com.julesg10.tetris.game;


import java.util.Random;

enum TetrisShapeType
{
    LINE,
    SQUARE,
    L,
    REVERSE_L,
    REVERSE_Z,
    Z,
    T,

    ACTIVE_LINE,
    ACTIVE_SQUARE,
    ACTIVE_L,
    ACTIVE_Z,
    ACTIVE_T,
    NONE
}

public class TetrisControl {

    private Point[] current_position;
    private TetrisShapeType current_type;

    private TetrisShapeType[] pointsMatrix;

    private double time;
    private double speed = 8;
    private final int TETRIS_WIDTH = 12;
    private final int TETRIS_HEIGHT = 22;

    public TetrisControl()
    {
        this.pointsMatrix = new TetrisShapeType[TETRIS_WIDTH * TETRIS_HEIGHT];

        for(int x = 0;x < TETRIS_WIDTH;x++)
        {
            for(int y = 0;y < TETRIS_HEIGHT;y++)
            {
                this.pointsMatrix[x + y * TETRIS_WIDTH] = TetrisShapeType.NONE;
            }
        }
    }

    public void update(double deltatime)
    {
        time += deltatime;
        if(time >= speed)
        {
            time = 0;
            this.moveY();
        }
    }

    private void movedown_lines(int start_y)
    {
        TetrisShapeType[] points = this.pointsMatrix;

        for (int y = 1; y < start_y; y++)
        {
            for (int x = 0; x < TETRIS_WIDTH;x++)
            {
                this.pointsMatrix[x + (y-1) * TETRIS_WIDTH] = points[x + y * TETRIS_WIDTH];
            }
        }
    }

    private void line_complete() {

        for (int y = 0; y < TETRIS_HEIGHT; y++) {
            boolean stop = false;
            for (int x = 0; x < TETRIS_WIDTH; x++) {
                if(this.pointsMatrix[x + y * TETRIS_WIDTH] == TetrisShapeType.NONE)
                {
                    stop = true;
                   break;
                }
            }

            if(!stop)
            {
                for (int x = 0; x < TETRIS_WIDTH; x++)
                {
                    this.pointsMatrix[x + y * TETRIS_WIDTH] = TetrisShapeType.NONE;
                }

                this.movedown_lines(y);
            }
        }

    }

    public boolean moveX(boolean add)
    {
        int addValue = add ? 1 : -1;

        Point[] futurePos =this.current_position;
        for (int i = 0; i < 4;i++)
        {
            futurePos[i].setX(this.current_position[i].getX() + addValue);
            if(!(futurePos[i].getX() < this.TETRIS_WIDTH && futurePos[i].getX() >= 0))
            {
                return false;
            }
        }
        boolean collision = false;

        for (int i = 0; i < 4;i++)
        {
            if(this.getShapeType(new Point(futurePos[i].getX(),futurePos[i].getY())) != TetrisShapeType.NONE)
            {
                collision = true;
                break;
            }
        }

        if(!collision)
        {
            this.current_position = futurePos;
            return true;
        }

        return false;
    }

    public boolean moveY()
    {

        Point[] futurePos =this.current_position;
        for (int i = 0; i < 4;i++)
        {
            futurePos[i].setY(this.current_position[i].getY()+1);
        }

        boolean collision = false;
        for (int i = 0; i < 4;i++)
        {
            if(this.getShapeType(new Point(futurePos[i].getX(),futurePos[i].getY())) != TetrisShapeType.NONE)
            {
                collision = true;
                break;
            }
        }

        if(!collision)
        {
            this.current_position = futurePos;
            return true;
        }

        return false;
    }

    private void generatePoints()
    {
        Random rand = new Random();
        int id = rand.nextInt(4);
        int startPos = (int)(TETRIS_WIDTH/2)-1;

        this.current_position = new Point[4];
        switch(TetrisShapeType.values()[id])
        {
            case LINE:
                for (int i=0;i<=4;i++)
                {
                    this.current_position[i] = new Point(startPos,i);
                }
                break;
            case SQUARE:
                this.current_position[0] = new Point(startPos,0);
                this.current_position[2] = new Point(startPos,1);
                this.current_position[1] = new Point(startPos+1,0);
                this.current_position[3] = new Point(startPos+1,1);
                break;
            case L:
                this.current_position[0] = new Point(startPos,0);
                this.current_position[1] = new Point(startPos,1);
                this.current_position[2] = new Point(startPos,2);
                this.current_position[3] = new Point(startPos+1,2);
            case T:
                this.current_position[0] = new Point(startPos,0);
                this.current_position[1] = new Point(startPos,1);
                this.current_position[2] = new Point(startPos+1,1);
                this.current_position[3] = new Point(startPos+1,2);
                break;
            case Z:
                this.current_position[0] = new Point(startPos,0);
                this.current_position[1] = new Point(startPos+1,0);
                this.current_position[2] = new Point(startPos+2,0);
                this.current_position[3] = new Point(startPos+1,1);
                break;
        }

    }

    private TetrisShapeType getShapeType(Point p)
    {
        return pointsMatrix[p.getX() + p.getY() * TETRIS_WIDTH];
    }
}
