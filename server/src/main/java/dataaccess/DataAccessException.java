package dataaccess;

import com.google.gson.Gson;

import java.util.Map;

/**
 * Indicates there was an error connecting to the database
 */
public class DataAccessException extends Exception{
    public DataAccessException(String message) {
        super(message);
    }

    public DataAccessException(String message, Throwable ex) {
        super(message, ex);
    }

    public String toJson() {
        return new Gson().toJson(Map.of("message", "Error: " + getMessage()));
    }

    public int messageToCode() {
        return switch (getMessage()) {
            case "bad request" -> 400;
            case "unauthorized" -> 401;
            case "Already taken" -> 403;
            default -> 500;
        };
    }
}
