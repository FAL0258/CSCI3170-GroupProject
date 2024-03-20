package DBs;

import java.io.*;
import java.sql.*;

public class CustomerDB {
    public String cID;
    public String cName;
    public String cred_card;
    public String address;

    public CustomerDB(String cID, String cName, String cred_card, String address) {
        this.cID = cID;
        this.cName = cName;
        this.cred_card = cred_card;
        this.address = address;

    }
}
