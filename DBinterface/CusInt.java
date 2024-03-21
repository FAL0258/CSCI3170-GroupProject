package DBinterface;

import java.io.*;
import java.sql.*;
import java.util.Scanner;
import DBs.*;

public class CusInt {
    public Connection currSession;
    public Scanner input;
    public String today;

    public CusInt(Connection con, Scanner input, String today){
        this.currSession = con;
        this.input = input;
        this.today = today;
        menu();
    }

    public int getInt(String arr, int begin, int end){
        double output = 0;
        double multiplier;
        int pre = end - begin;
        for(int i = begin; i < end + 1; i++){
            multiplier = Math.pow(Double.valueOf(10), Double.valueOf(pre--));
            // System.out.println(arr.charAt(i) + "*" + multiplier);
            output += Character.getNumericValue(arr.charAt(i))*multiplier;
        }
        return (int)output; 
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


    public void orderCreation(String cID, Scanner input, String today){
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
                    cart.updateBackDB(today, cID);
                    break;
            
                default:
                    if (cart.getBookStock(response) == -1){
                        break;
                    }
                    System.out.print("Please enter the quantity of the order: ");
                    desiredQuantity = Integer.parseInt(input.next());
                    if (desiredQuantity < 1){
                        System.out.println("You need to enter at least one quantity!");
                    }
                    else{
                        cart.add(response, desiredQuantity);
                    }
                    break;
            }
        }
    }

    public void orderAlter(String oID, Scanner input){
        boolean ok = false;
        boolean shipped = false;
        while (!ok){
            try{
                OrderAlter alterList = new OrderAlter(currSession);

                Statement orderStmt = currSession.createStatement();
                String ordersQuery = "SELECT * FROM orders WHERE oID = '" + oID + "'";
                ResultSet orderRs = orderStmt.executeQuery(ordersQuery);
                int count = 0;
                while(orderRs.next()){
                    count++;
                    Statement subStmt = currSession.createStatement();
                    String subQuery = "SELECT * FROM bookOrdered WHERE oID = '" + oID + "'";
                    ResultSet subRs = subStmt.executeQuery(subQuery);

                    if (orderRs.getString("status").equals("Y")){
                        shipped = true;
                    }
                    System.out.println("order_id:" + oID + "\tshipping:" + orderRs.getString("Status") + "\tcharge=" + orderRs.getInt("charge") + "\tcustomerId=" + orderRs.getString("cID"));
                    
                    int bookNo = 1;
                    while(subRs.next()){
                        alterList.add(Integer.toString(bookNo), subRs.getString("ISBN"));
                        System.out.println("book no: " + bookNo++ + "\tISBN = " + subRs.getString("ISBN") + "\tquantity = " + subRs.getInt("quantity"));
                    }
                }
                if (count == 0){
                    System.out.println("Order not found!");
                }

                if (!shipped){
                    System.out.println("Which book you want to alter (input book no.):");
                    String pickedBook = input.next();
                    System.out.println("input add or remove or \"Q\" to exit");
                    String option = input.next();
                    String addNum;
                    switch(option) {
                        case "add":
                            System.out.print("Input the number: ");
                            addNum = input.next();
                            alterList.alterAdd(pickedBook, Integer.valueOf(addNum));
                            break;
                        
                        case "remove":

                            break;

                        case "Q":
                            ok = true;
                        
                        default:
                            System.out.println("Please select the correct choice.");
                    }
                }
                else{
                    System.out.println("This order has been shipped, you can not alter this record...");
                    System.out.println();
                    ok = true;
                }
            }
            catch(SQLException e){
                System.out.println("Order not found!");
            }
        }
    }

    public void orderQuery(String cID, Scanner input){
        System.out.print("Please input the Year: ");
        String response = input.next();
        int qYear = Integer.parseInt(response);

        try{
            Statement stmt = currSession.createStatement();
            String query = "SELECT * FROM orders WHERE cID = '" + cID + "' ORDER BY oID ASC";
            ResultSet rs = stmt.executeQuery(query);
            int recordCount = 1;

            String resultDate;
            int resultYear;
            while(rs.next()){
                resultDate = rs.getString("oDate");
                resultYear = getInt(resultDate.toString(), 0, 3);
                System.out.println();
                if (resultYear == qYear){
                    System.out.println("Record : " + recordCount++);
                    System.out.println("OrderID : " + rs.getString("oID"));
                    System.out.println("OrderDate : " + resultDate);
                    System.out.println("Charge : " + rs.getInt("charge"));
                    System.out.println("Shipping stauts : " + rs.getString("status"));
                    System.out.println();
                }
            }
        }
        catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void menu(){
        while (true) {
            printMenu();
            int choice = 0;
            boolean isInt = false;
            String cID;
            String oID;
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
                    cID = input.next();
                    if (checkCustomer(cID)) orderCreation(cID, input, today);
                    break;

                case 3:
                    System.out.print("Please enter the OrderID that you want to change: ");
                    oID = input.next();
                    orderAlter(oID, input);
                    break;

                case 4:
                    System.out.print("Please Input Customer ID: ");
                    cID = input.next();
                    if (checkCustomer(cID)) orderQuery(cID, input);
                    break;

                case 5:
                    return;

                default:
                    System.out.println("Please select the correct choice.");
            }

        }
    }
}
