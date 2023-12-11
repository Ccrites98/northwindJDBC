package com.pluralsight;
import java.sql.*;
public class Main {
    public static void main(String[] args) {
        String url = "jdbc:mysql://127.0.0.1:3306/northwind";
        String user = "root";
        String password = "-wouldntyouliketoknowweatherboy-";
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
}