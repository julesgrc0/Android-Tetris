package com.julesg10.tetris;

import android.graphics.Color;

import java.util.Arrays;
import java.util.Random;

enum TetrisShapeType
{
    LINE,
    SQUARE,
    L,
    Z,
    T,
    REVERSE_L,
    REVERSE_Z,
    NONE
}

enum TetrisClickActionType
{
    LEFT,
    RIGHT,
    ROTATE,
    NONE
}

class TetrisClickAction
{
    public static int SECTION_HEIGHT = 0;
    public static int SURFACE_WIDTH = 0;

    public static TetrisClickActionType getAction(Point pos)
    {
        if(pos.y >= SECTION_HEIGHT)
        {
            int size = SURFACE_WIDTH/3;
            if(pos.x >= 0 && pos.x < size)
            {
                return TetrisClickActionType.LEFT;
            }else if(pos.x >= size && pos.x < size*2)
            {
                return TetrisClickActionType.ROTATE;
            }else  if(pos.x >= size*2 && pos.x <= SURFACE_WIDTH)
            {
                return TetrisClickActionType.RIGHT;
            }
        }
        return TetrisClickActionType.NONE;
    }
}

class TetrisARGB{
    public int a = 255;
    public int r = 255;
    public int g = 255;
    public int b = 255;


    public TetrisARGB(int a, int r, int g, int b)
    {
        this.a = a;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public TetrisARGB fromHexString(String colorStr) {
        this.r = Integer.valueOf(colorStr.substring(1, 3), 16);
        this.g = Integer.valueOf(colorStr.substring(3, 5), 16);
        this.b = Integer.valueOf(colorStr.substring(5, 7), 16);
        return this;
    }

    public int[] getArray()
    {
        int[] value = new int[4];
        value[0] = this.a;
        value[1] = this.r;
        value[2] = this.g;
        value[3] = this.b;

        return value;
    }
}

public class TetrisControl {

    private Point[] current_position = new Point[4];
    private int current_y = 0;
    private int current_x = 0;
    private int current_state = 0;
    private TetrisShapeType current_type;
    private final Random rand = new Random();

    private TetrisShapeType[][] pointsMatrix;
    public final int TETRIS_WIDTH = 10;
    public int TETRIS_HEIGHT = 20;

    private int tetrisScore = 0;
    public double tetrisSpeed = 50;
    private boolean isOver = false;

    private final Point[] SQUARE_Points = {new Point(3, -2), new Point(4, -2), new Point(3, -1), new Point(4, -1)};

    private final Point[] LINE_Points = {new Point(4, -1), new Point(4, -2), new Point(4, -3), new Point(4, -4)};
    private final Point[] LINE_s1_Points = {new Point(2, -1), new Point(3, -1), new Point(4, -1), new Point(5, -1)};

    private final Point[] T_Points =    {new Point(3, -2), new Point(4, -2), new Point(5, -2), new Point(4, -1)};
    private final Point[] T_s1_Points = {new Point(4, -3), new Point(4, -2), new Point(4, -1), new Point(5, -2)};
    private final Point[] T_s2_Points = {new Point(3, -1), new Point(4, -1), new Point(5, -1), new Point(4, -2)};
    private final Point[] T_s3_Points = {new Point(4, -3), new Point(4, -2), new Point(4, -1), new Point(3, -2)};

    private final Point[] L_Points = {new Point(3, -2), new Point(4, -2), new Point(5, -2), new Point(5, -1)};
    private final Point[] L_s1_Points = {new Point(3, -1), new Point(4, -3), new Point(4, -2), new Point(4, -1)};
    private final Point[] L_s2_Points = {new Point(3, -2), new Point(3, -1), new Point(4, -1), new Point(5, -1)};
    private final Point[] L_s3_Points = {new Point(3, -1), new Point(3, -2), new Point(3, -3), new Point(4, -3)};

    private final Point[] REVERSE_L_Points = {new Point(3, -2), new Point(4, -2), new Point(5, -2), new Point(3, -1)};
    private final Point[] REVERSE_L_s1_Points = {new Point(5, -1), new Point(4, -3), new Point(4, -2), new Point(4, -1)};
    private final Point[] REVERSE_L_s2_Points = {new Point(5, -2), new Point(3, -1), new Point(4, -1), new Point(5, -1)};
    private final Point[] REVERSE_L_s3_Points =  {new Point(3, -1), new Point(3, -2), new Point(3, -3), new Point(4, -1)};

