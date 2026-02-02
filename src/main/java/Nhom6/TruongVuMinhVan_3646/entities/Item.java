package Nhom6.TruongVuMinhVan_3646.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    private long id;
    private String name;
    private double price;
    private int quantity;
    
    public double getTotalPrice() {
        return price * quantity;
    }
}
