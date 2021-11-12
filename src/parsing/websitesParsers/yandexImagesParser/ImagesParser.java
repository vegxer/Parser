package parsing.websitesParsers.yandexImagesParser;

import urlImage.ImageExtension;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.nodes.Element;
import parsing.Parser;
import urlImage.Image;

import java.util.ArrayList;


public class ImagesParser implements Parser<ArrayList<Image>> {
    @Override
    public ArrayList<Image> parse(Element document) throws ParseException {
        String json = document.attributes().get("data-bem");
        JSONObject obj = (JSONObject)((JSONObject)new JSONParser().parse(json)).get("serp-item");
        JSONArray dups = (JSONArray)obj.get("dups");
        JSONArray preview = (JSONArray)obj.get("preview");

        ArrayList<Image> images = new ArrayList<>();
        ArrayList<Image> imagesNoExt = new ArrayList<>();
        int size = Math.max(dups.size(), preview.size());
        for (int i = 0; i < size; ++i) {
            if (i < dups.size()) {
                Image image = getImage((JSONObject) dups.get(i));
                if (ImageExtension.getImageExtension(image.getUrl()) == null)
                    imagesNoExt.add(image);
                else
                    images.add(image);
            }
            if (i < preview.size()) {
                Image image = getImage((JSONObject) preview.get(i));
                if (ImageExtension.getImageExtension(image.getUrl()) == null)
                    imagesNoExt.add(image);
                else
                    images.add(image);
            }
        }

        images.addAll(imagesNoExt);
        return images;
    }

    private Image getImage(JSONObject currElem) {
        Object objOrigin = currElem.get("origin");
        if (objOrigin == null)
            return new Image((String)currElem.get("url"));
        else
            return new Image((String)((JSONObject)objOrigin).get("url"));
    }

}
