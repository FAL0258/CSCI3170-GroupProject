package DBs;

import java.io.*;
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
}
