public class Point
{
    private int x, y, priority;

    Point(int x, int y, int priority)
    {
        this.x = x;
        this.y = y;
        this.priority = priority;
    }

    public boolean equals(Point p)
    {
        return (this.x == p.getX() && this.y == p.getY());
    }

    public double getDistance(Point p)
    {
        double xDif = x - p.x;
        double yDif = y - p.y;

        return Math.sqrt(xDif*xDif + yDif*yDif);
    }

    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getY()
    {
        return y;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public int getPriority()
    {
        return priority;
    }

    public void setPriority(int priority)
    {
        this.priority = priority;
    }
}
