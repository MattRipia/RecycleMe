package ripia.matt.recycleme;

/**
 *  The item class which holds the details of a current item.
 *
 * */

public class Item {

    private String name, barcode, brand;
    private int recNumber;

    public Item() {

        this.setBarcode(null);
        this.setName(null);
        this.setBrand(null);
        this.setRecNumber(0);
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getRecNumber() {
        return recNumber;
    }

    public void setRecNumber(int recyclingNumber) {
        this.recNumber = recyclingNumber;
    }

}
