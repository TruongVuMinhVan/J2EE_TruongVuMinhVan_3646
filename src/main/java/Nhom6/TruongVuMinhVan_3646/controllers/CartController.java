package Nhom6.TruongVuMinhVan_3646.controllers;

import Nhom6.TruongVuMinhVan_3646.entities.User;
import Nhom6.TruongVuMinhVan_3646.repositories.IUserRepository;
import Nhom6.TruongVuMinhVan_3646.services.CartService;
import Nhom6.TruongVuMinhVan_3646.daos.Item;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final Nhom6.TruongVuMinhVan_3646.services.VNPayService vnPayService;
    private final IUserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

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

    @GetMapping
    public String showCart(HttpSession session,
            Model model) {
        model.addAttribute("cart", cartService.getCart(session));
        model.addAttribute("totalPrice",
                cartService.getSumPrice(session));
        model.addAttribute("totalQuantity",
                cartService.getSumQuantity(session));
        return "book/cart";
    }

    @GetMapping("/removeFromCart/{id}")
    public String removeFromCart(HttpSession session,
            @PathVariable Long id) {
        var cart = cartService.getCart(session);
        cart.removeItems(id);
        return "redirect:/cart";
    }

    @GetMapping("/updateCart/{id}/{quantity}")
    public String updateCart(HttpSession session,
            @PathVariable Long id,
            @PathVariable int quantity) {
        var cart = cartService.getCart(session);
        cart.updateItems(id, quantity);
        return "redirect:/cart";
    }

    @GetMapping("/clearCart")
    public String clearCart(HttpSession session) {
        cartService.removeCart(session);
        return "redirect:/cart";
    }

    @PostMapping("/add-to-cart")
    public String addToCart(HttpSession session,
            @RequestParam long id,
            @RequestParam String name,
            @RequestParam double price,
            @RequestParam(defaultValue = "1") int quantity) {
        logger.debug("Adding item to cart - id: {}, name: {}, price: {}, quantity: {}", id, name, price, quantity);
        var cart = cartService.getCart(session);
        cart.addItems(new Item(id, name, price, quantity));
        cartService.updateCart(session, cart);
        logger.debug("Item added to cart. Total items in cart: {}", cartService.getSumQuantity(session));
        return "redirect:/books";
    }

    @GetMapping("/checkout")
    public String checkout(HttpSession session) {
        User user = getCurrentUser();
        cartService.saveCart(session, "PENDING", user);
        return "redirect:/books?checkout=success";
    }

    @GetMapping("/checkoutVNPay")
    public String checkoutVNPay(HttpSession session, jakarta.servlet.http.HttpServletRequest request) {
        var cart = cartService.getCart(session);
        if (cart.getCartItems().isEmpty()) {
            return "redirect:/cart";
        }
        long amount = (long) (cartService.getSumPrice(session));
        String orderInfo = "Thanh toan don hang qua VNPay";
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnPayService.createPaymentUrl(amount, orderInfo, baseUrl);
        return "redirect:" + vnpayUrl;
    }

    @GetMapping("/vnpay-payment-return")
    public String vnpayReturn(jakarta.servlet.http.HttpServletRequest request, HttpSession session, Model model) {
        int paymentStatus = vnPayService.orderReturn(request);

        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");

        model.addAttribute("orderId", orderInfo);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("paymentTime", paymentTime);
        model.addAttribute("transactionId", transactionId);

        User user = getCurrentUser();

        if (paymentStatus == 1) {
            cartService.saveCart(session, "PAID", user);
            return "book/order-success";
        } else {
            return "book/order-fail";
        }
    }
}