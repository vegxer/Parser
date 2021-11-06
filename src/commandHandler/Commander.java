package commandHandler;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Commander {
    private final ArrayList<Command> commands;


    public Commander() {
        commands = new ArrayList<>();
    }

    public Commander(ArrayList<Command> commands) {
        this();
        if (commands == null)
            throw new NullPointerException();
        for (Command command : commands)
            addCommand(command);
    }


    public void executeCommand(String command) throws IOException, ParseException, java.text.ParseException {
        ArrayList<String> arguments = splitArguments(command);
        if (arguments.size() == 0)
            throw new IllegalArgumentException("Неверно введена команда");

        String commandName = arguments.get(0);
        arguments.remove(0);
        getBy(commandName, arguments.size()).execute(arguments);
    }

    public Command getBy(String commandName, int argsCount) {
        boolean existsName = false;
        for (Command command : commands) {
            if (command.getCommandName().equals(commandName)) {
                existsName = true;
                if (argsCount == command.getArgsDescription().size())
                    return command;
            }
        }

        if (existsName)
            throw new IllegalArgumentException("Неверно указаны параметры команды " + commandName);
        throw new IllegalArgumentException("Команды " + commandName + " не существует");
    }

    public boolean exists(String commandName, int argsCount) {
        for (Command command : commands) {
            if (command.getCommandName().equals(commandName) && argsCount == command.getArgsDescription().size())
                return true;
        }

        return false;
    }

    public void addCommand(Command command) {
        if (command == null)
            throw new NullPointerException();
        if (exists(command.getCommandName(), command.getArgsDescription().size()))
            throw new IllegalArgumentException("Команда с такими параметрами уже существует");
        commands.add(command);
    }

    public ArrayList<Command> getCommands() {
        return new ArrayList<>(commands);
    }


    private ArrayList<String> splitArguments(String command) {
        int state = 0;
        StringTokenizer tok = new StringTokenizer(command, "\"' ", true);
        ArrayList<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean lastTokenHasBeenQuoted = false;

        while (true) {
            while (tok.hasMoreTokens()) {
                String nextTok = tok.nextToken();
                switch (state) {
                    case 1 -> {
                        if ("'".equals(nextTok)) {
                            lastTokenHasBeenQuoted = true;
                            state = 0;
                        } else {
                            current.append(nextTok);
                        }
                        continue;
                    }
                    case 2 -> {
                        if ("\"".equals(nextTok)) {
                            lastTokenHasBeenQuoted = true;
                            state = 0;
                        } else {
                            current.append(nextTok);
                        }
                        continue;
                    }
                }

                if ("'".equals(nextTok)) {
                    state = 1;
                } else if ("\"".equals(nextTok)) {
                    state = 2;
                } else if (" ".equals(nextTok)) {
                    if (lastTokenHasBeenQuoted || current.length() > 0) {
                        result.add(current.toString());
                        current.setLength(0);
                    }
                } else {
                    current.append(nextTok);
                }

                lastTokenHasBeenQuoted = false;
            }

            if (lastTokenHasBeenQuoted || current.length() > 0) {
                result.add(current.toString());
            }

            if (state != 1 && state != 2) {
                return result;
            }

            throw new IllegalArgumentException();
        }
    }
}
