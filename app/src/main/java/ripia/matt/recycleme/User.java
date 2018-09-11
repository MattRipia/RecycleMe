package ripia.matt.recycleme;

public class User {

    private String name;
    private String address;
    private int points;

    public User() {
        this.name = "unknown";
        this.address = "unknown";
        this.points = 0;
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
}
