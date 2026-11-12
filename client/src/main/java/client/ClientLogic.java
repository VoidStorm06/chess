package client;

//import chess.ChessGame;

import model.AuthData;
import model.LoginRequest;
import model.LogoutRequest;
import model.UserData;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class ClientLogic {
    private String user = null;
    private final ServerFacade serverFacade;
    boolean loggedIn = false;
    private String authToken;

    public ClientLogic(String url) {
        serverFacade = new ServerFacade(url);
    }

    public void run() {
        System.out.println("Welcome to Chess! Type 'help' to get started");
        Scanner scanner = new Scanner(System.in);
        var result = "";
        printGame();
        while (!result.equals("quit")) {
            String user;
            if (loggedIn) {
                user = "Logged_In";
            } else {
                user = "Logged_Out";
            }
            System.out.print( user + " >>> ");
            String line = scanner.nextLine();
            try {
                result = evaluate(line);
                System.out.println(result);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
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
                    default -> throw new SecurityException("Cannot perform action");
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
                default -> throw new SecurityException("Cannot perform action");
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
        try {
            if (inputs.length != 3) {
                throw new IllegalArgumentException("Error: incorrect number of arguments");
            }
            AuthData authData = serverFacade.loginUser(new LoginRequest(inputs[1], inputs[2]));
            user = authData.username();
            authToken = authData.authToken();
            loggedIn = true;
            return "logged in as " + inputs[1];
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    private String register(String[] inputs) {
        try {
            if (inputs.length != 4) {
                throw new IllegalArgumentException("Error: incorrect number of arguments");
            }
            AuthData authData = serverFacade.registerUser(new UserData(inputs[1], inputs[2], inputs[3]));
            user = authData.username();
            authToken = authData.authToken();
            loggedIn = true;
            return "registered as " + inputs[1];
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    private String logout() {
        try {
            serverFacade.logoutUser(new LogoutRequest(authToken));
            loggedIn = false;
            authToken = null;
            user = null;
            return "logged out";
        } catch (Exception ex) {
            return "Error: failed to log out";
        }
    }

    private String create(String[] inputs) {
        try {
            if (inputs.length != 2) {
                throw new IllegalArgumentException("Error: incorrect number of arguments");
            }
        } catch (IllegalArgumentException ex) {
            return ex.getMessage();
        }
        return "create";
    }

    private String list() {
        try {
            return "list";
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    private String play(String[] inputs) {
        try {
            if (inputs.length != 3) {
                throw new IllegalArgumentException("Error: incorrect number of arguments");
            }
            if (!inputs[2].equals("white") && !inputs[2].equals("black")) {
                throw new IllegalArgumentException("Error: must be join game as WHITE or BLACK player");
            }
            return "play";
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    private String observe(String[] inputs) {
        try {
            if (inputs.length != 2) {
                throw new IllegalArgumentException("Error: incorrect number of arguments");
            }
            return "observe";
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    private void printGame() {
        System.out.println("   A  B  C  D  E  F  G  H  ");
        for (int i = 0; i < 8; i++) {
            System.out.print(String.valueOf(i+1) + " ");
            for (int j = 0; j < 8; j++) {
                if (i%2 == 0) {
                    if (j%2 == 0) {
                        System.out.print(SET_TEXT_COLOR_WHITE);
                        System.out.print(SET_BG_COLOR_DARK_GREY);
                    } else {
                        System.out.print(SET_BG_COLOR_WHITE);
                        System.out.print(SET_TEXT_COLOR_DARK_GREY);
                    }
                } else {
                    if (j%2 == 0) {
                        System.out.print(SET_BG_COLOR_WHITE);
                        System.out.print(SET_TEXT_COLOR_DARK_GREY);
                    } else {
                        System.out.print(SET_TEXT_COLOR_WHITE);
                        System.out.print(SET_BG_COLOR_DARK_GREY);
                    }
                }
                System.out.print(" P ");
            }
            System.out.print(RESET_TEXT_COLOR);
            System.out.print(RESET_BG_COLOR);
            System.out.println(" " + String.valueOf(i+1));
        }
        System.out.println("   A  B  C  D  E  F  G  H  ");
    }
}
