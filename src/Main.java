import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;


public class Main
{
    private static final Color BACKGROUND = new Color(0x283E32);
    private static final Color WHITE = new Color(0xFFFFFF);
    private static final int RED_MASK = 0xFF0000, GREEN_MASK = 0xFF00, BLUE_MASK = 0xFF;
    private static final int BORDER = 30, MIN_DIST = 1, MAX_DIST = 6, DEFAULT_DIST = 2, MIN_STRENGTH = 0, MAX_STRENGTH = 10, DEFAULT_STREGNTH = 0;
    private static JFrame frame;
    private static BufferedImage image;
    private static BufferedImage outImg;
    private static int dist, strength;


    public static void main(String[] args)
    {
        image = null;
        outImg = null;
        frame = new JFrame("Images");
        initScreen();
    }

    private static void initScreen()
    {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //panel
        JPanel panel = new JPanel();
        formatPanel(panel);

        //choose image
        Button choose = new Button("Choose Image");
        panel.add(choose);
        choose.addActionListener(e -> image = Main.getImage());

        //dist
        JSlider distField = new JSlider(JSlider.HORIZONTAL, MIN_DIST, MAX_DIST, DEFAULT_DIST);
        JLabel distLabel = new JLabel("Edge Size: ");
        distLabel.setForeground(WHITE);
        dist = DEFAULT_DIST;
        distField.addChangeListener(e -> dist = distField.getValue());
        distField.setSnapToTicks(true);
        panel.add(distLabel);
        panel.add(distField);

        //strength
        JSlider strengthField = new JSlider(JSlider.HORIZONTAL, MIN_STRENGTH, MAX_STRENGTH, DEFAULT_STREGNTH);
        JLabel strengthLabel = new JLabel("Strength: ");
        strengthLabel.setForeground(WHITE);
        dist = DEFAULT_DIST;
        strengthField.addChangeListener(e -> strength = strengthField.getValue());
        strengthField.setSnapToTicks(true);
        panel.add(strengthLabel);
        panel.add(strengthField);

        //process
        Button process = new Button("Process Image");
        panel.add(process);
        process.addActionListener(e ->
        {
            if (image != null)
            {
                processImage();
            }
        });

        //invert
        Button invert = new Button("Invert");
        panel.add(invert);
        invert.addActionListener(e ->
        {
            if (outImg != null)
            {
                invert();
            }
        });

        //save
        Button save = new Button("Save Image");
        panel.add(save);
        save.addActionListener(e ->
        {
            if (outImg != null)
            {
                saveImage();
            }
        });


        frame.pack();
        frame.setVisible(true);


    }
    private static BufferedImage getImage()
    {
        boolean imageAccepted;
        BufferedImage image = null;
        do
        {
            imageAccepted = true;
            JFileChooser fileChooser = new JFileChooser();
            FileFilter fileFilter = new ImageFilter();
            fileChooser.setFileFilter(fileFilter);
            fileChooser.setMultiSelectionEnabled(false);
            fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            fileChooser.showOpenDialog(frame);

            File file = fileChooser.getSelectedFile();

            try
            {
                if (file != null)
                {
                    image = ImageIO.read(file);
                }
            }
            catch (java.io.IOException e)
            {
                imageAccepted = false;
            }
        } while (!imageAccepted);

        return image;

    }

    private static void saveImage()
    {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.showSaveDialog(frame);
        File file = fileChooser.getSelectedFile();

        try {
            if (file != null)
            {
                File outputfile = new File(file.getAbsolutePath());
                ImageIO.write(outImg, "jpg", outputfile);
            }
        } catch (IOException e) {
            System.out.println("Fail");

        }
    }

    private static void edgeColours()
    {
        outImg = new BufferedImage(image.getWidth() - dist, image.getHeight() - dist, TYPE_INT_RGB);
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
    }

    private static void invert()
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

    private static void formatPanel(JPanel panel)
    {
        frame.setContentPane(panel);
        panel.setBackground(BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(BORDER, BORDER, BORDER, BORDER));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    }

    private static void processImage()
    {
        edgeColours();
    }

}

