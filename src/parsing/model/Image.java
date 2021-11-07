package parsing.model;

import files.Extension;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;

public class Image {
    private String url;


    public Image() {}

    public Image(String url) {
        this.url = url;
    }

    public boolean save(String path) {
        try {
            BufferedImage input = ImageIO.read(new URL(url));
            if (input == null)
                return false;
            String extension = Extension.getImageExtension(url);
            if (extension == null) {
                ArrayList<String> extensions = Extension.getImageExtensions();
                boolean isDownloaded = false;
                for (int i = 0; i < extensions.size() && !isDownloaded; ++i) {
                    File savedFile = new File(path + "." + extensions.get(i));
                    FileImageOutputStream output = new FileImageOutputStream(savedFile);
                    try {
                        isDownloaded = ImageIO.write(input, extensions.get(i), output);
                    } catch (Exception exc) {
                        isDownloaded = false;
                    }
                    output.close();
                    if (!isDownloaded)
                        savedFile.delete();
                }
                return isDownloaded;
            }
            else {
                File savedFile = new File(path + "." + extension);
                FileImageOutputStream output = new FileImageOutputStream(savedFile);
                boolean isDownloaded = ImageIO.write(input, extension, output);
                if (!isDownloaded)
                    savedFile.delete();
                output.close();
                return isDownloaded;
            }
        } catch (Exception exc) {
            return false;
        }
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        if (url == null)
            throw new NullPointerException();
        this.url = url;
    }
}