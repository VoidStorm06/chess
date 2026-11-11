package client;

import java.util.Scanner;

public class ClientLogic {
    //private final String user;
    private final ServerFacade server;
    private final boolean loggedIn = false;


    public ClientLogic(String url) {
        server = new ServerFacade(url);
    }

    public void run() {
        System.out.println("Testing");
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("end")) {
            String line = scanner.nextLine();
            result = line;
            System.out.println(line);
        }
    }
}
