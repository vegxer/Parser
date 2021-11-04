package parsing.handlers;

import java.io.IOException;

public interface ParserHandler<S, D> {
    void onAction(S sender, D data) throws IOException;
}
