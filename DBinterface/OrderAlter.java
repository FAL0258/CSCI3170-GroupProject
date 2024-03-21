package DBinterface;

import java.sql.*;
import java.util.HashMap;

public class OrderAlter {
    public Connection currSession;
    HashMap<String, String> map = new HashMap<>(); 

    public OrderAlter(Connection con){
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

    public void add(String key, String value){
        map.put(key, value);
    }

    public void alterAdd(String key, int quantity){
        String targetISBN = map.get(key);
        int bookStock = getBookStock(targetISBN);
        if (quantity > bookStock){
            System.out.println("Insufficient stock of the current book");
        }
        else{
            // Update 'copies' FROM book

            // Update 'oDate' and 'charge' FROM orders

            // Update 'quantity' FROM bookOrdered


        }
        
    }
}
