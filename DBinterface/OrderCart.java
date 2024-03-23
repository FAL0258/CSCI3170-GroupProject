package DBinterface;

import java.util.HashMap;
import java.util.Map;
import java.sql.*;

public class OrderCart {
    HashMap<String, Integer> map = new HashMap<>(); 
    Connection currSession;

    public OrderCart(Connection con){
        this.currSession = con;
    }

    public int getBookStock(String ISBN){
        try{
            Statement stmt = currSession.createStatement();
            String query = "SELECT copies FROM book WHERE ISBN = '" + ISBN + "'";
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()){
                return rs.getInt("copies");
            }
            System.out.println("Book not found.");
            return -1;
        }
        catch(SQLException e){
            System.out.println("Record not found.");
            return -1;
        }
    }

    public String getOrderID(){
        try{
            Statement stmt = currSession.createStatement();
            String query = "SELECT oID FROM orders ORDER BY oID DESC";
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()){
                return rs.getString("oID");
            }
            return null;
        }
        catch(SQLException e){
            System.out.print("Error: " + e);
            return null;
        }
    }

    public int getBookPrice(String ISBN){
        try{
            Statement stmt = currSession.createStatement();
            String query = "SELECT price FROM book WHERE ISBN = '" + ISBN + "'";
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()){
                return rs.getInt("price");
            }
            return -1;
        }
        catch(SQLException e){
            System.out.print("Error: " + e);
            return -1;
        }
    }

    public boolean emptyCart(){
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            return false;
        }
        return true;
    }

    public void add(String ISBN, int quantity){
        int stock = getBookStock(ISBN);
        if (stock == -1) return;

        if (map.containsKey(ISBN)){
            if (quantity + map.get(ISBN) <= stock){
                map.put(ISBN, map.get(ISBN) + quantity);
            }
            else{
                System.out.println("Not enough stock to add!");
            }
        }
        else{
            if (quantity <= stock){
                map.put(ISBN, quantity);
            }
            else{
                System.out.println("Not enough stock to add!");
            }
        }
        
    }

    public void show(){
        System.out.println("ISBN\t\t\tNumber:");
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            System.out.println(key + "\t\t\t" + value);
        }
    }

    public void updateBackDB(String today, String cID){
        if (emptyCart()){
            System.out.println("The list is empty, no updates were made...");
            return;
        }

        PreparedStatement pstmt;
        String query;
        String currOrderID = getOrderID();
        int bookPrice = 0;
        int shippingPrice = 0;
        int totalCharge = 0;
        int noOfBook = 0;

        if (currOrderID == null){
            currOrderID = "00000000";
        }
        else{
            currOrderID = String.format("%0" + currOrderID.length() + "d", Integer.parseInt(currOrderID) + 1);
        }

        try{
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                String key = entry.getKey();
                Integer value = entry.getValue();

                int stock = getBookStock(key);
                int updateStock = stock - value;

                bookPrice += (getBookPrice(key)*value);
                noOfBook += value;

                // Update back to book database
                query = "UPDATE book set copies = ? where ISBN = ? ";
                pstmt = currSession.prepareStatement(query);
                pstmt.setInt(1, updateStock);
                pstmt.setString(2, key);
                pstmt.executeUpdate();

                // Update back to bookOrdered database
                query = "INSERT INTO bookOrdered (oID, ISBN, quantity) VALUES (?, ?, ?)";
                pstmt = currSession.prepareStatement(query);
                pstmt.setString(1, currOrderID);
                pstmt.setString(2, key);
                pstmt.setInt(3, value);
                pstmt.execute();
            }

            shippingPrice = noOfBook*10 + 10;
            totalCharge = bookPrice + shippingPrice;

            
            // Update back to orders database
            query = "INSERT INTO orders (oID, oDate, status, charge, cID) VALUES (?, ?, ?, ?, ?)";
            pstmt = currSession.prepareStatement(query);
            pstmt.setString(1, currOrderID);
            pstmt.setString(2, today);
            pstmt.setString(3, "N");
            pstmt.setInt(4, totalCharge);
            pstmt.setString(5, cID);
            pstmt.execute();

            System.out.println("Database updated successfully.");
            System.out.println();

        }
        catch(SQLException e){
            System.out.println(e);
        }
    }

}
