package Nhom6.TruongVuMinhVan_3646.repositories;

import Nhom6.TruongVuMinhVan_3646.entities.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IInvoiceRepository extends JpaRepository<Invoice, Long> {
}
