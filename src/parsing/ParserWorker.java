package parsing;

import org.json.simple.parser.ParseException;
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


    public void start() throws IOException, ParseException {
        isActive = true;
        work();
    }

    public void abort() {
        isActive = false;
    }

    protected abstract NestingLevel getFirstLvl();

    private void work() throws IOException, ParseException {
        work(getFirstLvl());
        onCompleted.onAction("Загрузка закончена");
        isActive = false;
    }

    private void work(NestingLevel nestLvl) throws IOException, ParseException {
        if (nestLvl.isFinal())
            parseLastLvl(nestLvl);
        else {
            for (int i = nestLvl.getStartPoint(); i <= nestLvl.getEndPoint(); ++i) {
                if (!isActive) {
                    onCompleted.onAction("Загрузка закончена");
                    return;
                }
                work(nestLvl.getNextLvl(nestLvl.getElementById(i)));
            }
        }
    }

    private void parseLastLvl(NestingLevel lastLvl) throws IOException, ParseException {
        for (int i = lastLvl.getStartPoint(); i <= lastLvl.getEndPoint(); ++i) {
            T result = parser.parse(lastLvl.getElementById(i));
            onNewData.onAction(result);
        }
    }

}
