package urlImage;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.*;
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


    public boolean download(String folderPath) {
        try {
            BufferedImage input = ImageIO.read(new URL(url));
            if (input == null)
                return false;
            String extension = ImageExtension.getExtension(url);
            if (extension == null) {
                ArrayList<String> extensions = ImageExtension.getAllExtensions();
                boolean isDownloaded = false;
                for (int i = 0; i < extensions.size() && !isDownloaded; ++i) {
                    File savedFile = new File(folderPath + "." + extensions.get(i));
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
                File savedFile = new File(folderPath + "." + extension);
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

    public String getName() {
        String name;

        int lastSlash = url.lastIndexOf('/') + 1;
        int lastDot = url.lastIndexOf('.');
        if (lastDot == url.length() - 1)
            name = "unnamed";
        else if (lastDot <= lastSlash)
            name = url.substring(lastSlash);
        else
            name = url.substring(lastSlash, lastDot);
        if (name.length() > 100)
            name = name.substring(100);

        return name.replaceAll("[?<>|\"*:\\\\/\\n]", "!");
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