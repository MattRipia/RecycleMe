package ripia.matt.recycleme;

import android.os.StrictMode;
import android.util.Log;

import com.google.firebase.auth.FirebaseUser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private Connection conn = null;
    private Statement statement = null;
    private String connectionString = "jdbc:jtds:sqlserver://recyclemeserver.database.windows.net:1433/Recycleme;user=mattripia@recyclemeserver;password=Hello1234;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
    private String driverName = "net.sourceforge.jtds.jdbc.Driver";
    private Globals globals;


    public Database(){

        globals = Globals.getInstance();
        initializeDb();
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public Statement getStatement() {
        return statement;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }

    // initialises the db object where we will communicate with our database
    // creates a driver using jtds which is used to query the microsoft sql server
    private void initializeDb() {

        // This prevents the app from crashing as internet i/o shouldn't be completed on the main thread
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try { // loading the driver jtds
            Class.forName(driverName);

        } catch (Exception e) {
            Log.d("debug driver exception ", " " + e);
        }

        try { // establishing a connection to the db and running the test statement
            conn = DriverManager.getConnection(connectionString);
            statement = conn.createStatement();

        } catch (Exception e) {
            Log.d("debug sql exception", " " + e);
        }
    }

    public void closeDB() {
        try {
            conn.close();
            statement.close();

        } catch (SQLException e) {
            Log.d("debug sql close ex - ", " " + e);
        }
    }

    public User createCurrentUser(FirebaseUser firebaseUser){

        ResultSet rs;
        User currentUser= new User();
        currentUser.setName(firebaseUser.getDisplayName());
        currentUser.setUniqueID(firebaseUser.getUid());
        String queryUser = "select * from account where uniqueid = '" + currentUser.getUniqueID() + "'";

        try {
            //statement = conn.createStatement();
            rs = statement.executeQuery(queryUser);

            // a user already exists in the database, pull their data now!
            if(rs.next())
            {
                currentUser.setUniqueID(rs.getString(1));
                currentUser.setName(rs.getString(2));
                currentUser.setAddress(rs.getString(3));
                currentUser.setPoints(rs.getInt(4));
                Log.d("checkUserInDatabase", " User Exists in DB");
            }
            else  // a user doesn't exist in the database, create a new record now!
            {
                String insertUser = "insert into account values('"
                        + currentUser.getUniqueID() + "','"
                        + currentUser.getName()  + "','"
                        + currentUser.getAddress()  + "',"
                        + currentUser.getPoints()+ ")";

                statement.executeUpdate(insertUser);
                Log.d("checkUserInDatabase", " New user inserted");
            }
        } catch (SQLException e) {

            Log.d("SetupUser tag exception", " " + e);
        }

        return currentUser;
    }

    public Boolean checkItemInDatabase(Item item){

        ResultSet rs;
        String queryItem = "select * from item where barcode = '" + item.getBarcode() + "'";
        Boolean itemExists = false;

        try {
            rs = statement.executeQuery(queryItem);

            // an item already exists in the database, pull its data now!
            if(rs.next())
            {
                itemExists = true;
                globals.getCurrentItem().setName(rs.getString(2));
                globals.getCurrentItem().setBrand(rs.getString(3));
                globals.getCurrentItem().setRecNumber(rs.getInt(4));
                Log.d("checkItemInDatabase", " Item Exists in DB");
            }
            // an item doesn't exist in the database, create a new record now!
            else
            {
                  itemExists = false;
                Log.d("checkItemInDatabase", " Item DOES NOT EXIST in DB");
            }
        } catch (SQLException e) {

        Log.d("checkItemInDatabase ex", " " + e);
        }

        return itemExists;
    }

    public void addItem() {
        Log.d("DB", "item barcode: " + globals.getCurrentItem().getBarcode());
        Log.d("DB", "item name: " + globals.getCurrentItem().getName());
        Log.d("DB", "item brand: " + globals.getCurrentItem().getBrand());
        Log.d("DB", "item rec #: " + globals.getCurrentItem().getRecNumber());


        String insertItem = "insert into item values('"
        + globals.getCurrentItem().getBarcode() + "','"
        + globals.getCurrentItem().getName() + "','"
        + globals.getCurrentItem().getBrand() + "',"
        + globals.getCurrentItem().getRecNumber()+ ")";

        try {
            statement.executeUpdate(insertItem);

        } catch (SQLException e) {
            Log.d("addItem ex", " error - " + e);
        }

        Log.d("addItem", " New item inserted");
    }

    // updates the user in the database with their address and updated points
    public void updateDatabase(){

            String updateUser = "update account set points = " + globals.getCurrentUser().getPoints()
                                              + ", address = '" + globals.getCurrentUser().getAddress()
                                              + "' where uniqueid = '" + globals.getCurrentUser().getUniqueID() + "'";
        try {
            statement.executeUpdate(updateUser);
            Log.d("updateDatabase", " user updated");

        } catch (SQLException e) {

            Log.d("updateDatabase", " user update failed - " + e.getMessage());
        }
    }
}





