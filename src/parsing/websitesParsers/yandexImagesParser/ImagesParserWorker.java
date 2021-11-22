package parsing.websitesParsers.yandexImagesParser;

import urlImage.threads.LoadingThread;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import parsing.NestingLevel;
import parsing.Parser;
import parsing.ParserSettings;
import parsing.ParserWorker;
import parsing.handlers.ParserHandler;
import urlImage.Image;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
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
            ImagesParserSettings settings = (ImagesParserSettings) parserSettings;
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
        public Element getElementById(int id) throws ParseException {
            if (id > 30)
                return null;
            if (getElements().size() == 0)
                throw new ParseException("требуется ввод капчи", 1);
            Elements image = getElement(0).select(String.format("[class~=serp-item serp-item_type_search serp-item" +
                            "_group_search serp-item_pos_%d serp-item_scale_yes.+justifier__item i-bem]",
                    Integer.parseInt(ParserSettings.PREFIX) * 30 + id - 1));
            if (image.size() == 0)
                return null;
            return image.get(0);
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


    @Override
    public void onNewData(ArrayList<Image> data) {
        System.out.println();
        Thread loading = new Thread(new LoadingThread());
        loading.start();

        String savePath = getSavePath();
        if (savePath.charAt(savePath.length() - 1) != '/')
            savePath += "/";
        boolean isDownloaded = false;
        String name = "";
        int i;
        for (i = 0; !isDownloaded && i < data.size(); ++i) {
            name = data.get(i).getName();
            isDownloaded = data.get(i).download(savePath + name);
        }

        loading.stop();
        System.out.print("\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b");

        if (isDownloaded)
            System.out.println("Изображение " + data.get(i - 1).getUrl() + " сохранено\nпод именем \"" + name + "\"");
        else
            System.out.println("Ошибка сохранения изображения");
    }

}
