import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import javax.imageio.ImageIO;


public class Main
{
    public static final Color BACKGROUND = new Color(0x1A2820);

    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Images");
        initScreen(frame);

        BufferedImage input = getImage(frame);



    }

    public static void initScreen(JFrame frame)
    {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setVisible(true);
        frame.getContentPane().setBackground(BACKGROUND);
        JPanel panel = new JPanel();
        frame.add(panel);
        panel.setBackground(BACKGROUND);
        panel.setLayout(new FlowLayout());
    }

    public static BufferedImage getImage(JFrame frame)
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
                image = ImageIO.read(file);
            }
            catch (java.io.IOException e)
            {
                imageAccepted = false;
            }
        } while (!imageAccepted);

        return image;

    }

    public static void saveImage(JFrame frame, RenderedImage image)
    {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.showSaveDialog(frame);
        File file = fileChooser.getSelectedFile();
        String path = file.getPath();


        ImageIO.write(image, ".jpg", path);
    }


}
