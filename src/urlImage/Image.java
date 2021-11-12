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


    public String download(String folderPath) {
        try {
            BufferedImage input = ImageIO.read(new URL(url));
            if (input == null)
                return null;
            String extension = ImageExtension.getExtension(url);
            if (extension == null) {
                ArrayList<String> extensions = ImageExtension.getAllExtensions();
                boolean isDownloaded = false;
                File savedFile = null;
                for (int i = 0; i < extensions.size() && !isDownloaded; ++i) {
                    savedFile = new File(folderPath + "." + extensions.get(i));
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
                return isDownloaded ? savedFile.getAbsolutePath() : null;
            }
            else {
                File savedFile = new File(folderPath + "." + extension);
                FileImageOutputStream output = new FileImageOutputStream(savedFile);
                boolean isDownloaded = ImageIO.write(input, extension, output);
                if (!isDownloaded)
                    savedFile.delete();
                output.close();
                return isDownloaded ? savedFile.getAbsolutePath() : null;
            }
        } catch (Exception exc) {
            return null;
        }
    }

    public static BufferedImage resize(BufferedImage image, int targetWidth, int targetHeight) {
        if (targetHeight <= 0 || targetWidth <= 0)
            throw new IllegalArgumentException("Неверный ввод размеров изображения");

        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(image, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return resizedImage;
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