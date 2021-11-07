package files;

import java.util.ArrayList;
import java.util.List;

public final class Extension {
    private Extension() {}

    public static String getImageExtension(String filePath)
    {
        ArrayList<String> formats = getImageExtensions();

        for (String format : formats) {
            if (filePath.endsWith("." + format))
                return format;
        }

        return null;
    }

    public static ArrayList<String> getImageExtensions() {
        return new ArrayList<>(List.of("jpg", "png", "jpeg", "bmp", "webp", "gif", "tiff",
                "ico", "ecw", "ilbm", "tif", "pcx"));
    }
}
