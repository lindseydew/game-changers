package models;


import java.sql.Date;
import java.util.List;
import java.util.UUID;

public interface Model {
    boolean UsernameExist(String username);
    boolean CorrectPassword(String username, String password);
    void createUser(String username, String full_name, String password);
}


