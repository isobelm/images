import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class DisplayPanel extends JPanel
{
    private BufferedImage image;
    private JFrame frame;

    DisplayPanel(BufferedImage img, JFrame frame)
    {
        image = img;
        this.frame = frame;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null)
        {
//            double imageAspect = (double) image.getHeight() / (double)image.getWidth();
//            double canvasAspect = (double)this.getHeight() / (double)this.getWidth();
//            double widthRatio = image.getWidth() / this.getWidth();
//            double heightRatio = image.getHeight() / this.getHeight();
//            int width, height;
//
//            if (imageAspect < canvasAspect)
//            {
//                width = this.getWidth();
//                height = (int) (image.getHeight() / widthRatio);
//            }
//            else
//            {
//                width = (int) (image.getWidth() / heightRatio);
//                height = this.getHeight();
//            }
            int imgWidth = image.getWidth();
            int imgHeight = image.getHeight();
            int dispWidth = this.getWidth();
            int dispHeight = this.getHeight();
            int imgDispWidth = imgWidth;
            int imgDispHeight = imgHeight;
            if (imgWidth > dispWidth)
            {
                imgDispWidth = dispWidth;
                imgDispHeight = (imgHeight * imgDispWidth) / imgWidth;
            }
            if (imgDispHeight > dispHeight)
            {
                imgDispHeight = dispHeight;
                imgDispWidth = (imgDispHeight * imgWidth) / imgHeight;
            }

            //g.drawImage(image, 0, 0, width, height, this);
            g.drawImage(image, 0, 0, imgDispWidth, imgDispHeight, this);
        }
    }

    public void setImage(BufferedImage img, Graphics g)
    {
        image = img;
        paintComponent(g);
    }
}
