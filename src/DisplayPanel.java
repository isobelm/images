import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class DisplayPanel extends JPanel
{
    private BufferedImage image;
    private BufferedImage selectedPortion;
    private int selectionX, selectionY, selectionWidth, selectionHeight;
    private int imageX, imageY, imageWidth, imageHeight;
    private double scale;
    private MouseListener mouseListener;
    private JFrame frame;

    DisplayPanel(BufferedImage img, JFrame frame)
    {
        image = img;
        selectedPortion = null;
        initMouseListener();
        addMouseListener(mouseListener);
        this.frame = frame;
        setBackground(new Color(0xCDCDCD));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null)
        {
            imageWidth = image.getWidth();
            imageHeight = image.getHeight();
            int dispWidth = this.getWidth();
            int dispHeight = this.getHeight();
            int imgDispWidth = imageWidth;
            int imgDispHeight = imageHeight;
            if (imageWidth > dispWidth)
            {
                imgDispWidth = dispWidth;
                imgDispHeight = (imageHeight * imgDispWidth) / imageWidth;
            }
            if (imgDispHeight > dispHeight)
            {
                imgDispHeight = dispHeight;
                imgDispWidth = (imgDispHeight * imageWidth) / imageHeight;
            }
            scale = (double) imgDispWidth / (double) imageWidth;

            imageX = (dispWidth - imgDispWidth) / 2;
            imageY = (dispHeight - imgDispHeight) / 2;
            g.drawImage(image, imageX, imageY + ((frame.hasFocus())? frame.getInsets().top : 0), imgDispWidth, imgDispHeight, frame);

            if (selectedPortion != null)
            {
                g.setColor(new Color(0xFFFFFF));
                g.drawRect((int) (((double)selectionX * scale)  + imageX), (int) (((double)selectionY * scale) + imageY + ((frame.hasFocus())? frame.getInsets().top : 0)),
                        (int) (selectionWidth * scale), (int) (selectionHeight * scale));

            }
        }
    }

    private void initMouseListener()
    {
        mouseListener = new MouseListener()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (image != null)
                {
                    selectedPortion = null;
                    paintComponent(frame.getGraphics());
                }
            }

            @Override
            public void mousePressed(MouseEvent e)
            {
                if (image != null)
                {
                    selectionX = (int) ((e.getX() - imageX) / scale);
                    selectionY = (int) ((e.getY() - imageY) / scale);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e)
            {
                if (image != null)
                {
                    int selectRightX = (int) ((double)(e.getX() - imageX) / scale);
                    int selectTopY = (int) ((double)(e.getY() - imageY) / scale);

                    if (selectRightX < selectionX)
                    {
                        int tmp = selectionX;
                        selectionX = selectRightX;
                        selectRightX = tmp;
                    }

                    if (selectionX < 0) selectionX = 0;
                    if (selectRightX > imageWidth) selectRightX = imageWidth;

                    if (selectTopY < selectionY)
                    {
                        int tmp = selectionY;
                        selectionY = selectTopY;
                        selectTopY = tmp;
                    }

                    if (selectionY < 0) selectionY = 0;
                    if (selectTopY > imageHeight) selectTopY = imageHeight;

                    selectionWidth = selectRightX - selectionX;
                    selectionHeight = selectTopY - selectionY;

                    if (selectionHeight > 0 && selectionWidth > 0)
                    {
                        selectedPortion = image.getSubimage(selectionX, selectionY, selectionWidth, selectionHeight);

                        paintComponent(frame.getGraphics());
                    }
                    else
                    {
                        selectedPortion = null;
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {

            }

            @Override
            public void mouseExited(MouseEvent e)
            {

            }
        };

    }

    public BufferedImage getSelectedPortion()
    {
        return selectedPortion;
    }

    public BufferedImage getSelectedPortionOf(BufferedImage img)
    {
        return img.getSubimage(selectionX, selectionY, selectionWidth, selectionHeight);
    }

    public void setImage(BufferedImage img, Graphics g)
    {
        image = img;
        paintComponent(g);
    }

    public int getSelectionX()
    {
        return selectionX;
    }

    public int getSelectionY()
    {
        return selectionY;
    }

}
