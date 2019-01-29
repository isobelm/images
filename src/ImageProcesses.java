import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static java.lang.Math.abs;

public class ImageProcesses
{
    private static final int RED_MASK = 0xFF0000, GREEN_MASK = 0xFF00, BLUE_MASK = 0xFF;
    private static final int RED_SHIFT = 16, GREEN_SHIFT = 8;
    private static final int BLACK = 0, WHITE = 0xFFFFFF, MAX_POINTS_IN_EDGE = 100, MAX_POINTS_IN_POINT = 10, MIN_POINTS_IN_POINT = 3;
    private static final int INIT_MAX_SQUARE = 200;

    public static BufferedImage edgeColours(BufferedImage image, int dist, int strength)
    {
        BufferedImage outImg = new BufferedImage(image.getWidth() - dist, image.getHeight() - dist, TYPE_INT_RGB);
        float strengthMul = 1 + (((float)strength)/10);

        for (int j = 0; j < outImg.getHeight(); j++)
        {
            for (int i = 0; i < outImg.getWidth(); i++)
            {
                int current = image.getRGB(i, j);
                int compH = image.getRGB(i + dist, j);
                int compV = image.getRGB(i, j + dist);
                int r = current & RED_MASK;
                int rh = compH & RED_MASK;
                int rv = compV & RED_MASK;
                int g = current & GREEN_MASK;
                int gh = compH & GREEN_MASK;
                int gv = compV & GREEN_MASK;
                int b = current & BLUE_MASK;
                int bh = compH & BLUE_MASK;
                int bv = compV & BLUE_MASK;

                int difR = Math.max(abs(r - rh), abs(r - rv));
                int difG = Math.max(abs(g - gh), abs(g - gv));
                int difB = Math.max(abs(b - bh), abs(b - bv));

                if (strength != 0)
                {
                    difR >>= RED_SHIFT;
                    float tmp = difR * strengthMul;
                    difR = (int) tmp;
                    difR <<= RED_SHIFT;
                    if (difR > RED_MASK) difR = RED_MASK;
                    difG >>= GREEN_SHIFT;
                    tmp = difG * strengthMul;
                    difG = (int) tmp;
                    difG <<= GREEN_SHIFT;
                    if (difG > GREEN_MASK) difG = GREEN_MASK;
                    tmp = difB * strengthMul;
                    difB = (int) tmp;
                    if (difB > BLUE_MASK) difB = BLUE_MASK;
                }

                int colour = difB + difG + difR;

                outImg.setRGB(i, j, colour);

            }
        }

        return outImg;
    }



    public static void invert(BufferedImage outImg)
    {
        for (int j = 0; j < outImg.getHeight(); j++)
        {
            for (int i = 0; i < outImg.getWidth(); i++)
            {
                int colour = outImg.getRGB(i, j);
                colour = ~colour;
                colour &= 0xFFFFFF;
                outImg.setRGB(i, j, colour);
            }
        }
    }

    public static BufferedImage block(BufferedImage img, int threshold)
    {
        BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(), TYPE_INT_RGB);

        int minXBlocks = img.getWidth() / INIT_MAX_SQUARE + 1;
        int minYBlocks = img.getHeight() / INIT_MAX_SQUARE + 1;

        for (int i = 0; i < minXBlocks; i++)
        {
            for (int j = 0; j < minYBlocks; j++)
            {
                halve(img, out,
                        i * INIT_MAX_SQUARE, (i * INIT_MAX_SQUARE + INIT_MAX_SQUARE > out.getWidth()) ? out.getWidth() : i * INIT_MAX_SQUARE + INIT_MAX_SQUARE,
                        j * INIT_MAX_SQUARE, (j * INIT_MAX_SQUARE + INIT_MAX_SQUARE > out.getHeight()) ? out.getHeight() : j * INIT_MAX_SQUARE + INIT_MAX_SQUARE,
                threshold);
            }
        }