    private final Point[] Z_Points = {new Point(4, -2), new Point(5, -2), new Point(3, -1), new Point(4, -1)};
    private final Point[] Z_s1_Points = {new Point(5, -1), new Point(5, -2), new Point(4, -2), new Point(4, -3)};
    private final Point[] Z_s2_Points = {new Point(3, -1), new Point(4, -1), new Point(4, -2), new Point(5, -2)};
    private final Point[] Z_s3_Points = {new Point(3, -3), new Point(3, -2), new Point(4, -2), new Point(4, -1)};

    private final Point[] REVERSE_Z_Points =   {new Point(3, -2), new Point(4, -2), new Point(4, -1), new Point(5, -1)};
    private final Point[] REVERSE_Z_s1_Points =  {new Point(4, -1), new Point(4, -2), new Point(5, -2), new Point(5, -3)};
    private final Point[] REVERSE_Z_s2_Points =  {new Point(3, -2), new Point(4, -2), new Point(4, -1), new Point(5, -1)};
    private final Point[] REVERSE_Z_s3_Points =  {new Point(3, -1), new Point(3, -2), new Point(4, -2), new Point(4, -3)};

    public TetrisControl()
    {

    }

    public void Init() {
        this.generatePoints();

        this.pointsMatrix = new TetrisShapeType[TETRIS_WIDTH][TETRIS_HEIGHT];

        for (int x = 0; x < TETRIS_WIDTH; x++) {
            for (int y = 0; y < TETRIS_HEIGHT; y++) {
                this.pointsMatrix[x][y] = TetrisShapeType.NONE;
            }
        }
    }

    public boolean isGameOver()
    {
        return isOver;
    }

    private void setCurrentPosition(Point[] p)
    {
        for(int i = 0; i < p.length; i++)
        {
            this.current_position[i] = new Point(p[i].x,p[i].y);
        }
    }

    public int getScore() {
        return this.tetrisScore;
    }

    public int[][] getTetrisShapeColor(TetrisShapeType type) {
        int[][] argb = new int[5][4];
        TetrisARGB tcolor = new TetrisARGB(255,0,0,0);

        switch (type) {
            case NONE:
                /*
                argb[0] = tcolor.fromHexString("#777777").getArray();// center
                argb[1] = tcolor.fromHexString("#a0a0a0").getArray();// left
                argb[2] = tcolor.fromHexString("#d6d6d6").getArray();// top
                argb[3] = tcolor.fromHexString("#535353").getArray();// right
                argb[4] = tcolor.fromHexString("#232323").getArray();// bottom
                */
                break;
            case LINE:
                argb[0] = tcolor.fromHexString("#00ffff").getArray();
                argb[1] = tcolor.fromHexString("#4dffff").getArray();
                argb[2] = tcolor.fromHexString("#b3ffff").getArray();
                argb[3] = tcolor.fromHexString("#00bcbc").getArray();
                argb[4] = tcolor.fromHexString("#004c4c").getArray();
                break;
            case SQUARE:
                argb[0] = tcolor.fromHexString("#ffff00").getArray();
                argb[1] = tcolor.fromHexString("#ffff4d").getArray();
                argb[2] = tcolor.fromHexString("#ffffb3").getArray();
                argb[3] = tcolor.fromHexString("#bcbc00").getArray();
                argb[4] = tcolor.fromHexString("#4c4c00").getArray();
                break;
            case L:
                argb[0] = tcolor.fromHexString("#0000aa").getArray();
                argb[1] = tcolor.fromHexString("#4d4dc4").getArray();
                argb[2] = tcolor.fromHexString("#b3b3e6").getArray();
                argb[3] = tcolor.fromHexString("#000077").getArray();
                argb[4] = tcolor.fromHexString("#000033").getArray();
                break;
            case REVERSE_L:
                argb[0] = tcolor.fromHexString("#ff7700").getArray();
                argb[1] = tcolor.fromHexString("#ffa04d").getArray();
                argb[2] = tcolor.fromHexString("#ffcb9d").getArray();
                argb[3] = tcolor.fromHexString("#b25300").getArray();
                argb[4] = tcolor.fromHexString("#4c2300").getArray();
                break;
            case REVERSE_Z:
                argb[0] = tcolor.fromHexString("#00ff00").getArray();
                argb[1] = tcolor.fromHexString("#4dff4d").getArray();
                argb[2] = tcolor.fromHexString("#b3ffb3").getArray();
                argb[3] = tcolor.fromHexString("#00bc00").getArray();
                argb[4] = tcolor.fromHexString("#004c00").getArray();
                break;
            case Z:
                argb[0] = tcolor.fromHexString("#ff0000").getArray();
                argb[1] = tcolor.fromHexString("#ff4d4d").getArray();
                argb[2] = tcolor.fromHexString("#ffb3b3").getArray();
                argb[3] = tcolor.fromHexString("#b20000").getArray();
                argb[4] = tcolor.fromHexString("#4c0000").getArray();
                break;
            case T:
                argb[0] = tcolor.fromHexString("#cc00cc").getArray();
                argb[1] = tcolor.fromHexString("#d943d9").getArray();
                argb[2] = tcolor.fromHexString("#f0b3f0").getArray();
                argb[3] = tcolor.fromHexString("#8e008e").getArray();
                argb[4] = tcolor.fromHexString("#3d003d").getArray();
                break;
        }

        return argb;
    }

