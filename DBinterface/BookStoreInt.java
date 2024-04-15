package DBinterface;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import DBs.*;

public class BookStoreInt {
    public Connection currSession;
    public Scanner input;

    public BookStoreInt(Connection con, Scanner input){
        this.currSession = con;
        this.input = input;
        menu();
    }

    public void printMenu(){
        System.out.println("\n<This is the bookstore interface.>");
        System.out.println("---------------------------------------");
        System.out.println("1. Order Update.");
        System.out.println("2. Order Query.");
        System.out.println("3. N most Popular Book Query.");
        System.out.println("4. Back to main menu.\n");
        System.out.printf("What is your choice??..");
    }

    public boolean checkOrder(String oID){
        // Return true if the order exist in the database
        try{
            String query = "SELECT COUNT(*) FROM orders WHERE oID = ?";
            PreparedStatement pstmt = currSession.prepareStatement(query);
            pstmt.setString(1, oID);
            ResultSet rs = pstmt.executeQuery();
            int count = 0;
            if (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
            if (count < 1){ 
                System.out.println("Order not found!");
            }
            return count > 0; 
        }
        catch(SQLException e){
            System.out.println("Order not found!");
        }
        return false;
    }

    public boolean checkOrderShipped(String oID){
        // Return true of the status of the order is "Y"
        char status = 'N';
        try{
            String query = "SELECT status FROM orders WHERE oID = ?";
            PreparedStatement pstmt = currSession.prepareStatement(query);
            pstmt.setString(1, oID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                status = rs.getString("status").charAt(0);
            } else {
                System.out.println("No order found with ID: " + oID);
                return false;
            }
            rs.close();
        } catch(SQLException e){
            System.out.println("Invalid Order ID for update.");
            return false;
        }
        if(status == 'Y'){
            return true;
        }
        else {
            System.out.println("The order has been shipped. Please select the other order");
            return false;
        }

    }

    public int getOrderQuantity(String oID){
        // Return the total quantity of the order
        // Remember to sum all the quantity from the bookOrdered,
        // as the oID in bookOrdered might have multiple records
        int orderQuantity = 0;
        try {
            String query = "SELECT SUM(quantity) as total FROM bookOrdered WHERE oID = ?";
            PreparedStatement pstmt = currSession.prepareStatement(query);
            pstmt.setString(1, oID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                orderQuantity = rs.getInt("total");
            } else {
                System.out.println("No records found for order ID: " + oID);
            }
            rs.close();
        } catch(SQLException e) {
            e.printStackTrace();
            orderQuantity = -1;
        }
        return orderQuantity;
    }

    public void updateOrderStatus(String oID){
        // Update the ship status to the order
        try{
            String query = "UPDATE orders SET status = 'Y' WHERE oID = ? AND status = 'N'";
            PreparedStatement pstmt = currSession.prepareStatement(query);
            pstmt.setString(1, oID);
            int rowsAffected = pstmt.executeUpdate();
            // Checking if the update was successful
            if (rowsAffected > 0) {
                System.out.println("The shipping status of order " + oID + " has been successfully updated to 'Y'.");
            } else {
                System.out.println("No updates were made. The order might not exist or it has already been shipped.");
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void orderUpdate(String oID, Scanner input){
        // Check if the order exists
        if (!checkOrder(oID)) {
            System.out.println("No order found with ID: " + oID);
            return;
        }

        // Check if the order has been shipped already
        if (checkOrderShipped(oID)) {
            System.out.println("The order has already been shipped and cannot be updated.");
            return;
        }

        // Get the total quantity of books in the order
        int totalQuantity = getOrderQuantity(oID);
        if (totalQuantity <= 0) {
            System.out.println("The order contains no books or an error occurred while fetching the quantity.");
            return;
        }

        // Output current order status and quantity before update
        System.out.println("The Shipping status of " + oID + " is N and " + totalQuantity + " books ordered");
    

        System.out.print("Are you sure to update the shipping status? (Yes=Y) ");
        String updateStatus = input.next();
        if (updateStatus.equalsIgnoreCase("Y")) {
            // Update the shipping status to 'Y'
            updateOrderStatus(oID);
            System.out.println("Updated shipping status");
        } else {
            System.out.println("Shipping status update cancelled by the user.");
        }
    }

    public void orderQuery(String queryDate){
        // Query and sum all the "charge" from orders that have been shipped
        try {
            int totalCharge = 0;
            int counter = 0;

            String query = "SELECT oID, cID, oDate, charge FROM orders WHERE oDate LIKE ? AND status = 'Y' ORDER BY oID ASC";
            PreparedStatement pstmt = currSession.prepareStatement(query);
            pstmt.setString(1, queryDate + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int charge = rs.getInt("charge");
                totalCharge += charge; 

                counter++;
                System.out.println("\n");
                System.out.println("Record : " + counter);
                System.out.println("order_id : " + rs.getString("oID"));
                System.out.println("customer_id : " + rs.getString("cID"));
                System.out.println("date : " + rs.getDate("oDate"));
                System.out.println("charge : " + charge);
            }
            if (counter > 0) {
                System.out.println("\nTotal charges of the month is " + totalCharge);
            } else {
                System.out.println("No records found for the given month and year.");
            }
        } 
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void nMostPopular(int n){
        try{
            String query = "SELECT b.ISBN, b.bTitle, SUM(bo.quantity) AS copies " +
                           "FROM bookOrdered bo " +
                           "JOIN book b ON bo.ISBN = b.ISBN " +
                           "GROUP BY b.ISBN, b.bTitle " +
                           "ORDER BY copies DESC " +
                           "FETCH FIRST ? ROWS ONLY";          
            PreparedStatement pstmt = currSession.prepareStatement(query);
            pstmt.setInt(1, n);            
            ResultSet rs = pstmt.executeQuery();

            int count = 0;

            // Print the header
            System.out.println("ISBN        Title                       copies");
            while (rs.next()) {
                String ISBN = rs.getString("ISBN");
                String title = rs.getString("bTitle");
                int totalCopies = rs.getInt("copies");
                System.out.printf("%-12s %-25s %d\n", ISBN, title, totalCopies);
                count++;
            }
        }
        catch(SQLException sqlE){
            sqlE.printStackTrace();
        }
        catch(Exception e){
            System.out.println("Please input a valid number!");
        }
    }

    public void menu(){
        while (true) {
            printMenu();
            int choice = 0;
            boolean isInt = false;
            String oID;
            String yearMonthDate;
            String n;
            int num;
            while(!isInt){
                try{
                    choice = input.nextInt();
                    isInt = true;
                }
                catch(Exception e){
                    System.out.println("Please select the correct choice.");
                    input.next();
                    continue;
                }
            }
            switch (choice) {
                case 1:
                    System.out.print("Please input the order ID: ");
                    oID = input.next();
                    if (checkOrder(oID)) orderUpdate(oID, input); 
                    break;

                case 2:
                    System.out.print("Please input the Month for Order Query (e.g.2005-09): ");
                    yearMonthDate = input.nextLine();
                    orderQuery(yearMonthDate);
                    break;

                case 3:
                    System.out.print("Please input the N popular books number: ");
                    n = input.next();
                    try{
                        num = Integer.valueOf(n);
                        nMostPopular(num);
                    }
                    catch(Exception e){
                        System.out.print("Please input a valid number!");
                    }
                    break;

                case 4:
                    return;

                default:
                    System.out.println("Please select the correct choice.");
            }

        }
    }
}
