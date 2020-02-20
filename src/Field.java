import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class Field
{
    private int x, y;
    private int size;

    private boolean isStartingPoint;
    private boolean isDestination;
    private boolean isObstacle;

    private int gCost = 0;
    private int hCost = 0;
    private int fCost = 0;

    private boolean isPath;
    private boolean isClosedList;
    private boolean isOpenList;

    private Field parent;

    public Field(int x, int y, int size, boolean isObstacle)
    {
        this.x = x;
        this.y = y;
        this.size = size;
        this.isObstacle = isObstacle;
    }

    public Field(int x, int y, int size)
    {
        this.x = x;
        this.y = y;
        this.size = size;
    }

    public void drawField(Graphics2D g)
    {
        g.setColor(Color.black);

        if (isObstacle)
        {
            g.setColor(Color.black);
            g.fillRect(x, y, size, size);
        }
        else
        if (isStartingPoint)
        {
            g.setColor(Color.green);
            g.fillRect(x, y, size, size);
            g.setColor(Color.black);
        }
        else
        if (isDestination)
        {
            g.setColor(Color.red);
            g.fillRect(x, y, size, size);
            g.setColor(Color.black);
        }
        else
        if (isPath)
        {
            g.setColor(Color.blue);
            g.fillRect(x, y, size, size);
            g.setColor(Color.black);
        }
        else
        if (isOpenList)
        {
            g.setColor(Color.yellow);
            g.fillRect(x, y, size, size);
            g.setColor(Color.black);
        }
        else
        if (isClosedList)
        {
            g.setColor(Color.gray);
            g.fillRect(x, y, size, size);
            g.setColor(Color.black);
        }
        g.drawRect(x, y, size, size);

        if (size == 100 && (isClosedList || isOpenList))
        {
            drawFCost(g);
            drawGCost(g);
            drawHCost(g);
        }
    }

    private void drawHCost(Graphics2D g)
    {
        g.drawString(Integer.toString(hCost),
                (int) (x + (size - size * 0.25)),
                (int) (y + (size - size * 0.1)));
    }

    private void drawGCost(Graphics2D g)
    {
        g.drawString(Integer.toString(gCost),
                (int) (x + (size - size * 0.9)),
                (float) (y + (size - size * 0.1)));
    }

    private void drawFCost(Graphics2D g)
    {
        g.drawString(Integer.toString(fCost),
                (int) (x + (size - size * 0.9)),
                (int) (y + (size - size * 0.75)));
    }

    public ArrayList<Field> getNeighbors()
    {
        ArrayList<Field> neighbors = new ArrayList<>();
        Field neighbor;
        neighbor = new Field(x, y - size, size);
        neighbors.add(neighbor);
        neighbor = new Field(x + size, y - size, size);
        neighbors.add(neighbor);
        neighbor = new Field(x + size, y, size);
        neighbors.add(neighbor);
        neighbor = new Field(x + size, y + size, size);
        neighbors.add(neighbor);
        neighbor = new Field(x, y + size, size);
        neighbors.add(neighbor);
        neighbor = new Field(x - size, y + size, size);
        neighbors.add(neighbor);
        neighbor = new Field(x - size, y, size);
        neighbors.add(neighbor);
        neighbor = new Field(x - size, y - size, size);
        neighbors.add(neighbor);

        return neighbors;
    }

    public Field getParent()
    {
        return parent;
    }

    public void setParent(Field parent)
    {
        this.parent = parent;
    }

    public boolean isPath()
    {
        return isPath;
    }

    public void setPath(boolean path)
    {
        this.isPath = path;
    }

    public boolean istClosedList()
    {
        return isClosedList;
    }

    public void setClosedList(boolean closedList)
    {
        this.isClosedList = closedList;
    }

    public boolean istOpenList()
    {
        return isOpenList;
    }

    public void setOpenList(boolean openList)
    {
        this.isOpenList = openList;
    }

    public boolean isStartingPoint()
    {
        return isStartingPoint;
    }

    public void setStartingPoint(boolean startingPoint)
    {
        this.isStartingPoint = startingPoint;
    }

    public boolean isDestination()
    {
        return isDestination;
    }

    public void setDestination(boolean destination)
    {
        this.isDestination = destination;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isObstacle()
    {
        return isObstacle;
    }

    public void setObstacle(boolean obstacle)
    {
        this.isObstacle = obstacle;
    }

    public int getSize()
    {
        return size;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getGCost()
    {
        return gCost;
    }

    public void setGCost(int gCost)
    {
        this.gCost = gCost;
    }

    public int getHCost()
    {
        return hCost;
    }

    public void setHCost(int hCost)
    {
        this.hCost = hCost;
    }

    public int getFCost()
    {
        return fCost;
    }

    public void setFCost(int fCost)
    {
        this.fCost = fCost;
    }
}
