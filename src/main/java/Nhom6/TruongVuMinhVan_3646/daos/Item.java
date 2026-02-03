package Nhom6.TruongVuMinhVan_3646.daos;

public class Item {
    private Long bookId;
    private String name;
    private Double price;
    private int quantity;

    public Item() {}
    
    public Item(Long bookId, String name, Double price, int quantity) {
        this.bookId = bookId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