    public TetrisShapeType[][] getPointsMatrix() {
        return this.pointsMatrix;
    }

    public TetrisShapeType[][] getActivePointsMatrix() {
        TetrisShapeType[][] activematrix = new TetrisShapeType[this.TETRIS_WIDTH][];

        for (int i = 0; i < this.TETRIS_WIDTH; i++) {
            activematrix[i] = this.pointsMatrix[i].clone();
        }

        for (int i = 0; i < this.current_position.length; i++) {
            if (this.isOnMatrix(this.current_position[i])) {
                activematrix[(int) this.current_position[i].x][(int) this.current_position[i].y] = this.current_type;
            }
        }
        return activematrix;
    }

    public TetrisShapeType getActiveType() {
        return this.current_type;
    }

    public Point[] getActivePositions() {
        return this.current_position;
    }

    public void update() {
            this.moveDown();
    }

    public boolean setTile(Point p, TetrisShapeType value) {
        if (this.isOnMatrix(p)) {
            this.pointsMatrix[(int) p.x][(int) p.y] = value;
            return true;
        }

        return false;
    }

    public TetrisShapeType getTile(Point p) {
        if (this.isOnMatrix(p)) {
            return this.pointsMatrix[(int) p.x][(int) p.y];
        }
        return TetrisShapeType.NONE;
    }

    public boolean isOnMatrix(Point p) {
        return (p.x >= 0 && p.x < this.TETRIS_WIDTH) && (p.y >= 0 && p.y < this.TETRIS_HEIGHT);
    }


    private boolean moveX(boolean left) {
        int add = left ? -1 : 1;
        boolean valide = true;

        for (int i = 0; i < this.current_position.length; i++) {
            if(!(this.current_position[i].x+add < 0)  && !(this.current_position[i].x+add >= this.TETRIS_WIDTH))
            {
                Point tmp = new Point(this.current_position[i].x+add,this.current_position[i].y);
                if(this.getTile(tmp) != TetrisShapeType.NONE)
                {
                    valide = false;
                    break;
                }
            }else{
                valide = false;
                break;
            }
        }

        if(valide)
        {
            this.current_x += add;
            for (int i = 0; i < this.current_position.length; i++) {
                this.current_position[i].x += add;
            }

            return true;
        }else{
            return false;
        }
    }

    public boolean left() {
        return this.moveX(true);
    }

    public boolean right() {
        return this.moveX(false);
    }

    public boolean rotate() {
        boolean valide = false;
        switch (this.current_type)
        {
            case LINE:
                if(this.current_state == 0)
                {
                    this.current_state = 1;
                    valide = this.changeState(this.LINE_Points);
                }else{
                    this.current_state = 0;
                    valide = this.changeState(this.LINE_s1_Points);
                }
                break;
            case Z:
                Point[][] Zpoints = {this.Z_Points,this.Z_s1_Points,this.Z_s2_Points,this.Z_s3_Points};
                valide = this.SwitchStates(Zpoints);
                break;
            case T:
                Point[][] Tpoints = {this.T_Points,this.T_s1_Points,this.T_s2_Points,this.T_s3_Points};
                valide = this.SwitchStates(Tpoints);
                break;
            case L:
                Point[][] Lpoints = {this.L_Points,this.L_s1_Points,this.L_s2_Points,this.L_s3_Points};
                valide = this.SwitchStates(Lpoints);
                break;
            case REVERSE_L:
                Point[][] RLpoints = {this.REVERSE_L_Points,this.REVERSE_L_s1_Points,this.REVERSE_L_s2_Points,this.REVERSE_L_s3_Points};
                valide = this.SwitchStates(RLpoints);
                break;
            case REVERSE_Z:
                Point[][] RZpoints = {this.REVERSE_Z_Points,this.REVERSE_Z_s1_Points,this.REVERSE_Z_s2_Points,this.REVERSE_Z_s3_Points};
                valide = this.SwitchStates(RZpoints);
                break;
        }

        return valide;
    }

