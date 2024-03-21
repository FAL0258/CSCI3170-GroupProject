package DBinterface;

import java.io.*;
import java.sql.*;
import java.util.Scanner;
import DBs.*;

public class CusInt {
    public Connection currSession;
    public Scanner input;

    public CusInt(Connection con, Scanner input){
        this.currSession = con;
        this.input = input;
        menu();
    }

    public void printMenu(){
        System.out.println("<This is the customer interface.>");
        System.out.println("---------------------------------------");
        System.out.println("1. Book Search.");
        System.out.println("2. Order Creation.");
        System.out.println("3. Order Altering.");
        System.out.println("4. Order Query.");
        System.out.println("5. Back to main menu.\n");
        System.out.printf("Please enter your choice??..");
    }

    public boolean checkCustomer(String customer){
        try{
            Statement stmt = currSession.createStatement();
            String query = "SELECT * FROM customer WHERE cID = '" + customer + "'";
            ResultSet rs = stmt.executeQuery(query);
            int count = 0;
            while(rs.next()){
                count++;
            }
            if (count == 0){
                System.out.println("Customer not found!");
                return false;
            }
            return true;
        }
        catch(SQLException e){
            System.out.println("Customer not found!");
            return false;
        }
    }


    public void orderCreation(String cID, Scanner input){
        OrderCart cart = new OrderCart(currSession);
        boolean ok = false;
        String response;
        int desiredQuantity;
        System.out.println(">> What books do you want to order??");
        System.out.println(">> Input ISBN and then the quantity.");
        System.out.println(">> You can press \"L\" to see ordered list, or \"F\" to finish ordering.");
        while(!ok){
            System.out.print("Please enter the book's ISBN: ");
            response = input.next();
            switch (response) {
                case "L":
                    cart.show();
                    break;

                case "F":
                    ok = true;
                    cart.updateBackDB();
                    break;
            
                default:
                    if (cart.getBookStock(response) == -1){
                        break;
                    }
                    System.out.print("Please enter the quantity of the order: ");
                    desiredQuantity = Integer.parseInt(input.next());
                    if (desiredQuantity < 1){
                        System.out.print("You need to enter at least one quantity!");
                    }
                    else{
                        cart.add(response, desiredQuantity);
                    }
                    break;
            }
        }

    }

    public void menu(){
        while (true) {
            printMenu();
            int choice = 0;
            boolean isInt = false;
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
                    new BookSearchInt(currSession, input);
                    break;

                case 2:
                    System.out.print("Please enter your customerID??");
                    String cID = input.next();
                    if (checkCustomer(cID)) orderCreation(cID, input);
                    break;
                case 3:
                    
                    break;
                case 4:

                    break;
                case 5:
                    return;
                default:
                    System.out.println("Please select the correct choice.");
            }

        }
    }
}
