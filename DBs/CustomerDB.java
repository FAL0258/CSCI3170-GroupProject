package DBs;

import java.sql.*;

public class CustomerDB {
    public String cID;
    public String cName;
    public String address;
    public String cred_card;


    public CustomerDB(String cID, String cName, String address, String cred_card) {
        this.cID = cID;
        this.cName = cName;
        this.address = address;
        this.cred_card = cred_card;


    }

    public void insertDB(Connection con){
        try{
            PreparedStatement pstmt = con.prepareStatement("INSERT INTO customer (cID, cName, address, cred_card) VALUES (?, ?, ?, ?)");
            pstmt.setString(1, cID);
            pstmt.setString(2, cName);
            pstmt.setString(3, address);
            pstmt.setString(4, cred_card);
            pstmt.execute();
        }
        catch(SQLException e){
            System.out.println(e);
        }
    }
}
