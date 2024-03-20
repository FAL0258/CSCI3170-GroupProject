package DBs;

import java.io.*;
import java.sql.*;

public class OrderDB {
    public String oID;
    public String cID;
    public String oDate;
    public String status;
    public int charge;

    public OrderDB(String oID, String cID, String oDate, String status, int charge) {
        this.oID = oID;
        this.cID = cID;
        this.oDate = oDate;
        this.status = status;
        this.charge = charge;

    }
}
