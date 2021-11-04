package parsing.handlers;

import java.io.IOException;
import java.util.ArrayList;

public class HandlerController<S, D> {
    private S sender;
    private final ArrayList<ParserHandler<S, D>> onActionList;


    public HandlerController(S sender) {
        this.sender = sender;
        onActionList = new ArrayList<>();
    }

    public HandlerController(ArrayList<ParserHandler<S, D>> handlers) {
        onActionList = new ArrayList<>(handlers);
    }


    public void onAction(D data) throws IOException {
        for (ParserHandler<S, D> handler : onActionList)
            handler.onAction(sender, data);
    }

    public void addOnActionHandler(ParserHandler<S, D> handler) {
        if (handler == null)
            throw new NullPointerException();
        onActionList.add(handler);
    }

    public void removeOnActionHandler(ParserHandler<S, D> handler) {
        if (handler == null)
            throw new NullPointerException();
        onActionList.remove(handler);
    }

    public void removeOnNewDataHandler(int index) {
        onActionList.remove(index);
    }

    public S getSender() {
        return sender;
    }

    public void setSender(S sender) {
        this.sender = sender;
    }
}
