package Tetris;

import tetrisblocks.*;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class GameArea extends JPanel
{
    private int gridrows;
    private int gridcolumns;
    private int gridcellsize;

    private Color[][] background;

    private TetrisBlock block;
    private TetrisBlock[] blocks;
    public GameArea(JPanel placeholder, int columns)
    {
        placeholder.setVisible(false);
        this.setBounds(150, 150, 150, 150);
        this.setBackground(Color.white);
        this.setForeground(Color.black);
        gridcolumns = columns;
        gridcellsize = this.getBounds().width / gridcolumns;
        gridrows = this.getBounds().height/gridcellsize;

        background = new Color[gridrows][gridcolumns];

        blocks = new TetrisBlock[]{new IShape(), new JShape(), new LShape(), new OShape(), new SShape(), new TShape(), new ZShape()};
    }

    public void spawnBlock()
    {
        Random rnd = new Random();


        block = blocks[rnd.nextInt(blocks.length)];
        block.spawn(gridcolumns);
    }
    public boolean isBlockOutOfBounds()
    {
        if(block.getY() <0)
        {
            block = null;
            return true;
        }

        return false;
    }

    public boolean moveBlockDown()
    {
        if(checkBottom() == false)
        {
            return false;
        }
        block.moveDown();
        repaint();
        return true;
    }

    public void moveBlockRight()
    {
        if(block == null)return;
        if(!checkRight()) return;
        block.moveRight();
        repaint();
    }
    public void moveBlockLeft()
    {
        if(block == null)return;
        if(!checkLeft()) return;
        block.moveLeft();
        repaint();
    }
    public void dropBlock()
    {
        if(block == null)return;
        while(checkBottom() )
        {
            block.moveDown();
        }
        repaint();
    }
    public void rotateBlock()
    {
        if(block == null) return;
        block.rotate();

        if(block.getLeftEdge()<0 )
            block.setX(0);
        if(block.getRightEdge() >= gridcolumns)
        {
            block.setX(gridcolumns - block.getWidth());
        }
        if(block.getBottomEdge()>=gridrows)
        {
            block.setY(gridrows - block.getHeight());
        }
        int[][]shape = block.getShape();
        int w= block.getWidth();
        int h= block.getHeight();
        for(int row = 0; row < h; row++)
        {
            for( int col = 0; col < w; col++)
            {
                if(shape[row][col] != 0)
                {
                    int x = col + block.getX();
                    int y = row + block.getY();
                    if(y < 0)
                        break;
                    if(background[y][x] != null)
                    {
                        block.undorotate();
                        repaint();
                        return;
                    }

                }
            }
        }
        repaint();
    }
    private boolean checkBottom()
    {
        if(block.getBottomEdge() == gridrows)
        {
            return false;
        }
        int[][]shape = block.getShape();
        int w= block.getWidth();
        int h= block.getHeight();

        for(int col=0;col<w;col++)
        {
            for(int row=h-1;row>=0;row--)
            {
                if(shape[row][col] !=0)
                {
                    int x = col+block.getX();
                    int y = row+block.getY()+1;
                    if(y<0)
                        break;
                    if(background[y][x] !=null)
                        return false;
                    break;
                }
            }
        }

        return true;
    }

    private boolean checkLeft()
    {
        if(block.getLeftEdge() == 0)
            return false;
        int[][]shape = block.getShape();
        int w= block.getWidth();
        int h= block.getHeight();

        for(int row=0;row<h;row++)
        {
            for(int col=0;col<w;col++)
            {
                if(shape[row][col] !=0)
                {
                    int x = col+block.getX()-1;
                    int y = row+block.getY();
                    if(y<0)
                        break;
                    if(background[y][x] !=null)
                        return false;
                    break;
                }
            }
        }
        return true;
    }

    private boolean checkRight()
    {
        if(block.getRightEdge() == gridcolumns)
            return false;
        int[][]shape = block.getShape();
        int w= block.getWidth();
        int h= block.getHeight();

        for(int row=0;row<h;row++)
        {
            for(int col=w-1;col>=0;col--)
            {
                if(shape[row][col] !=0)
                {
                    int x = col+block.getX()+1;
                    int y = row+block.getY();
                    if(y<0)
                        break;
                    if(background[y][x] !=null)
                        return false;
                    break;
                }
            }
        }
        return true;
    }

    public int clearLines()
    {
        boolean lineFilled;
        int linesCleared = 0;
        for(int r = gridrows-1;r>=0;r--)
        {
            lineFilled = true;
            for(int c = 0;c<gridcolumns;c++)
            {
                if(background[r][c] == null)
                {
                    lineFilled=false;
                    break;
                }
            }
            if(lineFilled)
            {
                clearLine(r);
                shiftDown(r);
                clearLine(0);

                r++;

                repaint();
            }
        }
        return linesCleared;
    }

    private void clearLine(int r)
    {
        for(int i=0;i<gridcolumns;i++)
        {
            background[r][i] = null;
        }
    }

    private void shiftDown(int r)
    {
        for(int row = r; row>0;row--)
        {
            for(int col = 0; col<gridcolumns;col++)
            {
                background[row][col] = background[row-1][col];
            }
        }
    }

    public void moveBlocktoBackground()
    {
        int[][] shape = block.getShape();
        int h = block.getHeight();
        int w = block.getWidth();

        int xPos = block.getX();
        int yPos = block.getY();

        Color color = block.getColor();

        for(int r = 0;r<h;r++)
        {
            for(int c=0;c<w;c++)
            {
                if(shape[r][c] == 1)
                {
                    background[r+yPos][c+xPos] = color;
                }
            }
        }
    }
    private void drawBlock(Graphics g)
    {
        int h = block.getHeight();
        int w = block.getWidth();
        Color c = block.getColor();
        int[][] shape = block.getShape();
        for(int row = 0;row<h;row++)
        {
            for(int col = 0; col<w; col++)
            {
                if(shape[row][col] == 1)
                {
                    int x = (block.getX()+col) * gridcellsize;
                    int y = (block.getY()+row) * gridcellsize;

                    drawGridSquare(g, c, x, y);
                }
            }
        }
    }


    private void drawBackground(Graphics g)
    {
        Color color;
        for(int r= 0; r<gridrows;r++)
        {
            for(int c = 0; c<gridcolumns;c++)
            {
                color = background[r][c];

                if(color != null)
                {
                    int x= c*gridcellsize;
                    int y= r*gridcellsize;

                    drawGridSquare(g, color,x,y);
                }
            }
        }
    }

    private void drawGridSquare(Graphics g, Color color, int x, int y)
    {
        g.setColor(color);
        g.fillRect(x, y, gridcellsize, gridcellsize);
        g.setColor(Color.black);
        g.drawRect(x, y, gridcellsize, gridcellsize);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        /*for(int x=0;x<gridrows;x++)
        {
            for(int y = 0; y<gridcolumns; y++)
            {
                g.drawRect(x*gridcellsize, y*gridcellsize, gridcellsize,  gridcellsize);
            }
        }
         */
        drawBackground(g);
        drawBlock(g);
    }
}
