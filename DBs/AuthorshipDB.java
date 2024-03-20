package DBs;

import java.io.*;
import java.sql.*;

public class AuthorshipDB {
    public String ISBN;
    public String aName;

    public AuthorshipDB(String ISBN, String aName) {
        this.ISBN = ISBN;
        this.aName = aName;

    }
}
