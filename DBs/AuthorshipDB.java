package DBs;

import java.sql.*;

public class AuthorshipDB {
    public String ISBN;
    public String aName;

    public AuthorshipDB(String ISBN, String aName) {
        this.ISBN = ISBN;
        this.aName = aName;
    }

    public void insertDB(Connection con){
        try{
            PreparedStatement pstmt = con.prepareStatement("INSERT INTO authorship (ISBN, aName) VALUES (?, ?)");
            pstmt.setString(1, ISBN);
            pstmt.setString(2, aName);
            pstmt.execute();
        }
        catch(SQLException e){
            System.out.println(e);
        }
    }
}
