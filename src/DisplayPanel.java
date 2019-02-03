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

            int x = (dispWidth - imgDispWidth) / 2;
            int y = (dispHeight - imgDispHeight) / 2;
            g.drawImage(image, x, y, imgDispWidth, imgDispHeight, this);
        }
    }

    public void setImage(BufferedImage img, Graphics g)
    {
        image = img;
        paintComponent(g);
    }
}
