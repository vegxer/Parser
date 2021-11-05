package parsing.parsers.imagesSearcher;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.nodes.Element;
import parsing.Parser;
import parsing.model.Image;

import java.util.ArrayList;


public class ImagesParser implements Parser<ArrayList<Image>> {
    @Override
    public ArrayList<Image> parse(Element document) throws ParseException {
        String json = document.attributes().get("data-bem");
        JSONObject obj = (JSONObject)((JSONObject)new JSONParser().parse(json)).get("serp-item");
        JSONArray dups = (JSONArray)obj.get("dups");
        JSONArray preview = (JSONArray)obj.get("preview");

        ArrayList<Image> images = new ArrayList<>();
        int size = Math.max(dups.size(), preview.size());
        for (int i = 0; i < size; ++i) {
            if (i < dups.size())
                images.add(getImage((JSONObject)dups.get(i)));
            if (i < preview.size())
                images.add(getImage((JSONObject)preview.get(i)));
        }

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
