import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Main
{
    private static final Color BACKGROUND = new Color(0x283E32);
    private static final Color WHITE = new Color(0xFFFFFF);
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
//                processImage();
                outImg = ImageProcesses.edgeColours(image, dist, strength);
            }
        });

        //invert
        Button invert = new Button("Invert");
        panel.add(invert);
        invert.addActionListener(e ->
        {
            if (outImg != null)
            {
                ImageProcesses.invert(outImg);
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
                File outputFile = new File(file.getAbsolutePath());
                ImageIO.write(outImg, "jpg", outputFile);
            }
        } catch (IOException e) {
            System.out.println("Fail");

        }
    }

    private static void formatPanel(JPanel panel)
    {
        frame.setContentPane(panel);
        panel.setBackground(BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(BORDER, BORDER, BORDER, BORDER));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    }

//    private static void processImage()
//    {
//        outImg = ImageProcesses.edgeColours(image, dist, strength);
//    }

}

