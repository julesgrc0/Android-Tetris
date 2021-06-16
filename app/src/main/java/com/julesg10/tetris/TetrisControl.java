package com.julesg10.tetris;
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

    private TetrisShapeType[][] pointsMatrix;
    private double time;
    private double speed = 8;

    public final int TETRIS_WIDTH = 10;
    public  int TETRIS_HEIGHT = 20;

    public TetrisControl()
    {
    }

    public void Init()
    {
        this.pointsMatrix = new TetrisShapeType[TETRIS_WIDTH][TETRIS_HEIGHT];

        for(int x = 0;x < TETRIS_WIDTH;x++)
        {
            for(int y = 0;y < TETRIS_HEIGHT;y++)
            {
                this.pointsMatrix[x][y] = TetrisShapeType.NONE;
            }
        }
    }

    public int[] getTetrisShapeColor(TetrisShapeType type)
    {
        int[] argb = new int[4];
        argb[0] = 255;
        argb[1] = 0;
        argb[2] = 0;
        argb[3] = 0;

        switch(type) {
            case NONE:
                argb[1] = 60;
                argb[2] = 60;
                argb[3] = 60;
                break;
            case LINE:
                argb[1] = 0;
                argb[2] = 240;
                argb[3] = 240;
                break;
            case SQUARE:
                argb[1] = 240;
                argb[2] = 240;
                argb[3] = 0;
                break;
            case L:
                argb[1] = 0;
                argb[2] = 0;
                argb[3] = 240;
                break;
            case REVERSE_L:
                argb[1] = 240;
                argb[2] = 160;
                argb[3] = 0;
                break;
            case REVERSE_Z:
                argb[1] = 0;
                argb[2] = 240;
                argb[3] = 0;
                break;
            case Z:
                argb[1] = 240;
                argb[2] = 0;
                argb[3] = 0;
                break;
            case T:
                argb[1] = 160;
                argb[2] = 0;
                argb[3] = 240;
                break;
        }

        return argb;
    }

    public TetrisShapeType[][] getPointsMatrix()
    {
        return this.pointsMatrix;
    }

    private Point testPos = new Point(3,-1);

    public void update()
    {
        if(testPos.getY()+1 < this.TETRIS_HEIGHT)
        {
            this.setTile(this.testPos,TetrisShapeType.NONE);
            testPos.setY(testPos.getY()+1);
            this.setTile(this.testPos,TetrisShapeType.L);
        }
    }

    public boolean setTile(Point p,TetrisShapeType value)
    {
        if((p.getX() >= 0 && p.getX() < this.TETRIS_WIDTH) && (p.getY() >= 0 && p.getY() < this.TETRIS_HEIGHT))
        {
            this.pointsMatrix[(int)p.getX()][(int)p.getY()] = value;
            return true;
        }

        return false;
    }

    private void generatePoints() {
        Random rand = new Random();
        int id = rand.nextInt(4);

        int startPos = (int) (TETRIS_WIDTH / 2) - 1;

        this.current_position = new Point[4];
        switch (TetrisShapeType.values()[id]) {
            case LINE:

                for (int i = 0; i <= 4; i++) {
                    this.current_position[i] = new Point(startPos, i);
                }
                break;

            case SQUARE:
                this.current_position[0] = new Point(startPos, 0);
                this.current_position[2] = new Point(startPos, 1);
                this.current_position[1] = new Point(startPos + 1, 0);
                this.current_position[3] = new Point(startPos + 1, 1);
                break;

            case L:
                this.current_position[0] = new Point(startPos, 0);
                this.current_position[1] = new Point(startPos, 1);
                this.current_position[2] = new Point(startPos, 2);
                this.current_position[3] = new Point(startPos + 1, 2);

            case T:
                this.current_position[0] = new Point(startPos, 0);
                this.current_position[1] = new Point(startPos, 1);
                this.current_position[2] = new Point(startPos + 1, 1);
                this.current_position[3] = new Point(startPos + 1, 2);
                break;

            case Z:
                this.current_position[0] = new Point(startPos, 0);
                this.current_position[1] = new Point(startPos + 1, 0);
                this.current_position[2] = new Point(startPos + 2, 0);
                this.current_position[3] = new Point(startPos + 1, 1);
                break;

        }
    }

}
