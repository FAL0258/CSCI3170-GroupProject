import java.io.*;
import java.sql.*;
import extpkg.*;

class gp1Main{
    public static String dbAddress = "jdbc:oracle:thin://@db18.cse.cuhk.edu.hk:1521/oradb.cse.cuhk.edu.hk";
    public static String dbUsername = "h025";
    public static String dbPassword = "Snogquee";

    public static Connection connectToMySQL(){
        Connection con = null;
        System.out.println("Connecting...");
        try{
            Class.forName("oracle.jdbc.OracleDriver");
            con = DriverManager.getConnection(dbAddress, dbUsername, dbPassword);
        } catch (ClassNotFoundException e){
            System.out.println("[Error]: Java MySQL DB Driver not found!!");
            System.err.println(e);
            System.exit(0);
        } catch (SQLException e){
            System.out.println(e);
        }
        System.out.println("Connection established");
        return con;
    }

    public static void main(String[] args){

        // Connection con = connectToMySQL();

        // For local testing
        Connection con = null;
        new DbSys(con);
    }
}