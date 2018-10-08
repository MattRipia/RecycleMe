package ripia.matt.recycleme;

import java.util.ArrayList;
import java.util.Queue;

/**
 *  The user class which is used to maintain a currentUser
 *
 * */

public class User {

    private String uniqueID, name, address;
    private ArrayList<Item> items;
    private int points;

    public User() {
        this.items = new ArrayList<>();
        this.name = "";
        this.uniqueID = "";
        this.address = "";
        this.points = 0;
    }

    // This adds an item to the array list of items, removes the oldest item from the list if there is 10 or more items already.
    public void addItemToList(Item item) {

        if (items.size() >= 10) {
            items.remove(0);
        }

        items.add(item);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public ArrayList<Item> getItems() { return items; }

    public void setItems(ArrayList<Item> items) { this.items = items; }
}
