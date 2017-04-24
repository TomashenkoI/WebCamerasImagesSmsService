import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jscience.physics.amount.Amount;
import org.jscience.physics.model.RelativisticModel;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.template.freemarker.FreeMarkerEngine;

import javax.measure.quantity.Mass;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static javax.measure.unit.SI.KILOGRAM;
import static spark.Spark.*;

public class Main {

  public static void main(String[] args) {

    port(Integer.valueOf(System.getenv("PORT")));
    staticFileLocation("/public");
    HikariConfig config = new  HikariConfig();
    config.setJdbcUrl(System.getenv("JDBC_DATABASE_URL"));
    final HikariDataSource dataSource = (config.getJdbcUrl() != null) ?
            new HikariDataSource(config) : new HikariDataSource();

    get("/mms", (req, res) -> {
      LinkedList<String> list = new LinkedList<>();

      try(Connection connection = dataSource.getConnection()) {
        ResultSet rs = connection.createStatement().executeQuery("SELECT tick FROM ticks");
        while (rs.next()) {
          list.add(rs.getTimestamp("tick").toString());
        }
      } catch (Exception e) {
        return e;
      }
      new MmsSender().send("", list.getLast());
      return "sent" + list.getLast();
    });

    get("/test", (req, res) -> {
      LinkedList<String> list = new LinkedList<>();

      try(Connection connection = dataSource.getConnection()) {
        ResultSet rs = connection.createStatement().executeQuery("SELECT tick FROM ticks ORDER BY tick DESC LIMIT 1");
        return "sent" + rs.getTimestamp("tick").toString();
      }
    });

    get("/hello", (req, res) -> {
      RelativisticModel.select();
      String energy = System.getenv().get("ENERGY");

      Amount<Mass> m = Amount.valueOf(energy).to(KILOGRAM);
      return "sent";
    });

    post("/save", new Route() {
      @Override
      public Object handle(Request request, Response response) throws Exception {
        new MmsSender().respondMessage(response.raw());
        return response;
      }
    });

    get("/", (request, response) -> {
      Map<String, Object> attributes = new HashMap<>();
      attributes.put("message", "Hello World!");

      return new ModelAndView(attributes, "index.ftl");
    }, new FreeMarkerEngine());


    get("/db", (req, res) -> {
      Map<String, Object> attributes = new HashMap<>();
      try(Connection connection = dataSource.getConnection()) {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
        stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
        ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");

        ArrayList<String> output = new ArrayList<String>();
        while (rs.next()) {
          output.add( "Read from DB: " + rs.getTimestamp("tick"));
        }

        attributes.put("results", output);
        return new ModelAndView(attributes, "db.ftl");
      } catch (Exception e) {
        attributes.put("message", "There was an error: " + e);
        return new ModelAndView(attributes, "error.ftl");
      }
    }, new FreeMarkerEngine());

  }

}
