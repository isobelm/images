
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

    public boolean intersects(Edge e)
    {
        //check if one or more of the lines are vertical
        if (a.getX() == b.getX())
        {
//            System.out.println("vert");
            return this.verticalIntersection(e);
        }
        else if (e.b.getX() == e.a.getX())
        {
//            System.out.println("vert_oth");
            return e.verticalIntersection(this);
        }

        //check if one or more of the lines are horizontal
        if (a.getY() == b.getY())
        {
//            System.out.println("hor");
            return this.horizontalIntersection(e);
        }
        else if (e.b.getY() == e.a.getY())
        {
//            System.out.println("hor_oth");
            return e.horizontalIntersection(this);
        }

        //get x position of intersection
        float xf = ((e.getC() - c) / (m - e.getM()));
        int x =(int) xf;
        //get y position of intersection
        float y = m * x + c;
        //int y = (int) fy;

        //return whether (x,y) is within the range of both lines
        return (x > Math.min(a.getX(), b.getX()) && x < Math.max(a.getX(), b.getX())
                && y > Math.min(a.getY(), b.getY()) && y < Math.max(a.getY(), b.getY())
                && (x > Math.min(e.getA().getX(), e.getB().getX())) && x < Math.max(e.getA().getX(), e.getB().getX())
                && y > Math.min(e.getA().getY(), e.getB().getY()) && y < Math.max(e.getA().getY(), e.getB().getY()));
    }

    private boolean verticalIntersection(Edge e)
    {
        Edge edgeA = new Edge(a.getY(), a.getX(), b.getY(), b.getX());
        Edge edgeB = new Edge(e.getA().getY(), e.getA().getX(), e.getB().getY(), e.getB().getX());
        return edgeA.horizontalIntersection(edgeB);

//        if (e.b.getX() == e.a.getX())   //if both lines are vertical
//        {
//            if (a.getX() == e.getA().getX())  //if both lines are on the same line
//            {                                   //return if lines overlap
//                return (Math.max(a.getY(), b.getY()) > Math.min(e.getA().getY(), e.getB().getY())
//                        && (Math.min(a.getY(), b.getY()) < Math.max(e.getA().getY(), e.getB().getY())));
//            } else return false;
//        } else if (e.getA().getY() == e.getB().getY())
//        {
//            return (Math.max(a.getY(), b.getY()) > e.getA().getY() && Math.min(a.getY(), b.getY()) < e.getA().getY()
//                    && Math.max(e.getA().getX(), e.getB().getX()) > a.getX() && Math.min(e.getA().getX(), e.getB().getX()) < a.getX());
//        } else
//        {
////            System.out.println("m: " + e.getM() + "\nc: " + e.getC());
//            int x = a.getX();
//            double y = x * e.getM() + e.getC();
////            System.out.println("x: " + x + "\ny: " + y);
//
//            return (y > Math.min(a.getY(), b.getY()) && y < Math.max(a.getY(), b.getY())
//                    && (y > Math.min(e.getA().getY(), e.getB().getY())) && y < Math.max(e.getA().getY(), e.getB().getY())
//                    && x > Math.min(e.getA().getX(), e.getB().getX()) && x < Math.max(e.getA().getX(), e.getB().getX()));
//        }

    }

    private boolean horizontalIntersection(Edge e)
    {
        if (e.b.getY() == e.a.getY())   //if both lines are horizontal
        {
            if (a.getY() == e.getA().getY())  //if both lines are on the same line
            {                                   //return if lines overlap
                return (Math.max(a.getX(), b.getX()) > Math.min(e.getA().getX(), e.getB().getX())
                        && (Math.min(a.getX(), b.getX()) < Math.max(e.getA().getX(), e.getB().getX())));
            } else return false;
        } else if (e.getA().getX() == e.getB().getX())
        {
            return (Math.max(a.getX(), b.getX()) > e.getA().getX() && Math.min(a.getX(), b.getX()) < e.getA().getX()
            && Math.max(e.getA().getY(), e.getB().getY()) > a.getY() && Math.min(e.getA().getY(), e.getB().getY()) < a.getY());
        }
        else
        {
//            System.out.println("m: " + e.getM() + "\nc: " + e.getC());
            double y = a.getY();
            double x = ((y - e.getC()) / e.getM());
            double thisMinX =  Math.min(a.getX(), b.getX());
            double thisMaxX =  Math.max(a.getX(), b.getX());
            double eMinX =  Math.min(e.getA().getX(), e.getB().getX());
            double eMaxX =  Math.max(e.getA().getX(), e.getB().getX());
            double eMinY =  Math.min(e.getA().getY(), e.getB().getY());
            double eMaxY =  Math.max(e.getA().getY(), e.getB().getY());
//            System.out.println("x: " + x + "\ny: " + y);

            return (x > thisMinX && x < thisMaxX
                    && x > eMinX && x < eMaxX
                    && y > eMinY && y < eMaxY);
        }
    }

    public boolean equals(Edge e)
    {
        return ((a.equals(e.a) && b.equals(e.b)) || (a.equals(e.b) && b.equals(e.a)));
    }

    private Point getA()
    {
        return a;
    }

    private Point getB()
    {
        return b;
    }

    public double getLength()
    {
        return length;
    }

    private float getM()
    {
        return m;
    }

    private float getC()
    {
        return c;
    }

    private float getN()
    {
        return n;
    }

    private float getD()
    {
        return d;
    }
}
