package DBinterface;

import java.io.*;
import java.sql.*;
import java.util.Scanner;
import DBs.*;

public class BookSearchInt {
    public Connection currSession;
    public Scanner input;

    public BookSearchInt(Connection con, Scanner input){
        this.currSession = con;
        this.input = input;
        menu();
    }

    public void printMenu(){
        System.out.println("What do u want to search??");
        System.out.println("1. ISBN");
        System.out.println("2. Book Title");
        System.out.println("3. Author Name");
        System.out.println("4. Exit");
        System.out.printf("Your choice?...");
    }

    public void queryByISBN(Connection con, String ISBN){
        try{
            Statement stmt = con.createStatement();
            String query = "SELECT * FROM book WHERE ISBN = '" + ISBN + "'";
            ResultSet rs = stmt.executeQuery(query);
            if (!rs.next()){
                System.out.println("No records found from the database");
                return;
            }
            System.out.println("Record 1");
            while(rs.next()){
                System.out.println("ISBN: " + rs.getString("ISBN"));
                System.out.println("Book Title: " + rs.getString("bTitle"));
                System.out.println("Unit Price: " + rs.getInt("price"));
                System.out.println("No of Available: " + rs.getInt("copies"));
            }

            query = "SELECT * FROM authorship WHERE ISBN = '" + ISBN + "' ORDER BY aName ASC";
            rs = stmt.executeQuery(query);
            System.out.println("Authors:");
            int aCount = 1;
            while(rs.next()){
                System.out.println(aCount++ + " : " + rs.getString("aName"));
            }
            System.out.println();
        }
        catch(SQLException e){
            System.out.println("No records found from the database");
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
                    System.out.print("Input the ISBN: ");
                    String iISBN = input.next();
                    queryByISBN(currSession, iISBN);
                    break;

                case 2:

                    break;
                case 3:
                    
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Please select the correct choice.");
            }

        }
    }
}
