package parsing.handlers;

import java.io.IOException;

public interface ParserHandler<D> {
    void onAction(D data) throws IOException;
}
