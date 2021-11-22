import commandHandler.Command;
import commandHandler.Commander;
import org.json.simple.parser.ParseException;
import parsing.ParserWorker;
import parsing.websitesParsers.internetShopsParser.ShopReviewParser;
import parsing.websitesParsers.internetShopsParser.ShopReviewParserWorker;
import parsing.websitesParsers.internetShopsParser.ShopReviewSettings;
import parsing.websitesParsers.leroyMerlinParser.LeroyMerlinParser;
import parsing.websitesParsers.leroyMerlinParser.LeroyMerlinParserWorker;
import parsing.websitesParsers.leroyMerlinParser.LeroyMerlinSettings;
import parsing.websitesParsers.newslerParser.NewslerParser;
import parsing.websitesParsers.newslerParser.NewslerParserWorker;
import parsing.websitesParsers.newslerParser.NewslerSettings;
import parsing.websitesParsers.theGuardianParser.GuardianParser;
import parsing.websitesParsers.theGuardianParser.GuardianParserWorker;
import parsing.websitesParsers.theGuardianParser.GuardianSettings;
import parsing.websitesParsers.yandexImagesParser.ImagesParser;
import parsing.websitesParsers.yandexImagesParser.ImagesParserSettings;
import parsing.websitesParsers.yandexImagesParser.ImagesParserWorker;

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
                                         int reviewsPagesEnd, int reviewStart, int reviewEnd) throws IOException, ParseException, java.text.ParseException {
        ShopReviewParserWorker parser = new ShopReviewParserWorker(new ShopReviewParser(),
                new ShopReviewSettings(start, end, startShop, endShop, reviewsPagesStart, reviewsPagesEnd, reviewStart, reviewEnd));
        parse(parser);
    }

    public static void parseImages(String searchQuery, int start, int end, int startImage, int endImage,
                                   String savePath) throws IOException, ParseException, java.text.ParseException {
        ImagesParserWorker parser = new ImagesParserWorker(new ImagesParser(),
                new ImagesParserSettings(start, end, startImage, endImage, searchQuery), savePath);
        parse(parser);
    }

    public static void parseLeroy(String query, int start, int end, int startShop, int endShop, int reviewsPagesStart,
                                         int reviewsPagesEnd, int reviewStart, int reviewEnd) throws IOException, ParseException, java.text.ParseException {
        LeroyMerlinParserWorker parser = new LeroyMerlinParserWorker(new LeroyMerlinParser(),
                new LeroyMerlinSettings(start, end, startShop, endShop, reviewsPagesStart, reviewsPagesEnd,
                        reviewStart, reviewEnd, query));
        parse(parser);
    }

    public static void parseNewsler(int start, int end, int startNews, int endNews, String savePath) throws IOException, ParseException, java.text.ParseException {
        NewslerParserWorker parser = new NewslerParserWorker(new NewslerParser(),
                new NewslerSettings(start, end, startNews, endNews), savePath);
        parse(parser);
    }

    public static void parseGuardian(int start, int end, int startNews, int endNews, String savePath) throws IOException, ParseException, java.text.ParseException {
        GuardianParserWorker parser = new GuardianParserWorker(new GuardianParser(),
                new GuardianSettings(start, end, startNews, endNews), savePath);
        parse(parser);
    }

    public static <T>void parse(ParserWorker<T> parser) throws IOException, ParseException, java.text.ParseException {
        parser.onCompleted.addOnActionHandler(parser::onCompleted);
        parser.onNewData.addOnActionHandler(parser::onNewData);
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

        commander.addCommand(new Command("pn",
                "parse newsler.ru",
                new ArrayList<>(Arrays.asList("<No. start page>", "<No. end page>",
                        "<No. start news>", "<No. end news>", "<Images save path>")),
                commandArgs -> parseNewsler(Integer.parseInt(commandArgs.get(0)), Integer.parseInt(commandArgs.get(1)),
                        Integer.parseInt(commandArgs.get(2)), Integer.parseInt(commandArgs.get(3)), commandArgs.get(4))));

        commander.addCommand(new Command("pg",
                "parse theguardian.com",
                new ArrayList<>(Arrays.asList("<No. start page>", "<No. end page>",
                        "<No. start news>", "<No. end news>", "<Images save path>")),
                commandArgs -> parseGuardian(Integer.parseInt(commandArgs.get(0)), Integer.parseInt(commandArgs.get(1)),
                        Integer.parseInt(commandArgs.get(2)), Integer.parseInt(commandArgs.get(3)), commandArgs.get(4))));

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Введите команду: ");
            try {
                commander.executeCommand(scanner.nextLine());
            } catch (Exception exc) {
                System.out.println("Ошибка: " + exc.getMessage());
            }
        }
    }
}
