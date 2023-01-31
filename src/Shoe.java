public class Shoe {
    private int id;
    private String brand;
    private String modelName;
    private int price;
    private int amount;
    private String colour;
    private int size;
    public Shoe(){

    }

    public Shoe(int id, String brand, String modelName, int price, int amount, String colour, int size) {
        this.id = id;
        this.brand = brand;
        this.modelName = modelName;
        this.price = price;
        this.amount = amount;
        this.colour = colour;
        this.size = size;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
