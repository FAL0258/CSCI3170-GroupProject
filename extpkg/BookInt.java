package extpkg;

import java.io.*;
import java.sql.*;
import java.util.Scanner;

public class BookInt {
    public Connection currSession;
    public Scanner input;

    public BookInt(Connection con, Scanner input){
        this.currSession = con;
        this.input = input;
        menu();
    }

    public void printMenu(){
        System.out.println("<This is the bookstore interface.>");
        System.out.println("---------------------------------------");
        System.out.println("1. Order Update.");
        System.out.println("2. Order Query.");
        System.out.println("3. N most Popular Book Query.");
        System.out.println("4. Back to main menu.\n");
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
                    return;
                default:
                    System.out.println("Please select the correct choice.");
            }

        }
    }
}
