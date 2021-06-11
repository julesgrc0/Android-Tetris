package com.julesg10.tetris.game;


import java.util.Random;

enum TetrisShapeType
{
    LINE,
    SQUARE,
    L,
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
            }
        }

    }

    private void moveY()
    {
        for (int i = 0; i < 4;i++)
        {
            this.current_position[i].setY(this.current_position[i].getY()+1);
        }
    }

    private void generatePoints()
    {
        Random rand = new Random();
        int id = rand.nextInt(4);
        int startPos = (int) TETRIS_WIDTH/2;

        this.current_position = new Point[4];
        switch(id)
        {
            case 0:
                for (int i=0;i<=4;i++)
                {
                    this.current_position[i] = new Point(startPos,i);
                }
                break;
            case 1:
                this.current_position[0] = new Point(startPos,0);
                this.current_position[2] = new Point(startPos,1);
                this.current_position[1] = new Point(startPos+1,0);
                this.current_position[3] = new Point(startPos+1,1);
                break;
            case 2:
                this.current_position[0] = new Point(startPos,0);
                this.current_position[1] = new Point(startPos,1);
                this.current_position[2] = new Point(startPos,2);
                this.current_position[3] = new Point(startPos+1,2);
            case 3:
                this.current_position[0] = new Point(startPos,0);
                this.current_position[1] = new Point(startPos,1);
                this.current_position[2] = new Point(startPos+1,1);
                this.current_position[3] = new Point(startPos+1,2);
                break;
            case 4:
                this.current_position[0] = new Point(startPos,0);
                this.current_position[1] = new Point(startPos+1,0);
                this.current_position[2] = new Point(startPos+2,0);
                this.current_position[3] = new Point(startPos+1,1);
                break;
        }

    }


   /* public void move(boolean x,boolean y)
    {
        current.setX(current.getX() + (x ? 1 : -1));
        current.setY(current.getY() + (y ? 1 : -1));
    }
    */

    private TetrisShapeType getShapeType(Point p)
    {
        return pointsMatrix[p.getX() + p.getY() * TETRIS_WIDTH];
    }
}
