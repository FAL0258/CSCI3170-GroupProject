package extpkg;

import java.io.*;
import java.sql.*;
import java.util.Scanner;

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

    public void insertData(Scanner input){
        boolean isInt = false;
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
                    
                    break;

                case 2:

                    break;
                case 3:
                    
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
