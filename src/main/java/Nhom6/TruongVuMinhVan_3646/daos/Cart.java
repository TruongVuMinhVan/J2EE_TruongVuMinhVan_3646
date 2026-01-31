package Nhom6.TruongVuMinhVan_3646.daos;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class Cart {
    private List<Item> cartItems = new ArrayList<>();

    public void addItems(Item item) {
        var existingItem = cartItems.stream()
                .filter(i -> Objects.equals(i.getBookId(), item.getBookId()))
                .findFirst();
        
        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(
                existingItem.get().getQuantity() + item.getQuantity()
            );
        } else {
            cartItems.add(item);
        }
    }

    public void removeItems(Long bookId) {
        cartItems.removeIf(item -> Objects.equals(item.getBookId(),
                bookId));
    }

    public void updateItems(int bookId, int quantity) {
        cartItems.stream()
                .filter(item -> Objects.equals(item
                        .getBookId(), (long) bookId))
                .forEach(item -> item.setQuantity(quantity));
    }
}