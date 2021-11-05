package parsing.parsers.internetShopsParser;

import org.jsoup.nodes.Element;
import parsing.Parser;
import parsing.model.Review;
import time.Date;

public class ShopReviewParser implements Parser<Review> {
    @Override
    public Review parse(Element document) {
        String[] parsedDate = document.getElementsByAttributeValue("itemprop", "datePublished")
                .get(0).attributes().get("content").split("-");
        Date date = new Date();
        date.setYear(Integer.parseInt(parsedDate[0]));
        date.setMonth(Integer.parseInt(parsedDate[1]) - 1);
        date.setDate(Integer.parseInt(parsedDate[2]));
        return new Review(document.getElementsByAttributeValue("itemprop", "name").get(0).attributes().get("content"),
                date,
                Integer.parseInt(document.getElementsByAttributeValue("itemprop", "ratingValue").text()),
                document.getElementsByAttributeValue("itemprop", "author").text(),
                document.getElementsByAttributeValue("itemprop", "reviewBody").text(),
                document.getElementsByAttributeValue("itemprop", "pro").text(),
                document.getElementsByAttributeValue("itemprop", "contra").text());
    }

}
