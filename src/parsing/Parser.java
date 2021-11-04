package parsing;

import org.json.simple.parser.ParseException;
import org.jsoup.nodes.Element;

import java.io.IOException;

public interface Parser<T> {
    T parse(Element document) throws IOException, ParseException;
}
