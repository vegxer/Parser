package parsing.leroyMerlinParser;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import parsing.Parser;
import parsing.model.Review;
import parsing.model.Reviews;
import time.Date;

public class LeroyMerlinParser implements Parser<Review> {
    @Override
    public Review parse(Element document) {
            Elements proContra = document.getElementsByClass("term-group");
            String pro = "Достоинства: не указано", cons = "Недостатки: не указано";
            if (proContra.size() > 0) {
                pro = proContra.get(0).text();
                if (proContra.size() == 2)
                    cons = proContra.get(1).text();
            }

            String[] parsedDate = document.getElementsByAttributeValue("itemprop", "datePublished").text().split("\\s");
            Date date = new Date();
            date.setDate(Integer.parseInt(parsedDate[0]));
            date.setMonth(parsedDate[1]);
            date.setYear(Integer.parseInt(parsedDate[2]));

            return new Review(document.ownText(), date,
                    (int)(Double.parseDouble(document.getElementsByClass("review-card__review-score-stars")
                            .get(0).attributes().get("value"))),
                    document.getElementsByAttributeValue("itemprop", "name").text(),
                    document.getElementsByAttributeValue("itemprop", "description").text(),
                    pro, cons);
    }

}
