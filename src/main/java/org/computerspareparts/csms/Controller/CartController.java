package org.computerspareparts.csms.Controller;

import jakarta.servlet.http.HttpSession;
import org.computerspareparts.csms.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    // ✅ Add product to cart
    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestParam Long productId,
                                            @RequestParam int quantity,
                                            HttpSession session) {
        Long customerId = (Long) session.getAttribute("customerId");

        if (customerId == null) {
            return ResponseEntity.status(401).body("Please login first");
        }

        String result = cartService.addToCart(customerId, productId, quantity);
        return ResponseEntity.ok(result);
    }

    // ✅ View cart items for logged-in customer
    @GetMapping("/view")
    public ResponseEntity<?> viewCart(HttpSession session) {
        Long customerId = (Long) session.getAttribute("customerId");

        if (customerId == null) {
            return ResponseEntity.status(401).body("Please login first");
        }

        return ResponseEntity.ok(cartService.viewCart(customerId));
    }

    // ✅ Remove product from cart
    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<String> removeFromCart(@PathVariable Long productId, HttpSession session) {
        Long customerId = (Long) session.getAttribute("customerId");

        if (customerId == null) {
            return ResponseEntity.status(401).body("Please login first");
        }

        cartService.removeFromCart(customerId, productId);
        return ResponseEntity.ok("Product removed from cart");
    }
}