        return out;
    }



    private static void halve(BufferedImage img, BufferedImage out, int xMin, int xMax, int yMin, int yMax, int threshold)
    {
        int xMid = (xMax + xMin) / 2;
        int yMid = (yMax + yMin) / 2;

        //get average colour in top, bottom, left and right of block
        long[] aveTop = aveOfRect(img, xMin, xMax, yMin, yMid);
        long[] aveBottom = aveOfRect(img, xMin, xMax, yMid, yMax);
        long[] aveRight = aveOfRect(img, xMin, xMid, yMin, yMax);
        long[] aveLeft = aveOfRect(img, xMid, xMax, yMin, yMax);

        //find greatest difference between colour (r, g or b) across both horizontal and vertical axes.
        int hDif = (int) Math.max(Math.max(abs(aveRight[0] - aveLeft[0]), abs(aveRight[1] - aveLeft[1])), abs(aveRight[2] - aveLeft[2]));
        if (xMax - xMin <= 1) hDif = 0;
        int vDif = (int) Math.max(Math.max(abs(aveTop[0] - aveBottom[0]), abs(aveTop[1] - aveBottom[1])), abs(aveTop[2] - aveBottom[2]));
        if (yMax - yMin <= 1) vDif = 0;

        //If difference is above the threshold, halve image along the axis with the greater difference.
        if (hDif >= threshold || vDif >= threshold)
        {
            if (hDif > vDif)
            {
                halve(img, out, xMin, xMid, yMin, yMax, threshold);
                halve(img, out, xMid, xMax, yMin, yMax, threshold);
            }
            else
            {
                halve(img, out, xMin, xMax, yMin, yMid, threshold);
                halve(img, out, xMin, xMax, yMid, yMax, threshold);
            }
        }
        //If difference is below threshold, colour block in overall average.
        else
        {
            int[] ave = new int[3];
            if (xMax - xMin > 1)
            {
                ave[0] = (int)(aveRight[0] + aveLeft[0]) / 2;
                ave[1] = (int)(aveRight[1] + aveLeft[1]) / 2;
                ave[2] = (int)(aveRight[2] + aveLeft[2]) / 2;
            }
            else
            {
                ave[0] = (int)aveLeft[0];
                ave[1] = (int)aveLeft[1];
                ave[2] = (int)aveLeft[2];
            }

            int colour = valsToInt(ave[0], ave[1], ave[2]);
            for(int i = xMin; i < xMax; i++)
            {
                for (int j = yMin; j< yMax; j++)
                {
                    out.setRGB(i, j, colour);
                }
            }

        }

    }



    private static long[] aveOfRect(BufferedImage img, int xMin, int xMax, int yMin, int yMax)
    {
        int count = 0;
        int xIncrement = (xMax - xMin) / 10;
        int yIncrement = (yMax - yMin) / 10;

        if (xIncrement < 1) xIncrement = 1;
        if (yIncrement < 1) yIncrement = 1;
        long[] ave = {0,0,0};

        for (int j = yMin; j < yMax; j += yIncrement)
        {
            for (int i = xMin; i < xMax; i+= xIncrement)
            {
                int colour = img.getRGB(i, j);
                int r = getRedValue(colour);
                int g = getGreenValue(colour);
                int b = getBlueValue(colour);
                ave[0] = (ave[0] * count + r) / (count + 1);
                ave[1] = (ave[1] * count + g) / (count + 1);
                ave[2] = (ave[2] * count + b) / (count + 1);
                ++count;
            }
        }

        return ave;
    }



    public static BufferedImage threshold(BufferedImage img, int threshold)
    {
        BufferedImage outImg = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        for (int j = 0; j < outImg.getHeight(); j++)
        {
            for (int i = 0; i < outImg.getWidth(); i++)
            {
                if (getRedValue(img.getRGB(i, j)) > getRedValue(threshold) && getBlueValue(img.getRGB(i, j)) > getBlueValue(threshold)
                        && getGreenValue(img.getRGB(i, j)) > getGreenValue(threshold))
                {
                    outImg.setRGB(i, j, WHITE);
                }
                else
                {
                    outImg.setRGB(i, j, BLACK);
                }
            }
        }

        return outImg;
    }



    public static BufferedImage imgToPointsAndEdges(BufferedImage img)
    {
        BufferedImage outImg = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        img.copyData(outImg.getRaster());
        ArrayList<Point> points = new ArrayList<>();
        ArrayList<Edge> edges = new ArrayList<>();

        for(int j = 0; j < outImg.getHeight(); j++)
        {
            for (int i = 0; i < outImg.getWidth(); i++)
            {
                if((outImg.getRGB(i, j) & WHITE) == BLACK)
                {
                    addEdge(outImg, i, j, points, edges);
                }
            }
        }

        for (Edge e:
             edges)
        {
            e.draw(outImg, BLACK);
        }
        for (Point p:
             points)
        {
            outImg.setRGB(p.getX(), p.getY(), BLACK);
        }

        return outImg;
    }



    private static void addEdge(BufferedImage img, int x, int y, ArrayList<Point> points, ArrayList<Edge> edges)
    {
        Point origin = new Point(x, y);
        Point furthestPoint = new Point(x, y);

        int noOfPoints = getPoints(img, x, y, origin, furthestPoint);

        if (noOfPoints > MAX_POINTS_IN_POINT)
        {
            Edge e = new Edge(origin, furthestPoint);
            points.add(origin);
            points.add(furthestPoint);
            edges.add(e);
        }
        else if (noOfPoints > MIN_POINTS_IN_POINT)
        {
            int xAve = (origin.getX() - furthestPoint.getX()) / 2 + furthestPoint.getX();
            int yAve = (origin.getY() - furthestPoint.getY()) / 2 + furthestPoint.getY();

            Point p = new Point(xAve, yAve);
            points.add(p);
        }


    }



    private static int getPoints(BufferedImage img, int x, int y, Point origin, Point furthestPoint)
    {
        img.setRGB(x, y, WHITE);
        int pointsInContact = 0;
        pointsInContact++;
        for (int j = -1; j <= 1; j++)
        {
            for (int i = -1; i <= 1; i++)
            {
                if ((x + i) > 0 && (x + i) < img.getWidth() && (y + j) > 0 && (y + j) < img.getHeight())
                {
                    if ((img.getRGB(x + i, y + j) & WHITE) == BLACK)
                    {
                        pointsInContact += getPoints(img, x + i, y + j, origin, furthestPoint);
                    }
                }
            }
        }

        Point p = new Point(x, y);
        if (origin.getDistance(furthestPoint) < origin.getDistance(p))
        {
            furthestPoint.setX(p.getX());
            furthestPoint.setY(p.getY());
        }

        return pointsInContact;
    }



    private static int getRedValue(int colour)
    {
        int red = colour & RED_MASK;
        red = red >> RED_SHIFT;
        return red;
    }



    private static int getGreenValue(int colour)
    {
        int green = colour & GREEN_MASK;
        green = green >> GREEN_SHIFT;
        return green;
    }



    private static int getBlueValue(int colour)
    {
        int blue = colour & BLUE_MASK;
        return blue;
    }



    private static int valsToInt(int r, int g, int b)
    {
        int result = 0;
        r <<= RED_SHIFT;
        g <<= GREEN_SHIFT;
        result += r;
        result += g;
        result += b;
        return result;
    }
}
