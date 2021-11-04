package parsing;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class HtmlLoader {
    private String url;

    public HtmlLoader(String url) {
        this.url = url;
    }

    public Document getSourceByPageId(String id) throws IOException {
        String currentUrl = url.replace("{CurrentId}", id);
        return Jsoup.connect(currentUrl).get();
    }

    public Document getSource() throws IOException {
        return Jsoup.connect(url).get();
    }

    public Document getBaseSource() throws IOException {
        return Jsoup.connect(ParserSettings.BASE_URL).get();
    }


    public void setUrl(String url) {
        if (url == null)
            throw new NullPointerException();
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
