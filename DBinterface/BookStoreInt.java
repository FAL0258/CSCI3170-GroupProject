package DBinterface;

import java.io.*;
import java.sql.*;
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
            Statement stmt = currSession.createStatement();

            return true;
        }
        catch(SQLException sqlE){
            System.out.println("Order not found, please try again.");
            return false;
        }
    }

    public boolean checkOrderShipped(String oID){
        // Return true of the status of the order is "Y"

        System.out.println("The order has been shipped. Please select the other order");
        return true;
    }

    public int getOrderQuantity(String oID){
        // Return the total quantity of the order
        // Remember to sum all the quantity from the bookOrdered,
        // as the oID in bookOrdered might have multiple records

        return -1;
    }

    public void updateOrderStatus(String oID){
        // Update the ship status to the order

    }

    public void orderUpdate(String oID, Scanner input){
        // Show "status" from orders and "quantity" from bookOrdered

        // Check if the order has been shipped or not
        // and also the quantity of the order >= 1
        if (!checkOrderShipped(oID) && getOrderQuantity(oID) >= 1){
            // Then confirm if the user want to update the status

        }

    }

    public void orderQuery(String queryDate){
        // Query and sum all the "charge" from orders that have been shipped
        
    }

    public void nMostPopular(Scanner input){
        System.out.print("Please input the N popular books number: ");
        int num;
        try{
            num = Integer.parseInt(input.next());
            Statement stmt = currSession.createStatement();
        }
        catch(SQLException sqlE){

        }
        catch(Exception e){

        }
    }

    public void menu(){
        while (true) {
            printMenu();
            int choice = 0;
            boolean isInt = false;
            String oID;
            String yearMonthDate;
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
                    yearMonthDate = input.next();
                    orderQuery(yearMonthDate);
                    break;

                case 3:
                    nMostPopular(input);
                    break;

                case 4:
                    return;

                default:
                    System.out.println("Please select the correct choice.");
            }

        }
    }
}
