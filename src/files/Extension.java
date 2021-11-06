package files;

import java.util.ArrayList;
import java.util.List;

public final class Extension {
    private Extension() {}

    public static String getImageExtension(String filePath)
    {
        ArrayList<String> formats = new ArrayList<>(List.of(".jpg", ".jpeg", ".png", ".webp", ".tiff", ".bmp", ".gif",
                ".ico", ".ecw", ".ilbm", ".tif", ".pcx"));

        for (String format : formats) {
            if (filePath.endsWith(format))
                return format.substring(1);
        }

        return null;
    }
}
