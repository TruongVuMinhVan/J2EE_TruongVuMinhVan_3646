package Nhom6.TruongVuMinhVan_3646.services;

import Nhom6.TruongVuMinhVan_3646.entities.Invoice;
import Nhom6.TruongVuMinhVan_3646.entities.User;
import Nhom6.TruongVuMinhVan_3646.repositories.IInvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InvoiceService {
    private final IInvoiceRepository invoiceRepository;

    public List<Invoice> getOrdersByUser(User user) {
        return invoiceRepository.findByUserOrderByInvoiceDateDesc(user);
    }

    public List<Invoice> getOrdersByUserAndStatus(User user, String status) {
        return invoiceRepository.findByUserAndStatusOrderByInvoiceDateDesc(user, status);
    }

    // Admin methods - get all orders
    public List<Invoice> getAllOrders() {
        return invoiceRepository.findAllByOrderByInvoiceDateDesc();
    }

    public List<Invoice> getAllOrdersByStatus(String status) {
        return invoiceRepository.findByStatusOrderByInvoiceDateDesc(status);
    }

    public Optional<Invoice> getOrderById(Long id) {
        return invoiceRepository.findById(id);
    }

    public void updateInvoiceStatus(Long id, String status) {
        invoiceRepository.findById(id).ifPresent(invoice -> {
            invoice.setStatus(status);
            invoiceRepository.save(invoice);
        });
    }

    public void deleteOrder(Long id) {
        invoiceRepository.deleteById(id);
    }
}
