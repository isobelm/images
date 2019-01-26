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
    private static final int BORDER = 30, MIN_DIST = 1, MAX_DIST = 6, DEFAULT_DIST = 2, MIN_STRENGTH = 0, MAX_STRENGTH = 10, DEFAULT_STRENGTH = 0;
    private static final int DEFAULT_THRESHOLD = 30, MIN_THRESHOLD = 5, MAX_THRESHOLD = 105;
    private static JFrame frame;
    private static BufferedImage image;
    private static BufferedImage outImg;
    private static int dist, strength, threshold;


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

        //TODO arrange ui so only relevant options are shown.

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
        JSlider strengthField = new JSlider(JSlider.HORIZONTAL, MIN_STRENGTH, MAX_STRENGTH, DEFAULT_STRENGTH);
        JLabel strengthLabel = new JLabel("Strength: ");
        strengthLabel.setForeground(WHITE);
        strength = DEFAULT_STRENGTH;
        strengthField.addChangeListener(e -> strength = strengthField.getValue());
        strengthField.setSnapToTicks(true);
        panel.add(strengthLabel);
        panel.add(strengthField);

        //threshold
        JSlider thresholdField = new JSlider(JSlider.HORIZONTAL, MIN_THRESHOLD, MAX_THRESHOLD, DEFAULT_THRESHOLD);
        JLabel thresholdLabel = new JLabel("Threshold: ");
        thresholdLabel.setForeground(WHITE);
        threshold = DEFAULT_THRESHOLD;
        thresholdField.addChangeListener(e -> threshold = thresholdField.getValue());
        thresholdField.setSnapToTicks(true);
        panel.add(thresholdLabel);
        panel.add(thresholdField);

        //process
        Button process = new Button("Process Image");
        panel.add(process);
        process.addActionListener(e ->
        {
            if (image != null)
            {
                outImg = ImageProcesses.edgeColours(image, dist, strength);
            }
        });

        //block
        Button block = new Button("Block Image");
        panel.add(block);
        block.addActionListener(e ->
        {
            if (image != null)
            {
                outImg = ImageProcesses.block(image, threshold);
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
        Button saveJpg = new Button("Save as .jpg");
        panel.add(saveJpg);
        saveJpg.addActionListener(e ->
        {
            if (outImg != null)
            {
                saveImage("jpg");
            }
        });

        Button savePng = new Button("Save as .png");
        panel.add(savePng);
        savePng.addActionListener(e ->
        {
            if (outImg != null)
            {
                saveImage("png");
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

    private static void saveImage(String format)
    {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.showSaveDialog(frame);
        File file = fileChooser.getSelectedFile();

        try {
            if (file != null)
            {
                String path = file.getAbsolutePath();
                //Slightly convoluted check for presence of file extension
                if (!(new ImageFilter().accept(file)))
                {
                    path = file.getAbsolutePath() + "." + format;
                }
                File outputFile = new File(path);
                ImageIO.write(outImg, format, outputFile);
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

}

