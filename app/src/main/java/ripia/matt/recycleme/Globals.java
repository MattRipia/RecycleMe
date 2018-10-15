package ripia.matt.recycleme;

import android.util.Log;

import java.util.ArrayList;

/**
*  this is a class which holds all of the global variables, these are used in most other classes
*  as the same instance variables need to be used thorughout our application. This class uses a
*  singleton pattern with no constructor to ensure only one instance of 'Globals' exists at a time.
*  The 'getInstance' method is what is used to get a reference to the globals instance.
*  This class has a static reference to itself which will hold all the other variables we need.
*
* */

public class Globals {

    private Database database;
    private Item currentItem;
    private User currentUser;
    private static Globals instance;
    private ArrayList<User> leaders;

    public Globals(){
    }

    public static synchronized Globals getInstance(){

        if(instance == null) {
            instance = new Globals();
        }
        return instance;
    }

    public ArrayList<User> getLeaders() {

        Log.d("leaders", " Getting leaders - Globals class");

//        if (leaders != null) {
//            return leaders;
//        }
//        else {

            leaders = database.getLeaders();
            return leaders;

//        }
    }

    public User getCurrentUser() {
        return this.currentUser;
    }

    public void setCurrentUser(User aUser) {
        this.currentUser = aUser;
    }

    public Database getDatabase() {
        return this.database;
    }

    public void setDatabase(Database aDatabase) {
        this.database = aDatabase;
    }

    public Item getCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(Item currentItem) {
        this.currentItem = currentItem;
    }
}
