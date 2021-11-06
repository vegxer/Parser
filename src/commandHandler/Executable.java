package commandHandler;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;

public interface Executable {
    void execute(ArrayList<String> commandArgs) throws IOException, ParseException, java.text.ParseException;
}
