package parsing.parsers.internetShopsParser;

import parsing.ParserSettings;

public class ShopReviewSettings extends ParserSettings {
    private int shopStart, shopEnd;
    private int reviewsPagesStart, reviewsPagesEnd;
    private int reviewStart, reviewEnd;

    public ShopReviewSettings(int start, int end, int shopStart, int shopEnd,
                              int reviewsPagesStart, int reviewsPagesEnd, int reviewStart, int reviewEnd) {
        super(start, end);
        BASE_URL = "https://nanegative.ru";
        PREFIX = "/{CurrentId}";
        setReviewsPagesEnd(reviewsPagesEnd);
        setReviewsPagesStart(reviewsPagesStart);
        setShopStart(shopStart);
        setShopEnd(shopEnd);
        setReviewStart(reviewStart);
        setReviewEnd(reviewEnd);
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

    public int getShopStart() {
        return shopStart;
    }

    public void setShopStart(int shopStart) {
        if (shopStart < 1)
            throw new IllegalArgumentException("Неверно указан номер страницы");
        this.shopStart = shopStart;
    }

    public int getShopEnd() {
        return shopEnd;
    }

    public void setShopEnd(int shopEnd) {
        if (shopEnd < shopStart)
            throw new IllegalArgumentException("Неверно указан номер страницы");
        this.shopEnd = shopEnd;
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
}
