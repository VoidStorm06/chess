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
        if (getMessage().equals("bad request")) {
            return 400;
        } else if (getMessage().equals("unauthorized")) {
            return 401;
        } else if (getMessage().equals("Already taken")) {
            return 403;
        } else {
            return 500;
        }
    }
}
