package parsing.websitesParsers.theGuardianParser;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import parsing.NestingLevel;
import parsing.Parser;
import parsing.ParserSettings;
import parsing.ParserWorker;
import parsing.handlers.ParserHandler;
import parsing.model.News;
import textEditor.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class GuardianParserWorker extends ParserWorker<News> {
    private String savePath;

    public GuardianParserWorker(Parser<News> parser, ParserSettings parserSettings, String savePath)
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
            GuardianSettings settings = (GuardianSettings)parserSettings;
            return new NewsLevel(settings.getStartNews(), settings.getEndNews(),
                    currElement.getElementsByClass("u-faux-block-link__overlay js-headline-text"));
        }

        @Override
        public Element getElementById(int id) throws IOException {
            return loader.getSourceByPageId("/world?page=" + id);
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
            String link = getElement(id - 1).attributes().get("href");
            if (id > 20)
                return null;
            if (link.contains("/video/"))
                return new Element("error").appendText("!!!Встретилась необрабатываемая \"видео-новость\" по ссылке "
                        + link + "!!!");
            if (link.contains("/gallery/"))
                return new Element("error").appendText("!!!Встретилась необрабатываемая \"фото-новость\" по ссылке "
                        + link + "!!!");
            if (link.contains("/live/"))
                return new Element("error").appendText("!!!Встретилась необрабатываемая \"новость в прямом эфире\"" +
                        " по ссылке " + link + "!!!");
            return loader.getSourceByPageId(link.substring(27)).appendText(getElement(id - 1).text());
        }
    }


    public static class NewData implements ParserHandler<ParserWorker<News>, News> {
        @Override
        public void onAction(ParserWorker<News> sender, News data) {
            System.out.println("\n");
            if (data.getText().contains("!!!Встретилась необрабатываемая")) {
                System.out.println(data.getText());
                return;
            }
            System.out.println("Заголовок новости:\n" + Text.splitByLines(data.getName(), 100));
            System.out.println("Дата: " + data.getDate().toString());
            System.out.print("Текст:\n" + Text.splitByLines(data.getText(), 100));
            if (!data.getImage().getUrl().isEmpty()) {
                String name = data.getName().replaceAll("[?<>|\"*:\\\\/\\n]", " ");
                if (name.length() > 100)
                    name = name.substring(0, 100);
                System.out.println("Ссылка на картинку: " + data.getImage().getUrl());
                if (data.getImage().save(((GuardianParserWorker)sender).getSavePath() + "/" + name))
                    System.out.println("Изображение \"" + name + "\" сохранено");
                else
                    System.out.println("Ошибка сохранения изображения " + data.getImage().getUrl());
            }
        }
    }

    public static class Completed implements ParserHandler<ParserWorker<News>, String> {
        @Override
        public void onAction(ParserWorker<News> sender, String data) {
            System.out.println(data);
        }
    }


    public void setSavePath(String savePath) throws FileNotFoundException {
        if (savePath == null)
            throw new NullPointerException();
        if (!new File(savePath).exists() || !new File(savePath).isDirectory())
            throw new FileNotFoundException("Указанная директория не найдена");
        this.savePath = savePath;
    }

    public String getSavePath() {
        return savePath;
    }
}
