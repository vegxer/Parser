package parsing.parsers.newslerParser;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import parsing.Parser;
import parsing.ParserSettings;
import parsing.model.Image;
import parsing.model.News;
import time.Date;

import java.util.Calendar;

public class NewslerParser implements Parser<News> {
    @Override
    public News parse(Element document) {
        String imageUrl;
        Elements imageElement = document.getElementsByClass("image");
        if (imageElement.isEmpty()) {
            imageElement = document.getElementsByClass("i");
            if (imageElement.isEmpty())
                imageUrl = "Изображение новости не найдено";
            else {
                imageUrl = imageElement.get(2).getElementsByTag("img").get(0).attributes().get("src");
                imageUrl = "afisha.newsler.ru/" + imageUrl;
            }
        }
        else {
            imageUrl = imageElement.get(0).getElementsByTag("img").get(0).attributes().get("src");
            imageUrl = "afisha.newsler.ru/" + imageUrl;
        }

        return new News(document.getElementsByClass("news-page-content-header").get(0)
                .getElementsByTag("h1").text() + "\n" + document.getElementsByClass("news-introtext").text(),
                getText(document),
                getDate(document),
                new Image(ParserSettings.BASE_URL + imageUrl));
    }

    private String getText(Element elem) {
        Elements textElements = elem.getElementsByClass("block-text text-content")
                .get(0).getElementsByTag("p");

        String text = "";
        for (Element textElement : textElements) {
            String tagText = textElement.text().replace("\n", "");
            if (!tagText.isEmpty())
                text = text.concat(tagText + "\n\n");
        }

        text = text.substring(0, text.length() - 1);
        return text;
    }

    private Date getDate(Element elem) {
        String[] parsedDate = elem.getElementsByClass("time-author").text()
                .replace(",", "").split("[\\s:]");
        Date date = new Date();
        if (parsedDate[0].equals("Сегодня") || parsedDate[0].equals("Вчера")) {
            Calendar calendar = Calendar.getInstance();
            date.setDate(calendar.get(Calendar.DATE));
            date.setYear(calendar.get(Calendar.YEAR));
            date.setMonth(calendar.get(Calendar.MONTH));
            if (parsedDate[0].equals("Вчера"))
                date.decrementDate();
            date.setHours(Integer.parseInt(parsedDate[1]));
            date.setMinutes(Integer.parseInt(parsedDate[2]));
        }
        else {
            date.setDate(Integer.parseInt(parsedDate[0]));
            date.setMonth(parsedDate[1]);
            date.setYear(Integer.parseInt(parsedDate[2]));
            date.setHours(Integer.parseInt(parsedDate[3]));
            date.setMinutes(Integer.parseInt(parsedDate[4]));
        }

        return date;
    }
}
