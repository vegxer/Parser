package parsing.websitesParsers.internetShopsParser;

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

public class ShopReviewParserWorker extends ParserWorker<Review> {
    public ShopReviewParserWorker(Parser<Review> parser, ShopReviewSettings parserSettings) {
        super(parser, parserSettings);
    }

    @Override
    protected NestingLevel getFirstLvl() {
        return new ShopPageLevel(parserSettings.getStartPoint(), parserSettings.getEndPoint());
    }

    private class ShopPageLevel extends NestingLevel {
        public ShopPageLevel(int startPoint, int endPoint) {
            super(startPoint, endPoint);
        }

        @Override
        public NestingLevel getNextLvl(Element currElement) {
            ShopReviewSettings settings = (ShopReviewSettings) parserSettings;
            return new ShopLevel(settings.getShopStart(), settings.getShopEnd(),
                    currElement.getElementsByClass("ss"));
        }

        @Override
        public Element getElementById(int id) throws IOException {
            ParserSettings.PREFIX = "internet-magaziny?page=" + id;
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
                    new Elements(Jsoup.parse(currElement.attributes().get("href").substring(1))));
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
            if (id > 30 || id > getElements().size())
                return null;
            return getElement(id - 1);
        }
    }


    @Override
    public void onNewData(Review data) {
        System.out.println("\n");
        System.out.println("??????????????: " + data.getShopName());
        System.out.println("?????????? ???????????????? ??????????????: " +
                ParserSettings.PREFIX.substring(ParserSettings.PREFIX.lastIndexOf('=') + 1));
        System.out.println("????????: " + data.getDate().toString().substring(0, 10));
        System.out.println("??????????: " + data.getReviewerName());
        System.out.println("????????????: " + data.getGrade() + "/5");
        System.out.println("??????????: " + Text.splitByLines(data.getPros(), 100));
        System.out.println("????????????: " + Text.splitByLines(data.getCons(), 100));
        System.out.println("??????????: " + Text.splitByLines(data.getReview(), 100));
    }
}
