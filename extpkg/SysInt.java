package extpkg;

import java.io.*;
import java.sql.*;
import java.util.Scanner;

public class SysInt {
    public Connection currSession;
    public Scanner input;

    public SysInt(Connection con, Scanner input){
        this.currSession = con;
        this.input = input;
        menu();
    }

    public void printMenu(){
        System.out.println("<This is the system interface.");
        System.out.println("---------------------------------------");
        System.out.println("1. Create Table.");
        System.out.println("2. Delete Table.");
        System.out.println("3. Insert Data.");
        System.out.println("4. Set System Date.");
        System.out.println("5. Back to main menu.\n");
        System.out.printf("Please enter your choice??..");
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
                    
                    break;

                case 2:

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
