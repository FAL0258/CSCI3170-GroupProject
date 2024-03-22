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

    public int getBookPrice(String ISBN){
        try{
            Statement stmt = currSession.createStatement();
            String query = "SELECT price FROM book WHERE ISBN = '" + ISBN + "'";
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()){
                return rs.getInt("price");
            }
            System.out.println("Book not found.");
            return -1;
        }
        catch(SQLException e){
            System.out.println("Record not found.");
            return -1;
        }
    }

    public int getOrderCharge(String oID){
        try{
            Statement stmt = currSession.createStatement();
            String query = "SELECT charge FROM orders WHERE oID = '" + oID + "'";
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()){
                return rs.getInt("charge");
            }
            System.out.println("Order not found.");
            return -1;
        }
        catch(SQLException e){
            System.out.println("Order not found.");
            return -1;
        }
    }

    public int getOrderQuantity(String oID, String ISBN){
        try{
            Statement stmt = currSession.createStatement();
            String query = "SELECT quantity FROM bookOrdered WHERE oID = '" + oID + "' AND ISBN = '" + ISBN + "'";
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()){
                return rs.getInt("quantity");
            }
            System.out.println("Order not found.");
            return -1;
        }
        catch(SQLException e){
            System.out.println("Order not found.");
            return -1;
        }
    }

    public void add(String key, String value){
        map.put(key, value);
    }

    public boolean alterAdd(String key, String oID, int quantity, String today){

        String targetISBN = map.get(key);
        int bookStock = getBookStock(targetISBN);

        PreparedStatement pstmt;
        String query;
        if (quantity > bookStock){
            System.out.println("Insufficient stock of the current book\n");
            return false;
        }
        else{
            try{
                // Update 'copies' FROM book
                int updatedCopies = bookStock - quantity;
                query = "UPDATE book SET copies = ? WHERE ISBN = ? ";
                pstmt = currSession.prepareStatement(query);
                pstmt.setInt(1, updatedCopies);
                pstmt.setString(2, targetISBN);
                pstmt.executeUpdate();

                // Update 'oDate' and 'charge' FROM orders
                int addedBookPrice = (quantity*getBookPrice(targetISBN)) + (quantity*10);

                int updatedPrice = addedBookPrice + getOrderCharge(oID); 
                query = "UPDATE orders SET oDate = ?, charge = ? WHERE oID = ? ";
                pstmt = currSession.prepareStatement(query);
                pstmt.setString(1, today);
                pstmt.setInt(2, updatedPrice);
                pstmt.setString(3, oID);
                pstmt.executeUpdate();

                // Update 'quantity' FROM bookOrdered
                int updatedQuantity = getOrderQuantity(oID, targetISBN) + quantity;
                query = "UPDATE bookOrdered SET quantity = ? WHERE ISBN = ? AND oID = ?";
                pstmt = currSession.prepareStatement(query);
                pstmt.setInt(1, updatedQuantity);
                pstmt.setString(2, targetISBN);
                pstmt.setString(3, oID);
                pstmt.executeUpdate();

                System.out.println("Database updated successfully.");
                System.out.println();

                return true;
            }
            catch (SQLException e){
                System.out.println(e);
                return false;
            }
        }
    }

    public boolean alterRemove(String key, String oID, int quantity, String today){

        String targetISBN = map.get(key);
        int bookStock = getBookStock(targetISBN);

        PreparedStatement pstmt;
        String query;
        if (quantity > bookStock){
            System.out.println("Insufficient stock of the current book\n");
            return false;
        }
        else{
            try{
                // Update 'copies' FROM book
                int updatedCopies = bookStock - quantity;
                query = "UPDATE book SET copies = ? WHERE ISBN = ? ";
                pstmt = currSession.prepareStatement(query);
                pstmt.setInt(1, updatedCopies);
                pstmt.setString(2, targetISBN);
                pstmt.executeUpdate();

                // Update 'oDate' and 'charge' FROM orders
                int addedBookPrice = (quantity*getBookPrice(targetISBN)) + (quantity*10);

                int updatedPrice = addedBookPrice + getOrderCharge(oID); 
                query = "UPDATE orders SET oDate = ?, charge = ? WHERE oID = ? ";
                pstmt = currSession.prepareStatement(query);
                pstmt.setString(1, today);
                pstmt.setInt(2, updatedPrice);
                pstmt.setString(3, oID);
                pstmt.executeUpdate();

                // Update 'quantity' FROM bookOrdered
                int updatedQuantity = getOrderQuantity(oID, targetISBN) + quantity;
                query = "UPDATE bookOrdered SET quantity = ? WHERE ISBN = ? AND oID = ?";
                pstmt = currSession.prepareStatement(query);
                pstmt.setInt(1, updatedQuantity);
                pstmt.setString(2, targetISBN);
                pstmt.setString(3, oID);
                pstmt.executeUpdate();

                System.out.println("Database updated successfully.");
                System.out.println();

                return true;
            }
            catch (SQLException e){
                System.out.println(e);
                return false;
            }
        }
    }
}
