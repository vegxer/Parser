package parsing.websitesParsers.theGuardianParser;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import parsing.Parser;
import urlImage.Image;
import parsing.model.News;
import time.Date;

public class GuardianParser implements Parser<News> {
    @Override
    public News parse(Element document) {
        String text = document.ownText();
        if (text.contains("!!!Встретилась необрабатываемая"))
            return new News(text);
        return new News(text, getText(document), getDate(document), getImage(document));
    }

    private String getText(Element elem) {
        Elements textElements = elem.getElementsByClass("article-body-commercial-selector article-body-vie" +
                "wer-selector  dcr-ucgxn1").get(0).getElementsByTag("p");

        String text = "";
        for (Element textElement : textElements) {
            String tagText = textElement.text().replace("\n", "");
            if (!tagText.isEmpty())
                text = text.concat(tagText + "\n\n");
        }

        return text;
    }

    private Date getDate(Element elem) {
        String dateText = elem.getElementsByClass("dcr-4tgv3s").text();
        String[] parsedDate = dateText.substring(0, dateText.indexOf("GMT")).split("[\\s.]");
        Date date = new Date();
        date.setDate(Integer.parseInt(parsedDate[1]));
        date.setMonth(parsedDate[2]);
        date.setYear(Integer.parseInt(parsedDate[3]));
        date.setHours(Integer.parseInt(parsedDate[4]));
        date.incrementHours();
        date.incrementHours();
        date.incrementHours();
        date.setMinutes(Integer.parseInt(parsedDate[5]));

        return date;
    }

    private Image getImage(Element elem) {
        Element image = elem.getElementsByClass("dcr-13udsys").get(0);
        Attributes attrs = image.getElementsByClass("dcr-1989ovb").get(0).attributes();
        if (attrs.hasKey("src"))
            return new Image(attrs.get("src"));

        Elements sources = image.getElementsByTag("source");
        for (Element source : sources) {
            Attributes sourceAttrs = source.attributes();
            if (sourceAttrs.hasKey("srcset") && !sourceAttrs.get("srcset").equals(""))
                return new Image(sourceAttrs.get("srcset").split("\\s")[0]);
        }

        return new Image("");
    }
}
