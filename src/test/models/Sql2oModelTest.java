package models;

import org.apache.log4j.BasicConfigurator;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.converters.UUIDConverter;
import org.sql2o.quirks.PostgresQuirks;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class Sql2oModelTest {

    Sql2o sql2o = new Sql2o("jdbc:postgresql://localhost:5432/" + "makersandmortalstest",
            null, null, new PostgresQuirks() {
        {
            // make sure we use default UUID converter.
            converters.put(UUID.class, new UUIDConverter());
        }
    });

    UUID id = UUID.fromString("49921d6e-e210-4f68-ad7a-afac266278cb");

    @BeforeAll
    static void setUpClass() {
        BasicConfigurator.configure();
        Flyway flyway = Flyway.configure().dataSource("jdbc:postgresql://localhost:5432/makersandmortalstest", null, null).load();
        flyway.migrate();

    }
    @BeforeEach
    void setUp() {
        Connection conn = sql2o.beginTransaction();
        conn.createQuery("insert into players(user_id, user_name, name, password, high_score) VALUES (:user_id, :username, :full_name, :password, :high_score)")
                .addParameter("user_id", id)
                .addParameter("username", "example username")
                .addParameter("full_name", "example full name")
                .addParameter("password", "example password")
                .addParameter("high_score", 0)
                .executeUpdate();
        conn.commit();

    }

    @AfterEach
    void tearDown() {
        Connection conn = sql2o.beginTransaction();
        conn.createQuery("TRUNCATE TABLE players")
                .executeUpdate();
        conn.commit();
    }
    @Test
    void createPlayer() {
        Connection conn = sql2o.open();
        Model model = new Sql2oModel(sql2o);
        boolean result = false;
        List<Players> list_of_players;
        list_of_players = (conn.createQuery("select * from players").executeAndFetch(Players.class));
        String test = "Players(user_ID=49921d6e-e210-4f68-ad7a-afac266278cb, user_name=example username, name=example full name, password=example password, high_score=0)";

        if(list_of_players.toString().contains(test)){
            result = true;
        } else {
            result = false;
        }
        assertTrue(result);
    }

    @Test
    void UsernameExist() {
        Model model = new Sql2oModel(sql2o);
        assertTrue(model.UsernameExist("example username"));
    }

    @Test
    void CorrectPassword(){
        Model model = new Sql2oModel(sql2o);
        assertTrue(model.CorrectPassword("example username","example password"));
    }

    @Test
    void CreatePlayer() {
        Player player = new Player("AdamR",100,10,20,"true");
        assertEquals("AdamR", player.username);
        assertEquals(100, player.health);
        assertEquals(10, player.damage);
        assertEquals(20, player.defence);
        assertEquals("true", player.is_alive);
    }


}