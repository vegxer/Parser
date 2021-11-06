package parsing.model;

import files.Extension;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
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
            if (input == null)
                return false;
            String extension = Extension.getImageExtension(url);
            if (extension == null)
                extension = "jpg";
            FileImageOutputStream output = new FileImageOutputStream(new File(path + "." + extension));
            try {
                if (!ImageIO.write(input, extension, output))
                    throw new Exception();
                return true;
            } catch (Exception exc) {
                output = new FileImageOutputStream(new File(path + ".jpg"));
                return ImageIO.write(input, "jpg", output);
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