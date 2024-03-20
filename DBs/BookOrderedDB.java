package DBs;

import java.sql.*;

public class BookOrderedDB {
    public String oID;
    public String ISBN;
    public int quantity; // Maybe a list of book...


    public BookOrderedDB(String oID, String ISBN, int quantity) {
        this.oID = oID;
        this.ISBN = ISBN;
        this.quantity = quantity;

    }

    public void insertDB(Connection con){
        try{
            PreparedStatement pstmt = con.prepareStatement("INSERT INTO bookOrdered (oID, ISBN, quantity) VALUES (?, ?, ?)");
            pstmt.setString(1, oID);
            pstmt.setString(2, ISBN);
            pstmt.setInt(3, quantity);
            pstmt.execute();
        }
        catch(SQLException e){
            System.out.println(e);
        }
    }
}
