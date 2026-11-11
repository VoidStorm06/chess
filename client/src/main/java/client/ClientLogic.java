package client;

import java.util.Scanner;

public class ClientLogic {
    //private final String user;
    private final ServerFacade server;
    boolean loggedIn = false;


    public ClientLogic(String url) {
        server = new ServerFacade(url);
    }

    public void run() {
        System.out.println("Welcome to Chess! Type 'help' to get started");
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            String line = scanner.nextLine();
            try {
                result = evaluate(line);
                System.out.println(result);
            } catch (Exception ex) {
                System.out.print(ex.getMessage());
            }
        }
    }

    private String evaluate(String input) {
        String[] inputs = input.toLowerCase().split(" ");
        try {
            if (!loggedIn) {
                return switch (inputs[0]) {
                    case "help" -> help();
                    case "quit" -> "quit";
                    case "login" -> login(inputs);
                    case "register" -> register(inputs);
                    default -> throw new SecurityException("Not signed in, cannot perform action");
                };
            }
            return switch (inputs[0]) {
                case "help" -> help();
                case "quit" -> "quit";
                case "logout" -> logout();
                case "create" -> create(inputs);
                case "list" -> list();
                case "play" -> play(inputs);
                case "observe" -> observe(inputs);
                default -> throw new SecurityException("Currently signed in, cannot perform action");
            };
        } catch (SecurityException ex) {
            return ex.getMessage();
        }
    }

    private String help() {
        if (!loggedIn) {
            return """
                    - help - shows available commands (depends on if signed in or not)
                    - quit - closes the program
                    - login <USERNAME> <PASSWORD> - sign in with pre-existing user
                    - register <USERNAME> <PASSWORD> <EMAIL> - make a new user
                    """;
        }
        return """
                    - help - shows available commands (depends on if signed in or not)
                    - quit - closes the program
                    - logout - signs out current user
                    - list - shows current games available to the current user
                    - create <ID> - creates a Chess game with ID as a name
                    - play <ID> [WHITE|BLACK] - joins Chess game with name ID as WHITE/BLACK
                    - observe <ID> - joins Chess game with name ID as an observer
                    """;
    }

    private String login(String[] inputs) {
        loggedIn = true;
        return "login";
    }

    private String register(String[] inputs) {
        loggedIn = false;
        return "register";
    }

    private String logout() {
        return "logout";
    }

    private String create(String[] inputs) {
        return "create";
    }

    private String list() {
        return "list";
    }

    private String play(String[] inputs) {
        return "play";
    }

    private String observe(String[] inputs) {
        return "observe";
    }
}
