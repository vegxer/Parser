package parsing;

import org.json.simple.parser.ParseException;
import org.jsoup.nodes.Element;
import parsing.handlers.HandlerController;

import java.io.IOException;

public abstract class ParserWorker<T> {
    protected final Parser<T> parser;
    protected final ParserSettings parserSettings;
    protected final HtmlLoader loader;
    private boolean isActive;
    public final HandlerController<ParserWorker<T>, T> onNewData;
    public final HandlerController<ParserWorker<T>, String> onCompleted;


    public ParserWorker(Parser<T> parser, ParserSettings parserSettings) {
        onNewData = new HandlerController<>(this);
        onCompleted = new HandlerController<>(this);
        this.parser = parser;
        loader = new HtmlLoader(ParserSettings.BASE_URL + ParserSettings.PREFIX);
        this.parserSettings = parserSettings;
    }


    public void start() throws IOException, ParseException, java.text.ParseException {
        isActive = true;
        work();
    }

    public void abort() {
        isActive = false;
    }

    protected abstract NestingLevel getFirstLvl();

    private void work() throws IOException, ParseException, java.text.ParseException {
        work(getFirstLvl());
        onCompleted.onAction("Загрузка закончена");
        isActive = false;
    }

    private void work(NestingLevel nestLvl) throws IOException, ParseException, java.text.ParseException {
        if (nestLvl.isFinal())
            parseLastLvl(nestLvl);
        else {
            for (int i = nestLvl.getStartPoint(); i <= nestLvl.getEndPoint(); ++i) {
                if (!isActive) {
                    onCompleted.onAction("Загрузка закончена");
                    return;
                }
                Element currElement = nestLvl.getElementById(i);
                if (currElement == null)
                    return;
                work(nestLvl.getNextLvl(currElement));
            }
        }
    }

    private void parseLastLvl(NestingLevel lastLvl) throws IOException, ParseException, java.text.ParseException {
        for (int i = lastLvl.getStartPoint(); i <= lastLvl.getEndPoint(); ++i) {
            Element currElement = lastLvl.getElementById(i);
            if (currElement == null)
                return;
            T result = parser.parse(currElement);
            onNewData.onAction(result);
        }
    }

}
