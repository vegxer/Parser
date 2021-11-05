package parsing.leroyMerlinParser;

import parsing.ParserSettings;

public class LeroyMerlinSettings extends ParserSettings {
    String searchQuery;
    private int productStart, productEnd;
    private int reviewsPagesStart, reviewsPagesEnd;
    private int reviewStart, reviewEnd;

    public LeroyMerlinSettings(int start, int end, int shopStart, int shopEnd, int reviewsPagesStart,
                               int reviewsPagesEnd, int reviewStart, int reviewEnd, String searchQuery) {
        super(start, end);
        setReviewsPagesEnd(reviewsPagesEnd);
        setReviewsPagesStart(reviewsPagesStart);
        setProductStart(shopStart);
        setProductEnd(shopEnd);
        setReviewStart(reviewStart);
        setReviewEnd(reviewEnd);
        setSearchQuery(searchQuery);
        BASE_URL = "https://leroymerlin.ru";
        PREFIX = "/{CurrentId}";
    }

    public int getReviewsPagesStart() {
        return reviewsPagesStart;
    }

    public void setReviewsPagesStart(int reviewsPagesStart) {
        if (reviewsPagesStart < 1)
            throw new IllegalArgumentException("Неверно указан номер страницы");
        this.reviewsPagesStart = reviewsPagesStart;
    }

    public int getReviewsPagesEnd() {
        return reviewsPagesEnd;
    }

    public void setReviewsPagesEnd(int reviewsPagesEnd) {
        if (reviewsPagesEnd < reviewsPagesStart)
            throw new IllegalArgumentException("Неверно указан номер страницы");
        this.reviewsPagesEnd = reviewsPagesEnd;
    }

    public int getProductStart() {
        return productStart;
    }

    public void setProductStart(int productStart) {
        if (productStart < 1)
            throw new IllegalArgumentException("Неверно указан номер страницы");
        this.productStart = productStart;
    }

    public int getProductEnd() {
        return productEnd;
    }

    public void setProductEnd(int productEnd) {
        if (productEnd < productStart)
            throw new IllegalArgumentException("Неверно указан номер страницы");
        this.productEnd = productEnd;
    }

    public int getReviewStart() {
        return reviewStart;
    }

    public void setReviewStart(int reviewStart) {
        if (reviewStart < 1)
            throw new IllegalArgumentException("Неверно указан номер страницы");
        this.reviewStart = reviewStart;
    }

    public int getReviewEnd() {
        return reviewEnd;
    }

    public void setReviewEnd(int reviewEnd) {
        if (reviewEnd < reviewStart)
            throw new IllegalArgumentException("Неверно указан номер страницы");
        this.reviewEnd = reviewEnd;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        if (searchQuery == null)
            throw new NullPointerException();
        this.searchQuery = searchQuery;
    }
}
