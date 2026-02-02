package Nhom6.TruongVuMinhVan_3646.entities;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "item_invoice")
public class ItemInvoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "quantity")
    private int quantity;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", referencedColumnName = "id")
    private Invoice invoice;

    public ItemInvoice() {}

    public ItemInvoice(Long id, int quantity, Book book, Invoice invoice) {
        this.id = id;
        this.quantity = quantity;
        this.book = book;
        this.invoice = invoice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ItemInvoice that = (ItemInvoice) o;
        return getId() != null && Objects.equals(getId(),
                that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}