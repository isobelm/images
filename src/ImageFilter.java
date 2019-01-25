import java.io.File;

public class ImageFilter extends javax.swing.filechooser.FileFilter
{
    @Override
    public String getDescription()
    {
        return "Accepts only .jpg files.";
    }

    @Override
    public boolean accept(File f)
    {
        boolean result = false;
        if (f.getName().toLowerCase().endsWith(".jpg") || f.getName().toLowerCase().endsWith(".jpeg") || f.getName().toLowerCase().endsWith(".png"))
        {
            result = true;
        }
        return result;
    }
}
