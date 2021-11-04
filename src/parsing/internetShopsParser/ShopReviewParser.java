package parsing.internetShopsParser;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import parsing.Parser;
import parsing.model.Review;
import parsing.model.Reviews;

public class ShopReviewParser implements Parser<Reviews> {
    @Override
    public Reviews parse(Element document) {
        Reviews reviews = new Reviews(document
                .getElementsByAttributeValue("itemprop", "name").get(0).attributes().get("content"));

        Elements reviewsElems = document.getElementsByClass("reviewers-box");
        for (Element review : reviewsElems) {
            reviews.addReview(new Review(
                    Integer.parseInt(review.getElementsByAttributeValue("itemprop", "ratingValue").text()),
                    review.getElementsByAttributeValue("itemprop", "author").text(),
                    review.getElementsByAttributeValue("itemprop", "reviewBody").text(),
                    review.getElementsByAttributeValue("itemprop", "pro").text(),
                    review.getElementsByAttributeValue("itemprop", "contra").text()));
        }

        return reviews;
    }

}
