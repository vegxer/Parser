package parsing.parsers.imagesSearcher;

import Threads.LoadingThread;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import parsing.NestingLevel;
import parsing.Parser;
import parsing.ParserSettings;
import parsing.ParserWorker;
import parsing.handlers.ParserHandler;
import parsing.model.Image;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class ImagesParserWorker extends ParserWorker<ArrayList<Image>> {
    private String savePath;

    public ImagesParserWorker(Parser<ArrayList<Image>> parser, ImagesParserSettings parserSettings, String savePath)
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
            ImagesParserSettings settings = (ImagesParserSettings)parserSettings;
            return new ImageLevel(settings.getStartImage(), settings.getEndImage(),
                    currElement.getElementsByClass("page-layout__column page-layout__column_type_content"));
        }

        @Override
        public Element getElementById(int id) throws IOException {
            ParserSettings.PREFIX = Integer.toString(id - 1);
            return loader.getSourceByPageId(ParserSettings.PREFIX);
        }
    }

    private class ImageLevel extends NestingLevel {
        public ImageLevel(int startPoint, int endPoint, Elements elements) {
            super(startPoint, endPoint, elements);
            isFinal = true;
        }

        @Override
        public NestingLevel getNextLvl(Element currElement) {
            return null;
        }

        @Override
        public Element getElementById(int id) {
            if (id > 30)
                return null;
            return getElement(0).getElementsByClass(String.format("serp-item serp-item_type_search serp-item" +
                            "_group_search serp-item_pos_%d serp-item_scale_yes justifier__item i-bem",
                    Integer.parseInt(ParserSettings.PREFIX) * 30 + id - 1)).get(0);
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


    public static class NewData implements ParserHandler<ParserWorker<ArrayList<Image>>, ArrayList<Image>> {
        @Override
        public void onAction(ParserWorker<ArrayList<Image>> sender, ArrayList<Image> data) {
            if (!(sender instanceof ImagesParserWorker))
                throw new IllegalArgumentException();
            Thread loading = new Thread(new LoadingThread());
            loading.start();
            String savePath = ((ImagesParserWorker) sender).getSavePath();

            if (savePath.charAt(savePath.length() - 1) != '/')
                savePath += "/";
            String name;
            int i = 0;
            boolean isDownloaded;
            do {
                int lastSlash = data.get(i).getUrl().lastIndexOf('/') + 1;
                int lastDot = data.get(i).getUrl().lastIndexOf('.');
                if (lastDot == data.get(i).getUrl().length() - 1)
                    name = "unnamed";
                else if (lastDot <= lastSlash)
                    name = data.get(i).getUrl().substring(lastSlash);
                else
                    name = data.get(i).getUrl().substring(lastSlash, lastDot);
                if (name.length() > 100)
                    name = name.substring(100);
            } while (!(isDownloaded = data.get(i).save(savePath + name)) && ++i < data.size());

            System.out.print("\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b");
            loading.stop();

            if (isDownloaded)
                System.out.println("Изображение " + data.get(i).getUrl() + " сохранено");
            else
                System.out.println("Ошибка сохранения изображения");
        }
    }

    public static class Completed implements ParserHandler<ParserWorker<ArrayList<Image>>, String> {
        @Override
        public void onAction(ParserWorker<ArrayList<Image>> sender, String data) {
            System.out.println(data);
        }
    }
}
