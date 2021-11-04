package parsing;

public abstract class ParserSettings {
    // Адрес сайта
    public static String BASE_URL;
    // Префикс страницы
    public static String PREFIX;
    // Начало пагинации
    protected int startPoint;
    // Конец пагинации
    protected int endPoint;

    public ParserSettings(int startPoint, int endPoint) {
        setEndPoint(endPoint);
        setStartPoint(startPoint);
    }

    public int getStartPoint() {
        return startPoint;
    }

    public int getEndPoint() {
        return endPoint;
    }

    public void setStartPoint(int startPoint) {
        if (startPoint < 1)
            throw new IllegalArgumentException("Неверно указан номер страницы");
        this.startPoint = startPoint;
    }

    public void setEndPoint(int endPoint) {
        if (endPoint < startPoint)
            throw new IllegalArgumentException("Неверно указан номер страницы");
        this.endPoint = endPoint;
    }
}
