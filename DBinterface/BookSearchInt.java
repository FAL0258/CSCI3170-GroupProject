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

    public boolean needExactSearch(String str){
        // Check if the string fulfills all of the following condition
        // 1. Head or Tail is "%" or "_"
        // 2. Middle part contains "%" or "_" (Excepts head and tail)

        String cpStr = "";
        for (int i = 0; i < str.length(); i++) {
            if (i == 0 || i == str.length() - 1) {
                continue;
            }
            cpStr += str.charAt(i);
        }
        
        if (cpStr.contains("%") || cpStr.contains("_")){
            return false;
        }
        return true;
    }

    public void queryByISBN(Connection con, String ISBN){
        try{
            Statement stmt = con.createStatement();
            String query = "SELECT * FROM book WHERE ISBN = '" + ISBN + "'";
            ResultSet rs = stmt.executeQuery(query);
            
            int recordCount = 1;
            System.out.println();
            while(rs.next()){
                System.out.println("Record " + recordCount++);
                System.out.println("ISBN: " + rs.getString("ISBN"));
                System.out.println("Book Title: " + rs.getString("bTitle"));
                System.out.println("Unit Price: " + rs.getInt("price"));
                System.out.println("No of Available: " + rs.getInt("copies"));
            }

            if (recordCount == 1){
                System.out.println("No records found...");
                System.out.println();
                return;
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

    public void queryByTitle(Connection con, String title){

        try{
            Statement stmt = con.createStatement();
            ResultSet rs;
            String query = "";

            Statement authorStmt = con.createStatement();
            ResultSet authorRs;
            String ISBN;
            String subQuery = "";

            int recordCount = 1;
            int authorCount = 1;

            String subT = title.replaceAll("%", "");
            subT = subT.replaceAll("_", ""); 
            // Query for the exact match first if %or_ located at the beginning or the end of the string
            if (needExactSearch(title)){
                query = "SELECT * FROM book WHERE bTitle = '" + subT + "'";
                rs = stmt.executeQuery(query);

                System.out.println();
                while(rs.next()){
                    // Exact match exists
                    ISBN = rs.getString("ISBN");
                    
                    System.out.println("Record " + recordCount++);
                    System.out.println("ISBN: " + ISBN);
                    System.out.println("Book Title: " + rs.getString("bTitle"));
                    System.out.println("Unit Price: " + rs.getInt("price"));
                    System.out.println("No Of Available: " + rs.getInt("copies"));
                    System.out.println("Authors:");

                    
                    subQuery = "SELECT aName FROM authorship WHERE ISBN = '" + ISBN + "' ORDER BY aName ASC";
                    authorRs = authorStmt.executeQuery(subQuery);
                    while(authorRs.next()){
                        System.out.println(authorCount++ + " : " + authorRs.getString("aName"));
                    }
                    System.out.println();
                }
            }
            
            if (!(title.contains("%") || title.contains("_"))){
                if ( recordCount == 1 ){
                    System.out.println("No records found...");
                    System.out.println();
                }
                return;
            }

            // Exclude the exact search result
            if (subT.length() == 0){
                query = "SELECT * FROM book WHERE bTitle LIKE '" + title + "' ORDER BY bTitle ASC" ;
            }
            else{
                query = "SELECT * FROM book WHERE bTitle LIKE '" + title + "' AND bTitle != '" + subT + "' ORDER BY bTitle ASC" ;
            }
            rs = stmt.executeQuery(query);
            System.out.println();
            
            while(rs.next()){
                ISBN = rs.getString("ISBN");
                    
                System.out.println("Record " + recordCount++);
                System.out.println("ISBN: " + ISBN);
                System.out.println("Book Title: " + rs.getString("bTitle"));
                System.out.println("Unit Price: " + rs.getInt("price"));
                System.out.println("No Of Available: " + rs.getInt("copies"));
                System.out.println("Authors:");

                subQuery = "SELECT aName FROM authorship WHERE ISBN = '" + ISBN + "' ORDER BY aName ASC";
                authorRs = authorStmt.executeQuery(subQuery);
                authorCount = 1;
                while(authorRs.next()){
                    System.out.println(authorCount++ + " : " + authorRs.getString("aName"));
                }
                System.out.println();
            }
            
        }
        catch(SQLException e){
            System.out.println("Error: " + e);
        }
    }

    public void queryByAuthor(Connection con, String author){
        try{
            Statement stmt = con.createStatement();
            ResultSet rs;
            String query = "";

            Statement authorStmt = con.createStatement();
            ResultSet authorRs;
            Statement bookStmt = con.createStatement();
            ResultSet bookRs;
            String ISBN;
            String subQuery = "";
            String subQuery2 = "";

            int recordCount = 1;
            int authorCount = 1;

            String subA = author.replaceAll("%", "");
            subA = subA.replaceAll("_", ""); 
            // Query for the exact match first if %or_ located at the beginning or the end of the string
            if (needExactSearch(author) ){


                query = "SELECT * FROM authorship WHERE aName = '" + subA + "' ORDER BY ISBN ASC";
                rs = stmt.executeQuery(query);

                System.out.println();
                while(rs.next()){
                    // Exact match exists
                    ISBN = rs.getString("ISBN");
                    
                    subQuery = "SELECT * FROM book WHERE ISBN = '" + ISBN + "'";
                    bookRs = bookStmt.executeQuery(subQuery);

                    while(bookRs.next()){
                        System.out.println("Record " + recordCount++);
                        System.out.println("ISBN: " + ISBN);
                        System.out.println("Book Title: " + bookRs.getString("bTitle"));
                        System.out.println("Unit Price: " + bookRs.getInt("price"));
                        System.out.println("No Of Available: " + bookRs.getInt("copies"));
                        System.out.println("Authors:");

                        
                        subQuery2 = "SELECT aName FROM authorship WHERE ISBN = '" + ISBN + "' ORDER BY aName ASC";
                        authorRs = authorStmt.executeQuery(subQuery2);
                        authorCount = 1;
                        while(authorRs.next()){
                            System.out.println(authorCount++ + " : " + authorRs.getString("aName"));
                        }
                        System.out.println();
                    }
                }
            }
            
            if (!(author.contains("%") || author.contains("_"))){
                if ( recordCount == 1 ){
                    System.out.println("No records found...");
                    System.out.println();
                }
                return;
            }

            if (subA.length() == 0){
                query = "SELECT * FROM authorship WHERE aName LIKE '" + author + "' ORDER BY ISBN ASC" ;
            }
            else{
                query = "SELECT * FROM authorship WHERE aName LIKE '" + author + "' AND aName != '" + subA + "' ORDER BY ISBN ASC" ;
            }

            rs = stmt.executeQuery(query);
            System.out.println();
            
            while(rs.next()){
                // Exact match exists
                ISBN = rs.getString("ISBN");
                
                subQuery = "SELECT * FROM book WHERE ISBN = '" + ISBN + "'";
                bookRs = bookStmt.executeQuery(subQuery);

                while(bookRs.next()){
                    System.out.println("Record " + recordCount++);
                    System.out.println("ISBN: " + ISBN);
                    System.out.println("Book Title: " + bookRs.getString("bTitle"));
                    System.out.println("Unit Price: " + bookRs.getInt("price"));
                    System.out.println("No Of Available: " + bookRs.getInt("copies"));
                    System.out.println("Authors:");

                    
                    subQuery2 = "SELECT aName FROM authorship WHERE ISBN = '" + ISBN + "' ORDER BY aName ASC";
                    authorRs = authorStmt.executeQuery(subQuery2);
                    authorCount = 1;
                    while(authorRs.next()){
                        System.out.println(authorCount++ + " : " + authorRs.getString("aName"));
                    }
                    System.out.println();
                }
            }

        }
        catch(SQLException e){
            System.out.println("Error: " + e);
        }
    }

    public void menu(){
        while (true) {
            printMenu();
            int choice = 0;
            boolean isInt = false;
            Scanner subInput = new Scanner(System.in);
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
                    System.out.print("Input the Book Title: ");
                    String bTitle = subInput.nextLine();
                    queryByTitle(currSession, bTitle);
                    break;

                case 3:
                    System.out.print("Input the Author Name: ");
                    String author = subInput.nextLine();
                    queryByAuthor(currSession, author);
                    break;

                case 4:
                    return;
                default:
                    System.out.println("Please select the correct choice.");
            }

        }
    }
}
