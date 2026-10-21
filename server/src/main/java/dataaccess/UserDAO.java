package dataaccess;

import model.UserData;

import java.util.Collection;

public interface UserDAO {
    UserData makeUser();
    Collection<UserData> readUsers();
    UserData updateUser();
    void deleteUser();
}
