package parsing.websitesParsers.newslerParser;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import parsing.NestingLevel;
import parsing.Parser;
import parsing.ParserSettings;
import parsing.ParserWorker;
import parsing.handlers.ParserHandler;
import parsing.model.News;
import parsing.websitesParsers.theGuardianParser.GuardianParserWorker;
import textEditor.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class NewslerParserWorker extends ParserWorker<News> {
    private String savePath;

    public NewslerParserWorker(Parser<News> parser, ParserSettings parserSettings, String savePath)
            throws FileNotFoundException {
        super(parser, parserSettings);
        setSavePath(savePath);
    }

    @Override
    protected NestingLevel getFirstLvl() {
        return new PageLevel(parserSettings.getStartPoint(), parserSettings.getEndPoint());
    }

    private class PageLevel extends NestingLevel {
        public PageLevel(int startPoint, int endPoint) {
            super(startPoint, endPoint);
        }

        @Override
        public NestingLevel getNextLvl(Element currElement) {
            NewslerSettings settings = (NewslerSettings) parserSettings;
            return new NewsLevel(settings.getStartNews(), settings.getEndNews(),
                    currElement.getElementsByClass("zag"));
        }

        @Override
        public Element getElementById(int id) throws IOException {
            return loader.getSourceByPageId("www.newsler.ru/news?p=" + id);
        }
    }

    private class NewsLevel extends NestingLevel {
        public NewsLevel(int startPoint, int endPoint, Elements elements) {
            super(startPoint, endPoint, elements);
            isFinal = true;
        }

        @Override
        public NestingLevel getNextLvl(Element currElement) {
            return null;
        }

        @Override
        public Element getElementById(int id) throws IOException {
            if (id > 22)
                return null;
            return loader.getSourceByPageId(getElement(id - 1).getElementsByTag("a")
                    .attr("href").substring(8));
        }
    }


    @Override
    public void onNewData(News data) {
        System.out.println("\n");
        System.out.println("?????????????????? ??????????????:\n" + Text.splitByLines(data.getName(), 100));
        System.out.println("????????: " + data.getDate().toString());
        System.out.println("??????????:\n" + Text.splitByLines(data.getText(), 100));
        if (!data.getImage().getUrl().isEmpty()) {
            String name = data.getName().replaceAll("[?<>|\"*:\\\\/\\n]", " ");
            if (name.length() > 100)
                name = name.substring(0, 100);
            String savePath = getSavePath();
            if (savePath.charAt(savePath.length() - 1) != '/')
                savePath += "/";
            System.out.println("???????????? ???? ????????????????: " + data.getImage().getUrl());
            if (data.getImage().download(savePath + name))
                System.out.println("?????????????????????? ?????????????????? ?????? ?????????????????? \"" + name + "\"");
            else
                System.out.println("???????????? ???????????????????? ??????????????????????");
        }
    }


    public void setSavePath(String savePath) throws FileNotFoundException {
        if (savePath == null)
            throw new NullPointerException();
        if (!new File(savePath).exists() || !new File(savePath).isDirectory())
            throw new FileNotFoundException("?????????????????? ???????????????????? ???? ??????????????");
        this.savePath = savePath;
    }

    public String getSavePath() {
        return savePath;
    }
}
