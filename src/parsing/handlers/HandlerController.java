package parsing.handlers;

import java.io.IOException;
import java.util.ArrayList;

public class HandlerController<D> {
    private final ArrayList<ParserHandler<D>> onActionList;


    public HandlerController() {
        onActionList = new ArrayList<>();
    }

    public HandlerController(ArrayList<ParserHandler<D>> handlers) {
        onActionList = new ArrayList<>(handlers);
    }


    public void onAction(D data) throws IOException {
        for (ParserHandler<D> handler : onActionList)
            handler.onAction(data);
    }

    public void addOnActionHandler(ParserHandler<D> handler) {
        if (handler == null)
            throw new NullPointerException();
        onActionList.add(handler);
    }

    public void removeOnActionHandler(ParserHandler<D> handler) {
        if (handler == null)
            throw new NullPointerException();
        onActionList.remove(handler);
    }

    public void removeOnNewDataHandler(int index) {
        onActionList.remove(index);
    }

}
