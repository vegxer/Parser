package parsing.parsers.yandexImagesParser;

import parsing.ParserSettings;

public class ImagesParserSettings extends ParserSettings {
    private int startImage, endImage;
    private String searchQuery;

    public ImagesParserSettings(int start, int end, int startImage, int endImage, String searchQuery) {
        super(start, end);
        setSearchQuery(searchQuery);
        setEndImage(endImage);
        setStartImage(startImage);
        BASE_URL = "https://yandex.ru/images/search?text=" + this.searchQuery;
        PREFIX = "&p={CurrentId}";
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        if (searchQuery == null)
            throw new NullPointerException();
        this.searchQuery = searchQuery;
    }

    public int getEndImage() {
        return endImage;
    }

    public void setEndImage(int endImage) {
        if (endImage < startImage)
            throw new IllegalArgumentException("Неверно указан номер страницы");
        this.endImage = endImage;
    }

    public int getStartImage() {
        return startImage;
    }

    public void setStartImage(int startImage) {
        if (startImage < 1)
            throw new IllegalArgumentException("Неверно указан номер страницы");
        this.startImage = startImage;
    }
}
