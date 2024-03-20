package DBs;

import java.sql.*;

public class OrdersDB {
    public String oID;
    public String cID;
    public String oDate;
    public String status;
    public int charge;

    public OrdersDB(String oID, String oDate, String status, int charge, String cID) {
        this.oID = oID;
        this.oDate = oDate;
        this.status = status;
        this.charge = charge;
        this.cID = cID;

    }

    public void insertDB(Connection con){
        try{
            PreparedStatement pstmt = con.prepareStatement("INSERT INTO orders (oID, oDate, status, charge, cID) VALUES (?, ?, ?, ?, ?)");
            pstmt.setString(1, oID);
            pstmt.setString(2, oDate);
            pstmt.setString(3, status);
            pstmt.setInt(4, charge);
            pstmt.setString(5, cID);
            pstmt.execute();
        }
        catch(SQLException e){
            System.out.println(e);
        }
    }

}
