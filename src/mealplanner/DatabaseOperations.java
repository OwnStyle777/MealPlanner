package mealplanner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface DatabaseOperations {
    default int getMealIdCounterFromDatabase(Connection connection) throws SQLException {
        int mealIdCounter = 0;
        String sql = "SELECT MAX(meal_id) AS max_meal_id FROM meals";
        try (PreparedStatement statement = connection.prepareStatement(sql); ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                mealIdCounter = resultSet.getInt("max_meal_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mealIdCounter;
    }

    default void addMealToDatabase(Connection connection, String category, String meal, int mealId) throws SQLException {
        String sql = "INSERT INTO meals (category, meal, meal_id) VALUES (?, ?, ?) ";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, category);
        statement.setString(2, meal);
        statement.setInt(3, mealId);
        statement.executeUpdate();

    }

    default void addIngredientsToDatabase(Connection connection, int mealId, String[] ingredients) throws SQLException {
        String sql = "INSERT INTO ingredients (ingredient, ingredient_id, meal_id) VALUES (?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        int ingredientIdCounter = 1;
        for (String ingredient : ingredients) {
            statement.setString(1, ingredient.trim());
            statement.setInt(2, ingredientIdCounter);
            statement.setInt(3, mealId);
            statement.addBatch();
            ingredientIdCounter++;
        }

        statement.executeBatch();
    }

    default boolean isMealsTableEmpty(Connection connection) throws SQLException {
        String sql = "SELECT COUNT(*) FROM meals";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count == 0;
            }
        }
        return true;
    }

    default boolean isIngredientsTableEmpty(Connection connection) throws SQLException {
        String sql = "SELECT COUNT(*) FROM ingredients";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count == 0;
            }
        }
        return true;
    }

    default boolean isCategoryEmpty(Connection connection, String category) throws SQLException {
        String sql = "SELECT COUNT(*) FROM meals WHERE category = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, category);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count == 0;
                }
            }
        }
        return true;
    }

    default boolean isMealInDatabase(Connection connection, String category, String meal) throws SQLException {
        String sql = "SELECT meal FROM meals WHERE category = ? AND meal = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, category);
            preparedStatement.setString(2, meal);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }
}
