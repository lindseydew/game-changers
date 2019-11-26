package models;

import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.List;
import java.util.UUID;

public class Sql2oModel implements Model {

    private Sql2o sql2o;

    public Sql2oModel(Sql2o sql2o) {
        this.sql2o = sql2o;

    }


    @Override
    public boolean UsernameExist(String username) {
        boolean does_username_exists = false;
        try (Connection conn = sql2o.open()) {
            List<Users> user1 = conn.createQuery("select username from users")
                    .executeAndFetch(Users.class);
            String user = user1.toString();
            if (user.contains(username)) {
                does_username_exists = true;
            }
        }
        return does_username_exists;
    }

    @Override
    public boolean CorrectPassword(String username, String password) {
        boolean correct_password = false;

        try (Connection conn = sql2o.open()) {
            List<Users> user = conn.createQuery("select password from users where username=:username")
                    .addParameter("username", username)
                    .executeAndFetch(Users.class);
            password = "[Users(username=null, full_name=null, password=" + password + ")]";
            if (user.toString().equals(password)) {
                correct_password = true;
            }
        }
        return correct_password;
    }

    @Override
    public void createUser(String username, String full_name, String password) {
        try (Connection conn = sql2o.beginTransaction()) {
            conn.createQuery("insert into users(username, full_name, password) VALUES (:username, :full_name, :password)")
                    .addParameter("username", username)
                    .addParameter("full_name", full_name)
                    .addParameter("password", password)
                    .executeUpdate();
            conn.commit();
        }
    }
}