package DBinterface;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Scanner;
import DBs.*;

public class SysInt {
    public Connection currSession;
    public Scanner input;
    public String date;

    public SysInt(Connection con, Scanner input, String date){
        this.currSession = con;
        this.input = input;
        this.date = date;
    }

    public void printMenu(){
        System.out.println("<This is the system interface.>");
        System.out.println("---------------------------------------");
        System.out.println("1. Create Table.");
        System.out.println("2. Delete Table.");
        System.out.println("3. Insert Data.");
        System.out.println("4. Set System Date.");
        System.out.println("5. Back to main menu.\n");
        System.out.printf("Please enter your choice??..");
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

    public String setDate(Scanner input){
        // First query for latest date from current records
        String latestDate = "1999-01-01";
        int latestDateYear = getInt(latestDate, 0, 3);
        int latestDateMonth = getInt(latestDate, 5, 6);
        int latestDateDay = getInt(latestDate, 8, 9);
        // System.out.println(latestDateYear);
        // System.out.println(latestDateMonth);
        // System.out.println(latestDateDay);

        int year = 0;
        int month = 0;
        int day = 0;

        Scanner dInput = input;
        int intDate;
        String outDate;
        boolean errFlag = true;
        while(errFlag){
            try{
                intDate = dInput.nextInt();
                outDate = Integer.toString(intDate);
                year = getInt(outDate, 0, 3);
                month = getInt(outDate, 4, 5);
                day = getInt(outDate, 6, 7);
                // System.out.println(year);
                // System.out.println(month);
                // System.out.println(day);

                if (year < latestDateYear || month < latestDateMonth || day < latestDateDay){
                    System.out.println("Please input the date later than the latest date in orders!");
                    dInput.next();
                    continue;
                }
                else{
                    errFlag = false;
                }
            }
            catch(Exception e){
                System.out.println("Please input only the number!");
                dInput.next();
                continue;
            }
        }
        return (Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(day));
    }

    public void createTable(){
        try{
            PreparedStatement[] pstmts = {
                currSession.prepareStatement("CREATE TABLE book (ISBN CHAR(13) NOT NULL, bTitle VARCHAR(100) NOT NULL, price INTEGER NOT NULL, copies INTEGER NOT NULL, PRIMARY KEY (ISBN))"),
                currSession.prepareStatement("CREATE TABLE orders (oID CHAR(8) NOT NULL, cID CHAR(10) NOT NULL, oDate CHAR(10) NOT NULL, status CHAR(1) NOT NULL, charge INTEGER NOT NULL, PRIMARY KEY (oID))"),
                currSession.prepareStatement("CREATE TABLE customer (cID CHAR(10) NOT NULL, cName VARCHAR(50) NOT NULL, cred_card CHAR(19) NOT NULL, address VARCHAR(200) NOT NULL, PRIMARY KEY(cID))"),
                currSession.prepareStatement("CREATE TABLE authorship (ISBN CHAR(13) NOT NULL, aName VARCHAR(50) NOT NULL, PRIMARY KEY (ISBN, aName))"),
                currSession.prepareStatement("CREATE TABLE bookOrdered (oID CHAR(8) NOT NULL, ISBN CHAR(13) NOT NULL, quantity INTEGER NOT NULL, PRIMARY KEY (oID, ISBN))")
            };
            for(PreparedStatement pstmt : pstmts) {
                pstmt.execute();
            }
        }
        catch(SQLException e){
            System.out.println(e);
        }
    
    }

    public void deleteTable(){
        try{
            PreparedStatement[] pstmts = {
                currSession.prepareStatement("DROP TABLE book"),
                currSession.prepareStatement("DROP TABLE orders"),
                currSession.prepareStatement("DROP TABLE customer"),
                currSession.prepareStatement("DROP TABLE authorship"),
                currSession.prepareStatement("DROP TABLE bookOrdered")
            };
            for(PreparedStatement pstmt : pstmts) {
                pstmt.execute();
            }
        }
        catch(SQLException e){
            System.out.println(e);
        }
    }

    public void insertData(Scanner input){
        BufferedReader reader;

        System.out.println("Please enter the folder path");
        String path = input.next();
        try{
            Path fullpath = Paths.get(path);
            File folder = new File(fullpath.toAbsolutePath().toString());
            // System.out.println(fullpath.toAbsolutePath().toString());

            System.out.print("Processing...");
            for(File file : folder.listFiles()){
                
                reader = new BufferedReader(new FileReader(file));
                String line = reader.readLine();
                String fileName = file.getName().toString().replaceFirst("[.][^.]+$", "");
                // System.out.println(file);
                // System.out.println(fileName);

                while (line != null) {
                    String[] elements = line.split("\\|");
                    switch (fileName) {
                        case "book_author":

                            break;
                        case "book":
                            BookDB thisBook = new BookDB(elements[0], elements[1], Integer.valueOf(elements[2]), Integer.valueOf(elements[3]));
                            thisBook.insertDB(currSession);
                            break;
                        case "customer":

                            break;
                        case "ordering":

                            break;
                        case "orders":

                            break;
                        default:
                            System.out.println("Unknown file");
                    }
                    line = reader.readLine();
                }
                reader.close();
            }
        }
        catch (Exception e){
            System.out.println("Folder / file not found, please try again.");
        }
    }

    public String menu(){
        String toSetDate = date;
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
                    createTable();
                    break;

                case 2:
                    deleteTable();
                    break;
                case 3:
                    insertData(input);
                    break;
                case 4:
                    toSetDate = setDate(input);
                    break;
                case 5:
                    return toSetDate;
                default:
                    System.out.println("Please select the correct choice.");
            }

        }
    }
}