    private boolean SwitchStates(Point[][] points)
    {
        boolean valide = this.changeState(points[this.current_state]);
        this.current_state++;
        if(this.current_state > 3)
        {
            this.current_state = 0;
        }

        return valide;
    }

    private boolean changeState(Point[] p)
    {
        boolean valide = true;
        for(int i = 0; i < p.length; i++)
        {
            Point tmp = new Point(p[i].x + this.current_x,p[i].y + this.current_y);
            if(this.getTile(tmp) != TetrisShapeType.NONE)
            {
                valide = false;
                break;
            }
        }
        if(valide)
        {
            for(int i = 0; i < p.length; i++) {
                this.current_position[i] = new Point(p[i].x + this.current_x,p[i].y + this.current_y);
            }
        }
        return valide;
    }

    private boolean moveDown() {
        boolean valide = true;
        try{
            for (int i = 0; i < this.current_position.length; i++) {
                if(!(this.current_position[i].y+1 >= this.TETRIS_HEIGHT))
                {
                    Point tmp = new Point(this.current_position[i].x,this.current_position[i].y+1);
                    if(this.getTile(tmp) != TetrisShapeType.NONE)
                    {
                        valide = false;
                        break;
                    }
                }else{
                    valide = false;
                    break;
                }
            }
        }catch(Exception e)
        {
            valide = false;
            e.printStackTrace();
        }


        if(valide)
        {
            this.current_y++;
            for (int i = 0; i < this.current_position.length; i++) {
                this.current_position[i].y += 1;
            }
            return true;
        }else{
            this.stopCurrent();
            return false;
        }
    }

    private void stopCurrent() {
        for (int i = 0; i < this.current_position.length; i++)
        {
            if(!this.setTile(this.current_position[i],this.current_type))
            {
                if(this.current_position[i].y < 0)
                {
                    this.isOver = true;
                }
            }
        }
        this.generatePoints();
        this.checkLines();
    }


    private void checkLines() {
        int combo = 0;
        for (int y = 0;y < this.TETRIS_HEIGHT;y ++)
        {
            boolean stop = false;
            for (int x = 0; x < this.TETRIS_WIDTH; x++)
            {
                if(this.getTile(new Point(x,y)) == TetrisShapeType.NONE)
                {
                    stop = true;
                    break;
                }
            }

            if(!stop)
            {
                combo++;
                this.tetrisScore += 10 * combo;
                this.clearLine(y);
                y--;
            }
        }
    }

    private void clearLine(int line_y)
    {
        for (int y = line_y;y != 0; y--)
        {
            for (int x = 0;x < this.TETRIS_WIDTH; x++)
            {
                this.setTile(new Point(x,y),this.getTile(new Point(x,y-1)));
            }
        }
    }

    private void generatePoints() {
        this.current_y = 0;
        this.current_x = 0;
        this.current_state = 1;

        int id = this.rand.nextInt(7);

        this.current_type = TetrisShapeType.values()[id];
        switch (this.current_type) {
            case SQUARE:
                this.setCurrentPosition(this.SQUARE_Points);
                break;
            case L:
                this.setCurrentPosition(this.L_Points);
                break;
            case LINE:
                this.setCurrentPosition(this.LINE_Points);
                break;
            case Z:
                this.setCurrentPosition(this.Z_Points);
                break;
            case REVERSE_L:
                this.setCurrentPosition(this.REVERSE_L_Points);
                break;
            case REVERSE_Z:
                this.setCurrentPosition(this.REVERSE_Z_Points);
                break;
            case T:
                this.setCurrentPosition(this.T_Points);
                break;
        }
    }

    public void reset()
    {
        this.Init();
        this.tetrisSpeed = 50;
        this.tetrisScore = 0;
        this.isOver = false;
    }
}
