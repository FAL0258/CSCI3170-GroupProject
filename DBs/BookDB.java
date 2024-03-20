package DBs;

import java.sql.*;

public class BookDB {
    public String ISBN;
    public String bTitle;
    public int price;
    public int copies;

    public BookDB(String ISBN, String bTitle, int price, int copies) {
        this.ISBN = ISBN;
        this.bTitle = bTitle;
        this.price = price;
        this.copies = copies;
        // Need to refine how author works

    }

    public void insertDB(Connection con){
        try{
            PreparedStatement pstmt = con.prepareStatement("INSERT INTO book (ISBN, bTitle, price, copies) VALUES (?, ?, ?, ?)");
            pstmt.setString(1, ISBN);
            pstmt.setString(2, bTitle);
            pstmt.setInt(3, price);
            pstmt.setInt(4, copies);
            pstmt.execute();
        }
        catch(SQLException e){
            System.out.println(e);
        }
    }
}
