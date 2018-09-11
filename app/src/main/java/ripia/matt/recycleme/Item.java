package ripia.matt.recycleme;

public class Item {

    private String barcode;
    private String description;
    private String brand;
    private int recyclingNumber;

    public Item() {

        this.setBarcode(null);
        this.setDescription(null);
        this.setBrand(null);
        this.setRecyclingNumber(0);
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getRecyclingNumber() {
        return recyclingNumber;
    }

    public void setRecyclingNumber(int recyclingNumber) {
        this.recyclingNumber = recyclingNumber;
    }
}
