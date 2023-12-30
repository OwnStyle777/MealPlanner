package mealplanner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection  {
    private static Connection connection;
    private static Statement statement;

    static {
        try {
            String DB_URL = "jdbc:postgresql:meals_db";
            String USER = "";
            String PASS = "";

            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            connection.setAutoCommit(true);

            statement = connection.createStatement();

            statement.execute("CREATE TABLE IF NOT EXISTS meals (" +
                    "category varchar NOT NULL," +
                    "meal varchar NOT NULL," +
                    "meal_id INTEGER NOT NULL PRIMARY KEY" +
                    ")");

            statement.execute("CREATE TABLE IF NOT EXISTS ingredients  (" +
                    "ingredient varchar NOT NULL," +
                    "ingredient_id  INTEGER NOT NULL," +
                    "meal_id INTEGER REFERENCES meals(meal_id)" +
                    ")");

            statement.execute("CREATE TABLE IF NOT EXISTS plan (" +
                    "day varchar NOT NULL," +
                    "category varchar NOT NULL," +
                    "mealName varchar NOT NULL" +
                    ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
    }

}





