package extpkg;

import java.io.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.Date;

public class DbSys {
    public Connection currSession;
    public Scanner input;
    public String date;

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date currDate = new Date();

    public DbSys(Connection con){
        this.currSession = con;
        this.input = new Scanner(System.in);
        this.date = dateFormat.format(currDate);
        menu();
    }

    public void printMenu() {
        System.out.println("The System Date is now: " + date);
        System.out.println("<This is the Book Ordering System.");
        System.out.println("---------------------------------------");
        System.out.println("1. System interface.");
        System.out.println("2. Customer interface.");
        System.out.println("3. Bookstore interface.");
        System.out.println("4. Show System Date.");
        System.out.println("5. Quit the system......\n");
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
                    new SysInt(currSession, input);
                    break;

                case 2:
                    new CusInt(currSession, input);
                    break;
                case 3:
                    new BookInt(currSession, input);
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
