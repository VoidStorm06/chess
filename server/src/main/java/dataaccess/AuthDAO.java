package dataaccess;

import model.AuthData;

import java.util.Collection;

public interface AuthDAO {
    AuthData makeAuth();
    Collection<AuthData> readAuths();
    AuthData updateAuth();
    void deleteAuth();

}
