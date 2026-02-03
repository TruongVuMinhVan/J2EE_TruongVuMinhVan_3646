package Nhom6.TruongVuMinhVan_3646.controllers;

import Nhom6.TruongVuMinhVan_3646.services.CartService;
import Nhom6.TruongVuMinhVan_3646.daos.Item;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

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
        cart.updateItems(Math.toIntExact(id), quantity);
        return "book/cart";
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
        cartService.saveCart(session);
        cartService.removeCart(session);
        return "redirect:/books?checkout=success";
    }
}