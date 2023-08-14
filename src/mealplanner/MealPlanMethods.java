package mealplanner;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface MealPlanMethods {

    default void savePlanToFile(Connection connection, String nameOfFile) throws SQLException {
        Map<String, Integer> shoppingList = new HashMap<>();
        String directoryPath = "C:\\Users\\DELL\\Desktop\\";

        File fileToCreate = new File(directoryPath + nameOfFile);

        try {
            if (fileToCreate.createNewFile()) {
            } else {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Integer> mealIdes = new ArrayList<>();
        List<String> ingredientsToWrite = new ArrayList<>();
        String sqlGetId = "SELECT meal_Id FROM meals JOIN plan ON meal = mealName";
        PreparedStatement statement = connection.prepareStatement(sqlGetId);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            int mealId = resultSet.getInt(1);
            mealIdes.add(mealId);
        }

        for (Integer mealId : mealIdes) {
            String sqlSelectIngredients = "SELECT ingredient FROM ingredients WHERE meal_id = ?";
            PreparedStatement statement1 = connection.prepareStatement(sqlSelectIngredients);
            statement1.setInt(1, mealId);
            ResultSet resultSet1 = statement1.executeQuery();

            while (resultSet1.next()) {
                String ingredient = resultSet1.getString(1);
                ingredientsToWrite.add(ingredient);
            }
        }
        for (String ingredient : ingredientsToWrite) {
            shoppingList.put(ingredient, shoppingList.getOrDefault(ingredient, 0) + 1);
        }

        try (FileWriter fileWriter = new FileWriter(fileToCreate)) {
            for (Map.Entry<String, Integer> entry : shoppingList.entrySet()) {
                String ingredient = entry.getKey();
                int count = entry.getValue();
                if (count > 1) {
                    fileWriter.write(ingredient + " x" + count + "\n");
                } else {
                    fileWriter.write(ingredient + "\n");
                }
            }
            System.out.println("Saved!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    default void addMealToPlan(Connection connection, String day, String category, String mealName) throws SQLException {
        String sql = "INSERT INTO plan (day, category, mealName) VALUES (?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, day);
        statement.setString(2, category);
        statement.setString(3, mealName);
        statement.executeUpdate();
    }

    default void deleteOldMealPlan(Connection connection, String dayName) throws SQLException {
        String sql = "DELETE FROM plan WHERE day = ?";
        PreparedStatement deleteStatement = connection.prepareStatement(sql);
        deleteStatement.setString(1, dayName);
        deleteStatement.execute();
    }

    default void printPlanForThisWeek(Connection connection) throws SQLException {
        for (DaysOfWeek day : DaysOfWeek.values()) {
            System.out.println(day.getNameOfDay());
            String sql = "SELECT * FROM plan WHERE day = ? ";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, day.getNameOfDay());
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    String category = resultSet.getString("category");
                    String mealName = resultSet.getString("mealName");
                    System.out.println(category + ": " + mealName);
                }
                System.out.println();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    default boolean isMealPlanComplete(Connection connection) {
        List<String> categories = List.of("breakfast", "lunch", "dinner");
        for (DaysOfWeek day : DaysOfWeek.values()) {
            for (String category : categories) {
                String sql = "SELECT COUNT(*) FROM plan WHERE day = ? AND category = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setString(1, day.getNameOfDay());
                    preparedStatement.setString(2, category);
                    ResultSet resultSet = preparedStatement.executeQuery();

                    if (resultSet.next()) {
                        int mealCount = resultSet.getInt(1);
                        if (mealCount != 1) {
                            return false;
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }
}