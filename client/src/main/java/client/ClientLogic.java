package client;

import chess.ChessGame;

import chess.ChessPiece;
import chess.ChessPosition;
import model.*;

import java.util.Objects;
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
                if (result.equals("quit")) {
                    System.out.println("quiting application");
                }
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
                case "quit" -> quit();
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

    private String quit() {
        logout();
        return "quit";
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
            CreateGameResponse res = serverFacade.createGame(new CreateGameRequest(authToken, inputs[1]));
            return "Game with name " + inputs[1] + " created";
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    private String list() {
        try {
            var gameList = serverFacade.listGames(authToken);
            int index = 0;
            StringBuilder response = new StringBuilder();
            for (var game : gameList) {
                String whiteUser;
                String blackUser;
                if (game.whiteUsername().isEmpty()) {
                    whiteUser = "None";
                } else {
                    whiteUser = game.whiteUsername();
                }
                if (game.blackUsername().isEmpty()) {
                    blackUser = "None";
                } else {
                    blackUser = game.blackUsername();
                }
                index++;
                String currGame = Integer.toString(index) + " Game name: " + game.gameName() +
                        ". White Player: " + whiteUser + ". Black Player: " + blackUser + ". GameID: "
                        + Integer.toString(game.gameID());
                response.append(currGame).append("\n");
            }
            return response.toString();
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    private String play(String[] inputs) {
        try {
            if (inputs.length != 3) {
                throw new IllegalArgumentException("Error: incorrect number of arguments");
            }
            try {
                var temp = Integer.parseInt(inputs[1]);
            } catch (Exception ex) {
                throw new IllegalArgumentException("Error: not a valid game ID");
            }
            if (!inputs[2].equals("white") && !inputs[2].equals("black")) {
                throw new IllegalArgumentException("Error: must be join game as WHITE or BLACK player");
            }
            serverFacade.joinGame(authToken, Integer.parseInt(inputs[1]), inputs[2]);
            if (inputs[2].equals("white")) {
                printGame(new ChessGame(), "white");
            } else {
                printGame(new ChessGame(), "black");
            }
            return "";
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    private String observe(String[] inputs) {
        try {
            if (inputs.length != 2) {
                throw new IllegalArgumentException("Error: incorrect number of arguments");
            }
            try {
                var temp = Integer.parseInt(inputs[1]);
            } catch (Exception ex) {
                throw new IllegalArgumentException("Error: not a valid Game ID");
            }
            serverFacade.joinGame(authToken, Integer.parseInt(inputs[1]), "observer");
            printGame(new ChessGame(), "white");
            return "";
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    private void printGame(ChessGame game, String playerColor) {
        String columns = "   A  B  C  D  E  F  G  H  ";
        if (playerColor.equals("black")) {
            columns = "   H  G  F  E  D  C  B  A  ";
        }
        System.out.println(columns);
        for (int i = 1; i < 9; i++) {
            if (playerColor.equals("black")) {
                System.out.print(String.valueOf(i) + " ");
            } else {
                System.out.print(String.valueOf(9-i) + " ");
            }
            for (int j = 1; j < 9; j++) {
                if (i%2 == 0) {
                    if (j%2 == 0) {
                        System.out.print(SET_BG_COLOR_WHITE);
                        System.out.print(SET_TEXT_COLOR_WHITE);
                    } else {
                        System.out.print(SET_BG_COLOR_DARK_GREY);
                        System.out.print(SET_TEXT_COLOR_DARK_GREY);
                    }
                } else {
                    if (j%2 == 0) {
                        System.out.print(SET_BG_COLOR_DARK_GREY);
                        System.out.print(SET_TEXT_COLOR_DARK_GREY);
                    } else {
                        System.out.print(SET_BG_COLOR_WHITE);
                        System.out.print(SET_TEXT_COLOR_WHITE);
                    }
                }
                ChessPiece piece;
                if (playerColor.equals("black")) {
                    piece = game.getBoard().getPiece(new ChessPosition(i, 9-j));
                } else {
                    piece = game.getBoard().getPiece(new ChessPosition(9-i, j));
                }
                if (piece != null) {
                    if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                        System.out.print(SET_TEXT_COLOR_RED);
                    } else {
                        System.out.print(SET_TEXT_COLOR_BLUE);
                    }
                    switch (piece.getPieceType()) {
                        case KING -> System.out.print(" K ");
                        case QUEEN -> System.out.print(" Q ");
                        case ROOK -> System.out.print(" R ");
                        case BISHOP -> System.out.print(" B ");
                        case KNIGHT -> System.out.print(" N ");
                        default -> System.out.print(" P ");
                    }
                } else {
                    System.out.print(" E ");
                }
            }
            System.out.print(RESET_TEXT_COLOR);
            System.out.print(RESET_BG_COLOR);
            if (playerColor.equals("black")) {
                System.out.println(" " + String.valueOf(i));
            } else {
                System.out.println(" " + String.valueOf(9-i));
            }
        }
        System.out.println(columns);
    }
}
