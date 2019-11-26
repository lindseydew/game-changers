import models.Model;
import models.Sql2oModel;
import org.apache.log4j.BasicConfigurator;
import org.flywaydb.core.Flyway;
import org.sql2o.Sql2o;
import org.sql2o.converters.UUIDConverter;
import org.sql2o.quirks.PostgresQuirks;
import spark.ModelAndView;

import java.util.HashMap;
import java.util.UUID;

import static spark.Spark.*;
import static spark.Spark.post;

public class Main {

    public static void main(String[] args) {
        BasicConfigurator.configure();
        staticFileLocation("/templates");

        Flyway flyway = Flyway.configure().dataSource("jdbc:postgresql://localhost:5432/makersandmortals", null, null).load();
        flyway.migrate();

        Sql2o sql2o = new Sql2o("jdbc:postgresql://localhost:5432/" + "makersandmortals", null, null, new PostgresQuirks() {
            {
                // make sure we use default UUID converter.
                converters.put(UUID.class, new UUIDConverter());
            }
        });

        Model model = new Sql2oModel(sql2o);


        get("/", (req, res) -> "Hello World");


        get("/home", (req, res) -> {
            String username = req.session().attribute("user");
            String signedIn = req.session().attribute("Signed_In?");
            if(signedIn == "true"){
                req.session().attribute("user", username);
            }else{
                req.session().attribute("user", "Prepare for carnage");
                username = req.session().attribute("user");
            }
            HashMap players = new HashMap();
            players.put("username", username);

            return new ModelAndView(players, "templates/home.vtl");
        }, new VelocityTemplateEngine());

        post("/sign_out", (req, res) ->{
            req.session().attribute("Signed_In?", "false");
            res.redirect("/home");
            return null;
        });

        get("/sign_up", (req, res) -> {
            HashMap signup = new HashMap();
            return new ModelAndView(signup, "templates/sign_up.vtl");
        }, new VelocityTemplateEngine());

        get("/signed_up", (req, res) -> {
            HashMap signed = new HashMap();
            return new ModelAndView(signed, "templates/signed_up.vtl");
        }, new VelocityTemplateEngine());

        post("/signed", (req, res) -> {
            res.redirect("/sign_in");
            return null;
        });

        post("/signed_up", (req, res) -> {
            String username = req.queryParams("username");
            String fullname = req.queryParams("full_name");
            String password = req.queryParams("password");
            UUID playerUuid = UUID.randomUUID();
            model.createPlayer(playerUuid.toString(), username, fullname, password, 0);
            res.redirect("/signed_up");
            return null;
        });

        get("/sign_in", (req, res) -> {
            HashMap signin = new HashMap();
            return new ModelAndView(signin, "templates/sign_in.vtl");
        }, new VelocityTemplateEngine());

        post("/signed_in", (req, res) -> {
            String username = req.queryParams("username");
            String password = req.queryParams("password");

            if(model.CorrectPassword(username, password)){
                res.redirect("/signed_in");
            } else {
                res.redirect("/sign_in");
            };

            req.session().attribute("user",username);
            req.session().attribute("Signed_In?","true");

            return null;
        });

        get("/signed_in", (req, res) -> {
            HashMap signedin = new HashMap();
            return new ModelAndView(signedin, "templates/signed_in.vtl");
        }, new VelocityTemplateEngine());

        post("/sign_in", (req, res) -> {
            res.redirect("/sign_in");
            return null;
        });

        post("/sign_up", (req, res) -> {
            res.redirect("/sign_up");
            return null;
        });

        post("/home", (req, res) -> {
            res.redirect("/home");
            return null;
        });


    }
}
