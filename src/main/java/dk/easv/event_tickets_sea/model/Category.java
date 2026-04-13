package dk.easv.event_tickets_sea.model;

public class Category {
    private int categoryId;
    private String categoryName;
    private String description;
    private double price;
    private int quantity;

    public Category(int categoryId, String categoryName, String description, double price, int quantity) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getPriceFormatted() {
        if (price == 0) return "Free";
        return String.format("%.2f DKK", price);
    }
}
