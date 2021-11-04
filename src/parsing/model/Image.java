package parsing.model;

import Files.Extension;
import Threads.LoadingThread;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Image {
    private String url;


    public Image() {}

    public Image(String url) {
        this.url = url;
    }

    public boolean save(String path) {
        try {
            BufferedImage input = ImageIO.read(new URL(url));
            String extension = Extension.getExtension(url);
            File output = new File(path + "." + extension);
            try {
                ImageIO.write(input, extension, output);
                return true;
            } catch (Exception exc) {
                output = new File(path + ".jpg");
                ImageIO.write(input, "jpg", output);
                return true;
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