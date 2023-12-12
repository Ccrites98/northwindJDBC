package com.pluralsight;
import java.sql.*;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        String url = "jdbc:mysql://127.0.0.1:3306/northwind";
        String user = "root";
        String password = "-wouldntyouliketoknowweatherboy-";
        String query = "SELECT ProductID, ProductName, UnitPrice, UnitsInStock FROM Products";
        while (true) {
            System.out.println("What do you want to do?");
            System.out.println("1) Display all products");
            System.out.println("2) Display all customers");
            System.out.println("0) Exit");
            System.out.print("Select an option: ");
            int choice = keyboard.nextInt();
            keyboard.nextLine();
            switch (choice) {
                case 1:
                    displayAllProducts(url, user, password);
                    break;
                case 2:
                    displayAllCustomers(url, user, password);
                    break;
                case 0:
                    System.out.println("Exiting the program. Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid option. Please choose a valid option.");
            }
        }
    }
    private static void displayAllProducts(String url, String user, String password) {
        String query = "SELECT ProductID, ProductName, UnitPrice, UnitsInStock FROM Products";
        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                int productID = rs.getInt("ProductID");
                String productName = rs.getString("ProductName");
                double unitPrice = rs.getDouble("UnitPrice");
                int unitsInStock = rs.getInt("UnitsInStock");
                //Now to stack the information
                System.out.println("Id: " + productID);
                System.out.println("Name: " + productName);
                System.out.println("Price: " + unitPrice);
                System.out.println("Stock: " + unitsInStock);
                System.out.println("------------------");
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static void displayAllCustomers(String url, String user, String password) {
        Scanner keyboard2 = new Scanner(System.in);
        String query = "SELECT ContactName, CompanyName, City, Country, Phone FROM Customers ORDER BY Country";
        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                //For all the customers and contact information in the database
                String contactName = resultSet.getString("ContactName");
                String companyName = resultSet.getString("CompanyName");
                String city = resultSet.getString("City");
                String country = resultSet.getString("Country");
                String phone = resultSet.getString("Phone");
                //Stack it up
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
