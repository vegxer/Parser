package urlImage;

import java.util.ArrayList;
import java.util.List;

public final class ImageExtension {
    private ImageExtension() {}

    public static String getExtension(String filePath)
    {
        ArrayList<String> formats = getAllExtensions();

        for (String format : formats) {
            if (filePath.endsWith("." + format))
                return format;
        }

        return null;
    }

    public static ArrayList<String> getAllExtensions() {
        return new ArrayList<>(List.of("jpg", "png", "jpeg", "bmp", "gif", "webp", "tiff",
                "ico", "ecw", "ilbm", "tif", "pcx"));
    }
}
