package Nhom6.TruongVuMinhVan_3646.controllers;

import Nhom6.TruongVuMinhVan_3646.entities.Invoice;
import Nhom6.TruongVuMinhVan_3646.entities.User;
import Nhom6.TruongVuMinhVan_3646.repositories.IUserRepository;
import Nhom6.TruongVuMinhVan_3646.services.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final InvoiceService invoiceService;
    private final IUserRepository userRepository;

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return null;
        }

        String email = null;
        Object principal = auth.getPrincipal();

        if (principal instanceof OAuth2User) {
            // OAuth2 login (Google)
            OAuth2User oauth2User = (OAuth2User) principal;
            email = oauth2User.getAttribute("email");
        } else if (principal instanceof org.springframework.security.core.userdetails.User) {
            // Form login - username is actually the email
            email = ((org.springframework.security.core.userdetails.User) principal).getUsername();
        } else if (principal instanceof User) {
            return (User) principal;
        }

        if (email != null) {
            Optional<User> userOpt = userRepository.findByEmail(email);
            if (userOpt.isPresent()) {
                return userOpt.get();
            }
            // Try by username as fallback
            return userRepository.findByUsername(email).orElse(null);
        }

        return null;
    }

    private boolean isAdmin(Authentication auth) {
        if (auth == null)
            return false;
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ADMIN"));
    }

    @GetMapping
    public String showOrderHistory(
            @RequestParam(required = false) String status,
            Model model) {

        User user = getCurrentUser();
        if (user == null) {
            return "redirect:/login";
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = isAdmin(auth);

        List<Invoice> orders;
        if (isAdmin) {
            // Admin sees all orders from all users
            orders = (status == null || status.isEmpty() || status.equals("ALL"))
                    ? invoiceService.getAllOrders()
                    : invoiceService.getAllOrdersByStatus(status);
        } else {
            // User sees only their own orders
            orders = (status == null || status.isEmpty() || status.equals("ALL"))
                    ? invoiceService.getOrdersByUser(user)
                    : invoiceService.getOrdersByUserAndStatus(user, status);
        }

        model.addAttribute("orders", orders);
        model.addAttribute("currentStatus", status == null ? "ALL" : status);
        model.addAttribute("isAdmin", isAdmin);

        return "order/history";
    }

    @GetMapping("/{id}")
    public String showOrderDetail(
            @PathVariable Long id,
            Model model) {

        User user = getCurrentUser();
        if (user == null) {
            return "redirect:/login";
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = isAdmin(auth);

        var order = invoiceService.getOrderById(id);
        if (order.isEmpty()) {
            return "redirect:/orders";
        }

        // Admin can view any order, User can only view their own
        if (!isAdmin && (order.get().getUser() == null || !order.get().getUser().getId().equals(user.getId()))) {
            return "redirect:/orders";
        }

        model.addAttribute("order", order.get());
        model.addAttribute("isAdmin", isAdmin);
        return "order/detail";
    }

    @PostMapping("/{id}/cancel")
    public String cancelOrder(@PathVariable Long id) {

        User user = getCurrentUser();
        if (user == null) {
            return "redirect:/login";
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = isAdmin(auth);

        var order = invoiceService.getOrderById(id);
        if (order.isPresent() && "PENDING".equals(order.get().getStatus())) {
            // Admin can cancel any order, User can only cancel their own
            if (isAdmin || (order.get().getUser() != null && order.get().getUser().getId().equals(user.getId()))) {
                invoiceService.updateInvoiceStatus(id, "CANCELLED");
            }
        }

        return "redirect:/orders";
    }

    @PostMapping("/{id}/delete")
    public String deleteOrder(@PathVariable Long id) {

        User user = getCurrentUser();
        if (user == null) {
            return "redirect:/login";
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = isAdmin(auth);

        var order = invoiceService.getOrderById(id);
        if (order.isPresent()) {
            // Admin can delete any order, User can only delete their own
            if (isAdmin || (order.get().getUser() != null && order.get().getUser().getId().equals(user.getId()))) {
                invoiceService.deleteOrder(id);
            }
        }

        return "redirect:/orders";
    }
}
