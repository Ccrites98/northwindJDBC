package com.pluralsight;
import org.apache.commons.dbcp2.BasicDataSource;
import javax.sql.DataSource;
import java.sql.*;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        DataSource dataSource = setupDataSource();
        try (Scanner keyboard = new Scanner(System.in)) {
            while (true) {
                System.out.println("What do you want to do?");
                System.out.println("1) Display all products");
                System.out.println("2) Display all customers");
                System.out.println("3) Display all categories and products in a category");
                System.out.println("0) Exit");
                System.out.print("Select an option: ");
                int choice = keyboard.nextInt();
                keyboard.nextLine();

                switch (choice) {
                    case 1:
                        displayAllProducts(dataSource);
                        break;
                    case 2:
                        displayAllCustomers(dataSource);
                        break;
                    case 3:
                        displayAllCategoriesAndProducts(dataSource, keyboard);
                        break;
                    case 0:
                        System.out.println("Exiting the program. Goodbye!");
                        System.exit(0);
                    default:
                        System.out.println("Invalid option. Please choose a valid option.");
                }
            }
        }
    }
    private static DataSource setupDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/northwind");
        dataSource.setUsername("root");
        dataSource.setPassword("-wouldntyouliketoknow-");
        dataSource.setMinIdle(5);
        dataSource.setMaxIdle(10);
        dataSource.setMaxOpenPreparedStatements(100);
        return dataSource;
    }
    private static void displayAllCategoriesAndProducts(DataSource dataSource, Scanner keyboard) {
        try (Connection connection = dataSource.getConnection()) {
            String categoryQuery = "SELECT CategoryID, CategoryName FROM Categories ORDER BY CategoryID";
            try (Statement categoryStatement = connection.createStatement();
                 ResultSet categoryResultSet = categoryStatement.executeQuery(categoryQuery)) {

                while (categoryResultSet.next()) {
                    int categoryId = categoryResultSet.getInt("CategoryID");
                    String categoryName = categoryResultSet.getString("CategoryName");

                    System.out.println("Category ID: " + categoryId + ", Category Name: " + categoryName);
                }
            }
            // put in category id
            System.out.print("Enter a Category ID to display products: ");
            int selectedCategoryId = keyboard.nextInt();
            keyboard.nextLine();

            // Display products in the selected category
            String productQuery = "SELECT ProductID, ProductName, UnitPrice, UnitsInStock FROM Products WHERE CategoryID = ?";
            try (PreparedStatement productStatement = connection.prepareStatement(productQuery)) {
                productStatement.setInt(1, selectedCategoryId);
                try (ResultSet productResultSet = productStatement.executeQuery()) {
                    while (productResultSet.next()) {
                        int productId = productResultSet.getInt("ProductID");
                        String productName = productResultSet.getString("ProductName");
                        double unitPrice = productResultSet.getDouble("UnitPrice");
                        int unitsInStock = productResultSet.getInt("UnitsInStock");

                        System.out.println("Product ID: " + productId);
                        System.out.println("Product Name: " + productName);
                        System.out.println("Unit Price: " + unitPrice);
                        System.out.println("Units In Stock: " + unitsInStock);
                        System.out.println("------------------");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static void displayAllProducts(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            String query = "SELECT ProductID, ProductName, UnitPrice, UnitsInStock FROM Products";
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    int productID = rs.getInt("ProductID");
                    String productName = rs.getString("ProductName");
                    double unitPrice = rs.getDouble("UnitPrice");
                    int unitsInStock = rs.getInt("UnitsInStock");
                    // Now to stack the information
                    System.out.println("Id: " + productID);
                    System.out.println("Name: " + productName);
                    System.out.println("Price: " + unitPrice);
                    System.out.println("Stock: " + unitsInStock);
                    System.out.println("------------------");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void displayAllCustomers(DataSource dataSource) {
        Scanner keyboard2 = new Scanner(System.in);
        String query = "SELECT ContactName, CompanyName, City, Country, Phone FROM Customers ORDER BY Country";
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                // For all the customers and contact information in the database
                String contactName = resultSet.getString("ContactName");
                String companyName = resultSet.getString("CompanyName");
                String city = resultSet.getString("City");
                String country = resultSet.getString("Country");
                String phone = resultSet.getString("Phone");
                // Stack it up
                System.out.println("Contact Name: " + contactName);
                System.out.println("Company Name: " + companyName);
                System.out.println("City: " + city);
                System.out.println("Country: " + country);
                System.out.println("Phone: " + phone);
                System.out.println("------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            keyboard2.close();
        }
    }
}
