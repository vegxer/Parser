package parsing.parsers.theGuardianParser;

import parsing.ParserSettings;

public class GuardianSettings extends ParserSettings {
    private int startNews, endNews;

    public GuardianSettings(int startPoint, int endPoint, int startNews, int endNews) {
        super(startPoint, endPoint);
        setStartNews(startNews);
        setEndNews(endNews);
        BASE_URL = "https://www.theguardian.com";
        PREFIX = "{CurrentId}";
    }

    public int getStartNews() {
        return startNews;
    }

    public void setStartNews(int startNews) {
        if (startNews < 0)
            throw new IllegalArgumentException("Неверно указан номер новости");
        this.startNews = startNews;
    }

    public int getEndNews() {
        return endNews;
    }

    public void setEndNews(int endNews) {
        if (endNews < startNews)
            throw new IllegalArgumentException("Неверно указан номер страницы");
        this.endNews = endNews;
    }
}
