import java.awt.image.BufferedImage;

public class Edge
{
    private Point a, b;
    private float m, c, n, d;
    private double length;

    Edge(int xa, int ya, int xb, int yb)
    {
        a = new Point(xa, ya);
        b = new Point(xb, yb);
        calculateEquations();
    }

    Edge(Point a, Point b)
    {
        this.a = a;
        this.b = b;
        calculateEquations();
    }

    private void calculateEquations()
    {
        float xDif = a.getX() - b.getX();
        float yDif = a.getY() - b.getY();
        m = yDif / xDif;
        n = xDif / yDif;
        float mx = (m * a.getX());
        c = a.getY() - mx;
        float ny = (n * a.getY());
        d = a.getX() - ny;
        length = calculateLength();
    }

    public double calculateLength()
    {
        return a.getDistance(b);
    }

    public void draw(BufferedImage img, int colour)
    {

        //TODO   Use more efficient line algorithm
        int xA = Math.min(a.getX(), b.getX());
        int xB = Math.max(a.getX(), b.getX());
        if (xA > 0)
        {
            for (int i = xA; i < xB && i < img.getWidth(); i++)
            {
                int j = (int) (m * i + c);
                if (j > 0 && j < img.getHeight())
                {
                    img.setRGB(i, j, colour);
                }
            }
        }


        int yA = Math.min(a.getY(), b.getY());
        int yB = Math.max(a.getY(), b.getY());
        if (yA > 0)
        {
            for (int j = yA; j < yB && j < img.getHeight(); j++)
            {
                int i = (int) (n * j + d);
                if (i > 0 && i < img.getWidth())
                {
                    img.setRGB(i, j, colour);
                }
            }
        }
    }

    public boolean equals(Edge e)
    {
        return ((a.equals(e.a) && b.equals(e.b)) || (a.equals(e.b) && b.equals(e.a)));
    }

    public Point getA()
    {
        return a;
    }

    public void setA(Point a)
    {
        this.a = a;
        calculateEquations();
    }

    public Point getB()
    {
        return b;
    }

    public void setB(Point b)
    {
        this.b = b;
        calculateEquations();
    }

    public double getLength()
    {
        return length;
    }

    public void setLength(double length)
    {
        this.length = length;
        calculateEquations();
    }
}
