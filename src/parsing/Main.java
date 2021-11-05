package parsing;

import commandHandler.Command;
import commandHandler.Commander;
import org.json.simple.parser.ParseException;
import parsing.imagesSearcher.ImagesParser;
import parsing.imagesSearcher.ImagesParserSettings;
import parsing.imagesSearcher.ImagesParserWorker;
import parsing.internetShopsParser.ShopReviewParser;
import parsing.internetShopsParser.ShopReviewParserWorker;
import parsing.internetShopsParser.ShopReviewSettings;
import parsing.leroyMerlinParser.LeroyMerlinParser;
import parsing.leroyMerlinParser.LeroyMerlinParserWorker;
import parsing.leroyMerlinParser.LeroyMerlinSettings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void printCommands(ArrayList<Command> commands) {
        for (Command command : commands) {
            System.out.print(command.getCommandName());

            if (!command.getArgsDescription().isEmpty()) {
                for (String arg : command.getArgsDescription())
                    System.out.print(" " + arg);
            }

            System.out.println(" - " + command.getDescription());
        }
    }

    public static void parseShopsReviews(int start, int end, int startShop, int endShop, int reviewsPagesStart,
                                         int reviewsPagesEnd, int reviewStart, int reviewEnd) throws IOException, ParseException {
        ShopReviewParserWorker parser = new ShopReviewParserWorker(new ShopReviewParser(),
                new ShopReviewSettings(start, end, startShop, endShop, reviewsPagesStart, reviewsPagesEnd, reviewStart, reviewEnd));
        parser.onCompleted.addOnActionHandler(new ShopReviewParserWorker.Completed());
        parser.onNewData.addOnActionHandler(new ShopReviewParserWorker.NewData());
        parse(parser);
    }

    public static void parseImages(String searchQuery, int start, int end, int startImage, int endImage,
                                   String savePath) throws IOException, ParseException {
        ImagesParserWorker parser = new ImagesParserWorker(new ImagesParser(),
                new ImagesParserSettings(start, end, startImage, endImage, searchQuery), savePath);
        parser.onCompleted.addOnActionHandler(new ImagesParserWorker.Completed());
        parser.onNewData.addOnActionHandler(new ImagesParserWorker.NewData());
        parse(parser);
    }

    public static void parseLeroy(String query, int start, int end, int startShop, int endShop, int reviewsPagesStart,
                                         int reviewsPagesEnd, int reviewStart, int reviewEnd) throws IOException, ParseException {
        LeroyMerlinParserWorker parser = new LeroyMerlinParserWorker(new LeroyMerlinParser(),
                new LeroyMerlinSettings(start, end, startShop, endShop, reviewsPagesStart, reviewsPagesEnd,
                        reviewStart, reviewEnd, query));
        parser.onCompleted.addOnActionHandler(new LeroyMerlinParserWorker.Completed());
        parser.onNewData.addOnActionHandler(new LeroyMerlinParserWorker.NewData());
        parse(parser);
    }

    public static <T>void parse(ParserWorker<T> parser) throws IOException, ParseException {
        parser.start();
        parser.abort();
    }


    public static void main(String[] args) {
        Commander commander = new Commander();

        commander.addCommand(new Command("quit",
                "quit program",
                commandArgs -> System.exit(0)));

        commander.addCommand(new Command("help",
                "print list of commands",
                commandArgs -> printCommands(commander.getCommands())));

        commander.addCommand(new Command("psr",
                "parse shops reviews",
                new ArrayList<>(Arrays.asList("<No. shops start page>", "<No. shops end page>", "<No. start shop>", "<No. end shop>",
                        "<No. reviews start page>", "<No. reviews end page>", "<No. start review>", "<No. end review>")),
                commandArgs -> parseShopsReviews(Integer.parseInt(commandArgs.get(0)), Integer.parseInt(commandArgs.get(1)),
                        Integer.parseInt(commandArgs.get(2)), Integer.parseInt(commandArgs.get(3)), Integer.parseInt(commandArgs.get(4)),
                        Integer.parseInt(commandArgs.get(5)), Integer.parseInt(commandArgs.get(6)), Integer.parseInt(commandArgs.get(7)))));

        commander.addCommand(new Command("pi",
                "download images by query",
                new ArrayList<>(Arrays.asList("<Search query>", "<No. start page>", "<No. end page>",
                        "<No. start image>", "<No. end image>", "<Images save path>")),
                commandArgs -> parseImages(commandArgs.get(0), Integer.parseInt(commandArgs.get(1)),
                        Integer.parseInt(commandArgs.get(2)), Integer.parseInt(commandArgs.get(3)),
                        Integer.parseInt(commandArgs.get(4)), commandArgs.get(5))));

        commander.addCommand(new Command("plm",
                "parse Leroy Merlin products",
                new ArrayList<>(Arrays.asList("Search query", "<No. products start page>", "<No. products end page>",
                        "<No. start product>", "<No. end product>", "<No. reviews start page>", "<No. reviews end page>",
                        "<No. start review>", "<No. end review>")),
                commandArgs -> parseLeroy(commandArgs.get(0), Integer.parseInt(commandArgs.get(1)), Integer.parseInt(commandArgs.get(2)),
                        Integer.parseInt(commandArgs.get(3)), Integer.parseInt(commandArgs.get(4)), Integer.parseInt(commandArgs.get(5)),
                        Integer.parseInt(commandArgs.get(6)), Integer.parseInt(commandArgs.get(7)), Integer.parseInt(commandArgs.get(8)))));

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Введите команду: ");
            try {
                commander.executeCommand(scanner.nextLine());
            } catch (Exception exc) {
                System.out.println(exc.getMessage());
            }
        }
    }
}
