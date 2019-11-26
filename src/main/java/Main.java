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


            HashMap posts = new HashMap();


            return new ModelAndView(posts, "templates/home.vtl");
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

        get("/signed", (req, res) -> {
            HashMap signed = new HashMap();
            return new ModelAndView(signed, "templates/signed.vtl");
        }, new VelocityTemplateEngine());

        post("/signed", (req, res) -> {
            res.redirect("/sign_in");
            return null;
        });

        post("/signed_up", (req, res) -> {
            String username = req.queryParams("username");
            String fullname = req.queryParams("full_name");
            String password = req.queryParams("password");
//            model.createUser(username, fullname, password);
            res.redirect("/signed");
            return null;
        });

        get("/sign_in", (req, res) -> {
            HashMap signin = new HashMap();
            return new ModelAndView(signin, "templates/signin.vtl");
        }, new VelocityTemplateEngine());

        post("/signed_in", (req, res) -> {
            String username = req.queryParams("username");
            String password = req.queryParams("password");

//            if(model.CorrectPassword(username, password)){
//                res.redirect("/signedin");
//            } else {
//                res.redirect("/signin");
//            };

            req.session().attribute("user",username);
            req.session().attribute("Signed_In?","true");

            return null;
        });

        get("/signedin", (req, res) -> {
            HashMap signedin = new HashMap();
            return new ModelAndView(signedin, "templates/signedin.vtl");
        }, new VelocityTemplateEngine());

        post("/signin", (req, res) -> {
            res.redirect("/signin");
            return null;
        });
        post("/signup", (req, res) -> {
            res.redirect("/signup");
            return null;
        });

    }
}
