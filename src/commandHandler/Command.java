package commandHandler;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;

public class Command implements Executable {
    private Executable command;
    private String commandName, description;
    private final ArrayList<String> argsDescription;


    public Command(String commandName, String commandDescription, Executable command) {
        setCommand(command);
        setCommandName(commandName);
        setDescription(commandDescription);
        argsDescription = new ArrayList<>();
    }

    public Command(String commandName, String commandDescription, ArrayList<String> argsDescription, Executable command) {
        this(commandName, commandDescription, command);
        if (argsDescription == null)
            throw new NullPointerException();
        this.argsDescription.addAll(argsDescription);
    }


    @Override
    public void execute(ArrayList<String> commandArgs) throws IOException, ParseException {
        this.command.execute(commandArgs);
    }


    public void setCommand(Executable command) {
        if (command == null)
            throw new NullPointerException();
        this.command = command;
    }

    public String getCommandName() {
        return commandName;
    }

    public void setCommandName(String commandName) {
        if (commandName == null)
            throw new NullPointerException();
        this.commandName = commandName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description == null)
            throw new NullPointerException();
        this.description = description;
    }

    public ArrayList<String> getArgsDescription() {
        return new ArrayList<>(argsDescription);
    }

    public void insertArgDescription(String argDescription, int index) {
        if (argDescription == null)
            throw new NullPointerException();
        argsDescription.add(index, argDescription);
    }

    public void removeArgDescription(int index) {
        argsDescription.remove(index);
    }
}
