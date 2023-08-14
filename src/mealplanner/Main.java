package mealplanner;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main implements MealPlanMethods, DatabaseOperations {
    private static final List<String> categoriesOfMeal = new ArrayList<>();
    private static final List<String> namesOfMeal = new ArrayList<>();
    private static final List<String[]> listOfIngredients = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);
    private static final String ingredientsRegex = "^([a-zA-Z ]+)(, *[a-zA-Z ]+)*$";
    private static final String mealsRegex = "^[a-zA-Z]+( [a-zA-Z]+)*$";
    private static boolean exit = false;
    private static int mealIdCounter = 1;
    private static final List<String> categories = List.of("breakfast", "lunch", "dinner");


    public static void main(String[] args) {
        MealPlanMethods mealPlanMethods = new MealPlanMethods() {
        };
        DatabaseOperations databaseOperations = new DatabaseOperations() {
        };
        try (
                Connection connection = DatabaseConnection.getConnection()) {
            mealIdCounter = databaseOperations.getMealIdCounterFromDatabase(connection) + 1;

            while (!exit) {
                System.out.println("What would you like to do (add, show, plan, save, exit)?");
                String choose = scanner.nextLine();
                if (choose.equals("exit") || choose.equals("add") || choose.equals("show") || choose.equals("plan") || choose.equals("save")) {
                    if (choose.equals("exit")) {
                        System.out.println("Bye!");
                        exit = true;
                    } else if (choose.equals("add")) {

                        System.out.println("Which meal do you want to add (breakfast, lunch, dinner)?");
                        String categoryOfMeal = scanner.nextLine();
                        if (categoryOfMeal.equals("breakfast") || categoryOfMeal.equals("lunch") || categoryOfMeal.equals("dinner")) {
                            categoriesOfMeal.add(categoryOfMeal);

                            System.out.println("Input the meal's name:");
                            String nameOfMeal = scanner.nextLine();
                            boolean rightFormatMeals = nameOfMeal.matches(mealsRegex);
                            if (rightFormatMeals) {
                                namesOfMeal.add(nameOfMeal);
                            }
                            while (!rightFormatMeals || !nameOfMeal.matches(mealsRegex)) {
                                System.out.println("Wrong format. Use letters only!");
                                nameOfMeal = scanner.nextLine();
                                if (nameOfMeal.matches(mealsRegex)) {
                                    namesOfMeal.add(nameOfMeal);
                                }
                                rightFormatMeals = true;
                            }


                            System.out.println("Input the ingredients:");
                            String inputIngredients = scanner.nextLine();
                            boolean rightFormatIngr = inputIngredients.matches(ingredientsRegex);

                            if (rightFormatIngr) {
                                String[] ingredients = inputIngredients.split(",");
                                listOfIngredients.add(ingredients);
                                databaseOperations.addMealToDatabase(connection, categoryOfMeal, nameOfMeal, mealIdCounter);
                                databaseOperations.addIngredientsToDatabase(connection, mealIdCounter, ingredients);
                                mealIdCounter++;
                            }

                            while (!rightFormatIngr || !inputIngredients.matches(ingredientsRegex)) {
                                System.out.println("Wrong format. Use letters only!");
                                inputIngredients = scanner.nextLine();
                                if (inputIngredients.matches(ingredientsRegex)) {
                                    String[] ingredients = inputIngredients.split(",");
                                    listOfIngredients.add(ingredients);
                                    rightFormatIngr = true;
                                    databaseOperations.addMealToDatabase(connection, categoryOfMeal, nameOfMeal, mealIdCounter);
                                    databaseOperations.addIngredientsToDatabase(connection, mealIdCounter, ingredients);
                                    mealIdCounter++;
                                }
                            }

                            if (rightFormatIngr && rightFormatMeals) {

                                System.out.println("The meal has been added!");
                            }
                        } else {

                            boolean rightCategory = false;
                            while (!rightCategory) {
                                System.out.println("Wrong meal category! Choose from: breakfast, lunch, dinner.");
                                categoryOfMeal = scanner.nextLine();
                                if (categoryOfMeal.equals("breakfast") || categoryOfMeal.equals("lunch") || categoryOfMeal.equals("dinner")) {
                                    categoriesOfMeal.add(categoryOfMeal);

                                    System.out.println("Input the meal's name:");
                                    String nameOfMeal = scanner.nextLine();
                                    boolean rightFormatMeals = nameOfMeal.matches(mealsRegex);
                                    if (rightFormatMeals) {
                                        namesOfMeal.add(nameOfMeal);

                                    }
                                    while (!rightFormatMeals || !nameOfMeal.matches(mealsRegex)) {
                                        System.out.println("Wrong format. Use letters only!");
                                        nameOfMeal = scanner.nextLine();
                                        if (nameOfMeal.matches(mealsRegex)) {
                                            namesOfMeal.add(nameOfMeal);
                                        }
                                        rightFormatMeals = true;
                                    }


                                    System.out.println("Input the ingredients:");
                                    String inputIngredients = scanner.nextLine();
                                    boolean rightFormatIngr = inputIngredients.matches(ingredientsRegex);
                                    if (rightFormatIngr) {
                                        String[] ingredients = inputIngredients.split(",");
                                        listOfIngredients.add(ingredients);
                                        databaseOperations.addMealToDatabase(connection, categoryOfMeal, nameOfMeal, mealIdCounter);
                                        databaseOperations.addIngredientsToDatabase(connection, mealIdCounter, ingredients);
                                        mealIdCounter++;
                                    }

                                    while (!rightFormatIngr && !inputIngredients.matches(ingredientsRegex)) {
                                        System.out.println("Wrong format. Use letters only!");
                                        inputIngredients = scanner.nextLine();
                                        if (inputIngredients.matches(ingredientsRegex)) {
                                            String[] ingredients = inputIngredients.split(",");
                                            listOfIngredients.add(ingredients);
                                            rightFormatIngr = true;
                                            databaseOperations.addMealToDatabase(connection, categoryOfMeal, nameOfMeal, mealIdCounter);
                                            databaseOperations.addIngredientsToDatabase(connection, mealIdCounter, ingredients);
                                            mealIdCounter++;
                                        }
                                    }

                                    if (rightFormatIngr && rightFormatMeals) {
                                        System.out.println("The meal has been added!");

                                    }
                                    rightCategory = true;
                                }
                            }
                        }
                    } else if (choose.equals("show")) {
                        if (databaseOperations.isIngredientsTableEmpty(connection) && databaseOperations.isMealsTableEmpty(connection)) {
                            System.out.println("No meals saved. Add a meal first.");
                        } else {
                            System.out.println("Which category do you want to print (breakfast, lunch, dinner)?");
                            String categoryOfMeal = scanner.nextLine();
                            boolean rightMealCategory = categoryOfMeal.equals("lunch") || categoryOfMeal.equals("dinner") || categoryOfMeal.equals("breakfast");
                            while (!rightMealCategory) {
                                System.out.println("Wrong meal category! Choose from: breakfast, lunch, dinner.");
                                categoryOfMeal = scanner.nextLine();
                                if (categoryOfMeal.equals("lunch") || categoryOfMeal.equals("dinner") || categoryOfMeal.equals("breakfast")) {
                                    rightMealCategory = true;
                                }
                            }
                            if (categoryOfMeal.equals("lunch")) {
                                if (databaseOperations.isCategoryEmpty(connection, "lunch")) {
                                    System.out.println("No meals found.");
                                }
                                System.out.println("Category: lunch");
                                for (int i = 1; i < mealIdCounter; i++) {
                                    String sqlMeals = "SELECT category, meal, meal_id FROM meals WHERE meal_id = " + i + " AND category = 'lunch'";
                                    try (PreparedStatement preparedStatement = connection.prepareStatement(sqlMeals);
                                         ResultSet resultSet = preparedStatement.executeQuery()) {
                                        if (resultSet.next()) {
                                            String meal = resultSet.getString("meal");
                                            System.out.println("Name: " + meal);

                                            String sqlIngredients = "SELECT ingredient FROM ingredients WHERE meal_id = " + i;
                                            try (PreparedStatement preparedStatementIngredients = connection.prepareStatement(sqlIngredients);
                                                 ResultSet resultSetIngredients = preparedStatementIngredients.executeQuery()) {

                                                System.out.println("Ingredients:");
                                                while (resultSetIngredients.next()) {
                                                    String ingredient = resultSetIngredients.getString("ingredient");
                                                    System.out.println(ingredient);
                                                }
                                                System.out.println();
                                            } catch (SQLException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else if (categoryOfMeal.equals("dinner")) {
                                if (databaseOperations.isCategoryEmpty(connection, "dinner")) {
                                    System.out.println("No meals found.");
                                }
                                System.out.println("Category: dinner");
                                for (int i = 1; i < mealIdCounter; i++) {
                                    String sqlMeals = "SELECT category, meal, meal_id FROM meals WHERE meal_id = " + i + " AND category = 'dinner'";
                                    try (PreparedStatement preparedStatement = connection.prepareStatement(sqlMeals);
                                         ResultSet resultSet = preparedStatement.executeQuery()) {
                                        if (resultSet.next()) {
                                            String meal = resultSet.getString("meal");
                                            System.out.println("Name: " + meal);

                                            String sqlIngredients = "SELECT ingredient FROM ingredients WHERE meal_id = " + i;
                                            try (PreparedStatement preparedStatementIngredients = connection.prepareStatement(sqlIngredients);
                                                 ResultSet resultSetIngredients = preparedStatementIngredients.executeQuery()) {

                                                System.out.println("Ingredients:");
                                                while (resultSetIngredients.next()) {
                                                    String ingredient = resultSetIngredients.getString("ingredient");
                                                    System.out.println(ingredient);
                                                }
                                                System.out.println();
                                            } catch (SQLException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else if (categoryOfMeal.equals("breakfast")) {
                                if (databaseOperations.isCategoryEmpty(connection, "breakfast")) {
                                    System.out.println("No meals found.");
                                }
                                System.out.println("Category: breakfast");
                                for (int i = 1; i < mealIdCounter; i++) {
                                    String sqlMeals = "SELECT category, meal, meal_id FROM meals WHERE meal_id = " + i + " AND category = 'breakfast'";
                                    try (PreparedStatement preparedStatement = connection.prepareStatement(sqlMeals);
                                         ResultSet resultSet = preparedStatement.executeQuery()) {
                                        if (resultSet.next()) {
                                            String meal = resultSet.getString("meal");
                                            System.out.println("Name: " + meal);

                                            String sqlIngredients = "SELECT ingredient FROM ingredients WHERE meal_id = " + i;
                                            try (PreparedStatement preparedStatementIngredients = connection.prepareStatement(sqlIngredients);
                                                 ResultSet resultSetIngredients = preparedStatementIngredients.executeQuery()) {

                                                System.out.println("Ingredients:");
                                                while (resultSetIngredients.next()) {
                                                    String ingredient = resultSetIngredients.getString("ingredient");
                                                    System.out.println(ingredient);
                                                }
                                                System.out.println();
                                            } catch (SQLException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    } else if (choose.equals("plan")) {

                        for (DaysOfWeek day : DaysOfWeek.values()) {
                            System.out.println(day.getNameOfDay());
                            mealPlanMethods.deleteOldMealPlan(connection, day.getNameOfDay());
                            for (String categoryMeal : categories) {

                                String categorySql = "SELECT meal FROM meals WHERE category = ? ORDER BY meal";
                                try (PreparedStatement preparedStatement = connection.prepareStatement(categorySql)) {
                                    preparedStatement.setString(1, categoryMeal);
                                    ResultSet resultSetMeals = preparedStatement.executeQuery();

                                    while (resultSetMeals.next()) {
                                        String meal = resultSetMeals.getString(1);
                                        System.out.println(meal);
                                    }
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                System.out.println("Choose the " + categoryMeal + " for " + day.getNameOfDay() + " from the list above:");
                                String choosenMeal = scanner.nextLine();
                                boolean containsMeal = databaseOperations.isMealInDatabase(connection, categoryMeal, choosenMeal);
                                if (containsMeal) {
                                    mealPlanMethods.addMealToPlan(connection, day.getNameOfDay(), categoryMeal, choosenMeal);
                                }
                                while (!containsMeal) {
                                    System.out.println("This meal doesn’t exist. Choose a meal from the list above.");
                                    choosenMeal = scanner.nextLine();
                                    if (databaseOperations.isMealInDatabase(connection, categoryMeal, choosenMeal)) {
                                        containsMeal = true;
                                        mealPlanMethods.addMealToPlan(connection, day.getNameOfDay(), categoryMeal, choosenMeal);
                                    }
                                }
                            }
                            System.out.println("Yeah! We planned the meals for " + day.getNameOfDay() + ".");
                        }
                        System.out.println();
                        mealPlanMethods.printPlanForThisWeek(connection);
                    } else if (choose.equals("save")) {
                        if (mealPlanMethods.isMealPlanComplete(connection)) {
                            System.out.println("Input a filename:");
                            String nameOfFile = scanner.nextLine();
                            mealPlanMethods.savePlanToFile(connection, nameOfFile);
                        } else {
                            System.out.println("Unable to save. Plan your meals first.");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}