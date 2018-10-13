package ripia.matt.recycleme;

import android.os.StrictMode;
import android.util.Log;
import com.google.firebase.auth.FirebaseUser;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.xml.transform.Result;

/**
 *  The database class which holds all the connection information to our microsoft sql server. This class is
 *  also used for any query's or updates to and from the sql server.
 *
 * */

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

    // initialises the db object where we will communicate with our database
    // creates a driver using jtds which is used to query the microsoft sql server
    private void initializeDb() {

        // This prevents the app from crashing as internet i/o shouldn't be completed on the main thread
        // this will need to be changed so that database access is done through Async tasks.
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // loading the driver jtds
        try {
            Class.forName(driverName);

        } catch (Exception e) {
            Log.d("debug driver exception ", " " + e);
        }

        // establishing a connection to the db and creating a statement from this connection
        try {
            conn = DriverManager.getConnection(connectionString);
            statement = conn.createStatement();

        } catch (Exception e) {
            Log.d("debug sql exception", " " + e);
        }
    }

    // closes that database connection
    protected void closeDB() {
        try {
            conn.close();
            statement.close();

        } catch (SQLException e) {
            Log.d("debug sql close ex - ", " " + e);
        }
    }

    // when a user logs in, this method checks the user against the backend database, if a user exists
    // then their details are pulled. This is done through the use of a uniqueid given to each user.
    protected User createCurrentUser(FirebaseUser firebaseUser){

        ResultSet rs;
        User currentUser= new User();
        currentUser.setName(firebaseUser.getDisplayName());
        currentUser.setUniqueID(firebaseUser.getUid());
        String queryUser = "select * from account where uniqueid = '" + currentUser.getUniqueID() + "'";

        try {
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

            // a user doesn't exist in the database, create a new record now!
            else
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

    // checks to see if the item scanned is in the database already.
    // returns true if an item exists and sets the current items details
    // from the returned resultSet.
    protected Boolean checkItemInDatabase(Item item){

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

            // an item doesn't exist in the database, return false
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

    // an item is added to the database from the currentItem variable
    protected void addItem() {

        String insertItem = "insert into item values('"
        + globals.getCurrentItem().getBarcode() + "','"
        + globals.getCurrentItem().getName() + "','"
        + globals.getCurrentItem().getBrand() + "',"
        + globals.getCurrentItem().getRecNumber()+ ")";

        try {
            statement.executeUpdate(insertItem);
            addHistoryItem();

        } catch (SQLException e) {
            Log.d("addItem ex", " error - " + e);
        }

        Log.d("addItem", " New item inserted");
    }

    // updates the user in the database with their address and updated points
    protected void updateDatabase(){

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

    // this method adds an item to the hostory table on the database, this table is a child on the item and account table and keeps a record of a users scan history
    protected void addHistoryItem(){

        String currentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String insertItemToHistory = "insert into history values('"
                + globals.getCurrentUser().getUniqueID() + "','"
                + globals.getCurrentItem().getBarcode() + "','"
                + currentDateTime + "')";

        try {
            statement.executeUpdate(insertItemToHistory);

        } catch (SQLException e) {
            Log.d("insertItemToHistory ex", " error - " + e);
        }

        Log.d("insertItemToHistory", " New item inserted into history table");

    }

    // this gets the item scan history from the database for an associated user. It then add these to a users items.
    public void syncItemHistory() {

        String queryHistoryTable = "select i.barcode, i.name, i.brand, i.recyclingno from item i, history h where h.uniqueid = '"+ globals.getCurrentUser().getUniqueID() +"' and h.barcode = i.barcode order by datetime desc";
        ResultSet rs;

        try {
            rs = statement.executeQuery(queryHistoryTable);

            // whiles there is a result, add the row
            while(rs.next())
            {
                Item item = new Item();
                item.setBarcode(rs.getString(1));
                item.setName(rs.getString(2));
                item.setBrand(rs.getString(3));
                item.setRecNumber(rs.getInt(4));

                globals.getCurrentUser().getItems().add(item);
            }

        } catch (SQLException e) {
            Log.d("syncItemHistory ex", " error - " + e);
        }

        Log.d("syncItemHistory", " syncItemHistory");
    }

    public ArrayList<User> getLeaders() {

        ArrayList<User> leaders = new ArrayList<>();

        ResultSet rs;
        String queryLeaders = "select * from account order by points fetch first 10 rows only";

        try {
            rs = statement.executeQuery(queryLeaders);

            // whiles there is a result, add the row
            while(rs.next())
            {
                User aUser = new User();
                aUser.setUniqueID(rs.getString(1));
                aUser.setName(rs.getString(2));
                aUser.setPoints(rs.getInt(4));
                Log.d("leaders", " User Exists in DB");

                leaders.add(aUser);
            }
        } catch (SQLException e) {
            Log.d("syncItemHistory ex", " error - " + e);
        }

        Log.d("syncItemHistory", " syncItemHistory");

        return leaders;
    }
}





