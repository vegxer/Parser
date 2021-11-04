package parsing.internetShopsParser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import parsing.NestingLevel;
import parsing.Parser;
import parsing.ParserSettings;
import parsing.ParserWorker;
import parsing.handlers.ParserHandler;
import parsing.model.Review;
import parsing.model.Reviews;

import java.io.IOException;

public class ShopReviewParserWorker extends ParserWorker<Reviews> {
    public ShopReviewParserWorker(Parser<Reviews> parser, ShopReviewSettings parserSettings) {
        super(parser, parserSettings);
    }


    protected NestingLevel getFirstLvl() {
        return new ShopPageLevel(parserSettings.getStartPoint(), parserSettings.getEndPoint(),
                Jsoup.parse("internet-magaziny").getAllElements());
    }

    private class ShopPageLevel extends NestingLevel {
        public ShopPageLevel(int startPoint, int endPoint, Elements elements) {
            super(startPoint, endPoint, elements);
        }

        @Override
        public NestingLevel getNextLvl(Element currElement) {
            ShopReviewSettings settings = (ShopReviewSettings) parserSettings;
            return new ShopLevel(settings.getShopStart(), settings.getShopEnd(),
                    currElement.getElementsByClass("ss"));
        }

        @Override
        public Element getElementById(int id) throws IOException {
            ParserSettings.PREFIX = getElement(0).text() + "?page=" + id;
            return loader.getSourceByPageId(ParserSettings.PREFIX);
        }
    }

    private class ShopLevel extends NestingLevel {
        public ShopLevel(int startPoint, int endPoint, Elements elements) {
            super(startPoint, endPoint, elements);
        }

        @Override
        public NestingLevel getNextLvl(Element currElement) {
            ShopReviewSettings settings = (ShopReviewSettings) parserSettings;
            return new ReviewPageLevel(settings.getReviewsPagesStart(), settings.getReviewsPagesEnd(),
                    Jsoup.parse(currElement.attributes().get("href").substring(1)).getAllElements());
        }

        @Override
        public Element getElementById(int id) {
            return getElement(id - 1);
        }
    }

    private class ReviewPageLevel extends NestingLevel {
        public ReviewPageLevel(int startPoint, int endPoint, Elements elements) {
            super(startPoint, endPoint, elements);
        }

        @Override
        public NestingLevel getNextLvl(Element currElement) {
            ShopReviewSettings settings = (ShopReviewSettings) parserSettings;
            return new ReviewLevel(settings.getReviewStart(), settings.getReviewEnd(),
                    currElement.getElementsByClass("reviewers-box"));
        }

        @Override
        public Element getElementById(int id) throws IOException {
            ParserSettings.PREFIX = getElement(0).text() + "?page=" + id;
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
            return getElement(id - 1);
        }
    }


    public static class Completed implements ParserHandler<ParserWorker<Reviews>, String> {
        @Override
        public void onAction(ParserWorker<Reviews> sender, String data) {
            System.out.println(data);
        }
    }

    public static class NewData implements ParserHandler<ParserWorker<Reviews>, Reviews> {
        @Override
        public void onAction(ParserWorker<Reviews> sender, Reviews data) {
            System.out.println("Магазин: " + data.getName());
            System.out.println("Номер страницы отзывов: " +
                    ParserSettings.PREFIX.substring(ParserSettings.PREFIX.lastIndexOf('=') + 1));
            for (Review review : data.getReviews()) {
                System.out.println("Автор: " + review.getReviewerName());
                System.out.println("Оценка: " + review.getGrade() + "/5");
                System.out.println("Плюсы: " + review.getPros());
                System.out.println("Минусы: " + review.getCons());
                System.out.println("Отзыв: " + review.getReview());
                System.out.println();
            }
        }
    }
}
