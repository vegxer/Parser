package parsing.websitesParsers.leroyMerlinParser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import parsing.NestingLevel;
import parsing.Parser;
import parsing.ParserSettings;
import parsing.ParserWorker;
import parsing.handlers.ParserHandler;
import parsing.model.Review;
import textEditor.Text;

import java.io.IOException;

public class LeroyMerlinParserWorker extends ParserWorker<Review> {
    public LeroyMerlinParserWorker(Parser<Review> parser, LeroyMerlinSettings parserSettings) {
        super(parser, parserSettings);
    }


    protected NestingLevel getFirstLvl() {
        return new ProductPageLevel(parserSettings.getStartPoint(), parserSettings.getEndPoint(),
                new Elements(Jsoup.parse("search/?q=" + ((LeroyMerlinSettings)parserSettings).getSearchQuery() + "&page=")));
    }

    private class ProductPageLevel extends NestingLevel {
        public ProductPageLevel(int startPoint, int endPoint, Elements elements) {
            super(startPoint, endPoint, elements);
        }

        @Override
        public NestingLevel getNextLvl(Element currElement) {
            LeroyMerlinSettings settings = (LeroyMerlinSettings) parserSettings;
            return new ProductLevel(settings.getProductStart(), settings.getProductEnd(),
                    currElement.getElementsByClass("bex6mjh_plp b1f5t594_plp p5y548z_plp pblwt5z_plp nf842w" +
                            "f_plp"));
        }

        @Override
        public Element getElementById(int id) throws IOException {
            ParserSettings.PREFIX = getElement(0).text() + id;
            return loader.getSourceByPageId(ParserSettings.PREFIX);
        }
    }

    private class ProductLevel extends NestingLevel {
        public ProductLevel(int startPoint, int endPoint, Elements elements) {
            super(startPoint, endPoint, elements);
        }

        @Override
        public NestingLevel getNextLvl(Element currElement) {
            LeroyMerlinSettings settings = (LeroyMerlinSettings) parserSettings;
            return new ReviewPageLevel(settings.getReviewsPagesStart(), settings.getReviewsPagesEnd(),
                    new Elements(Jsoup.parse(currElement.attributes().get("href").substring(1) + "?page=")));
        }

        @Override
        public Element getElementById(int id) {
            if (id > 30 || id > getElements().size())
                return null;
            return getElement(id - 1);
        }
    }

    private class ReviewPageLevel extends NestingLevel {
        public ReviewPageLevel(int startPoint, int endPoint, Elements elements) {
            super(startPoint, endPoint, elements);
        }

        @Override
        public NestingLevel getNextLvl(Element currElement) {
            LeroyMerlinSettings settings = (LeroyMerlinSettings)parserSettings;
            Elements reviews = currElement.getElementsByTag("uc-prp-review-card");
            reviews.add(0, Jsoup.parse(currElement.getElementsByAttributeValue("slot", "title").get(0).text()));
            return new ReviewLevel(settings.getReviewStart(), settings.getReviewEnd(), reviews);
        }

        @Override
        public Element getElementById(int id) throws IOException {
            ParserSettings.PREFIX = getElement(0).text() + id;
            return loader.getSourceByPageId(ParserSettings.PREFIX);
        }
    }

    private class ReviewLevel extends NestingLevel {
        public ReviewLevel(int startPoint, int endPoint, Elements elements) {
            super(startPoint, endPoint, elements);
            isFinal = true;
        }

        @Override
        public NestingLevel getNextLvl(Element currElement) {
            return null;
        }

        @Override
        public Element getElementById(int id) {
            if (id > 5 || id > getElements().size() - 1)
                return null;
            return getElement(id).appendText(getElement(0).text());
        }
    }


    public static class Completed implements ParserHandler<ParserWorker<Review>, String> {
        @Override
        public void onAction(ParserWorker<Review> sender, String data) {
            System.out.println(data);
        }
    }

    public static class NewData implements ParserHandler<ParserWorker<Review>, Review> {
        @Override
        public void onAction(ParserWorker<Review> sender, Review data) {
            System.out.println("\n");
            System.out.println("Название товара: " + data.getShopName());
            System.out.println("Номер страницы отзывов: " +
                    ParserSettings.PREFIX.substring(ParserSettings.PREFIX.lastIndexOf('=') + 1));
            System.out.println("Дата: " + data.getDate().toString().substring(0, 10));
            System.out.println("Автор: " + data.getReviewerName());
            System.out.println("Оценка: " + data.getGrade() + "/5");
            System.out.println(Text.splitByLines(data.getPros(), 100));
            System.out.println(Text.splitByLines(data.getCons(), 100));
            System.out.println("Отзыв: " + Text.splitByLines(data.getReview(), 100));
        }
    }
}
