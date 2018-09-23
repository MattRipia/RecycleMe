package ripia.matt.recycleme;

/*
*  this is a class which holds all of the global variables
*
*
* */
public class Globals {

    private Database database;
    private Item currentItem;
    private User currentUser;
    private static Globals instance;

    public Globals(){

    }

    public static synchronized Globals getInstance(){

        if(instance == null) {
            instance = new Globals();
        }
        return instance;
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
