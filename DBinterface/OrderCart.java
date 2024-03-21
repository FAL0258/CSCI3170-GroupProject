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

    public void updateBackDB(){
        PreparedStatement pstmt;
        try{
            // Update back to book database
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                String key = entry.getKey();
                Integer value = entry.getValue();
                int stock = getBookStock(key);
                int updateStock = stock - value;
                String query = "UPDATE book set copies = ? where ISBN = ? ";
                pstmt = currSession.prepareStatement(query);
                pstmt.setInt(1, updateStock);
                pstmt.setString(2, key);
                pstmt.executeUpdate();
            }

            // Update back to orders database


            // Update back to bookOrdered database
        }
        catch(SQLException e){
            System.out.println(e);
        }
    }

}
