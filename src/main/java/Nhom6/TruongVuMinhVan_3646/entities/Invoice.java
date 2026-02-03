package Nhom6.TruongVuMinhVan_3646.entities;

import jakarta.persistence.*;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "invoices")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "invoice_date")
    private Date invoiceDate = new Date();
    @Column(name = "total")
    private Double price;
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    private List<ItemInvoice> itemInvoices = new ArrayList<>();

    public Invoice() {
    }

    public Invoice(Long id, Date invoiceDate, Double price, List<ItemInvoice> itemInvoices) {
        this.id = id;
        this.invoiceDate = invoiceDate;
        this.price = price;
        this.itemInvoices = itemInvoices;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List<ItemInvoice> getItemInvoices() {
        return itemInvoices;
    }

    public void setItemInvoices(List<ItemInvoice> itemInvoices) {
        this.itemInvoices = itemInvoices;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Invoice invoice = (Invoice) o;
        return getId() != null && Objects.equals(getId(),
                invoice.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ToString.Exclude
    private User user;
}