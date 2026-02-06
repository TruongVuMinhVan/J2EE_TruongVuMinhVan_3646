package Nhom6.TruongVuMinhVan_3646.repositories;

import Nhom6.TruongVuMinhVan_3646.entities.Invoice;
import Nhom6.TruongVuMinhVan_3646.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface IInvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByUserOrderByInvoiceDateDesc(User user);

    List<Invoice> findByUserAndStatusOrderByInvoiceDateDesc(User user, String status);

    // Admin queries - all orders
    List<Invoice> findAllByOrderByInvoiceDateDesc();

    List<Invoice> findByStatusOrderByInvoiceDateDesc(String status);
}
