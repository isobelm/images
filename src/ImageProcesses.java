import java.awt.image.BufferedImage;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

public class ImageProcesses
{
    private static final int RED_MASK = 0xFF0000, GREEN_MASK = 0xFF00, BLUE_MASK = 0xFF;

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

                int difR = Math.max(Math.abs(r - rh), Math.abs(r - rv));
                int difG = Math.max(Math.abs(g - gh), Math.abs(g - gv));
                int difB = Math.max(Math.abs(b - bh), Math.abs(b - bv));

                if (i == 0 && j == 0)
                {
                    System.out.println("R: " + r + "\tG: " + g + "\tB: " + b + "\ndR: " + difR + "\tdG: " + difG + "\tdB: " + difB);
                }

                if (strength != 0)
                {
                    difR >>= 16;
                    float tmp = difR * strengthMul;
                    difR = (int) tmp;
                    difR <<= 16;
                    if (difR > RED_MASK) difR = RED_MASK;
                    difG >>= 8;
                    tmp = difG * strengthMul;
                    difG = (int) tmp;
                    difG <<= 8;
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
}
