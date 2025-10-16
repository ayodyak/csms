package org.computerspareparts.csms.Service;

import org.computerspareparts.csms.Entity.Cart;
import org.computerspareparts.csms.Entity.Customer;
import org.computerspareparts.csms.Entity.Product;
import org.computerspareparts.csms.Repository.CartRepository;
import org.computerspareparts.csms.Repository.CustomerRepository;
import org.computerspareparts.csms.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    // ✅ Add product to cart and save in DB
    public String addToCart(Long customerId, Long productId, int quantity) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getStock() < quantity) {
            return "Not enough stock available";
        }

        // Check if product already in cart → update quantity
        List<Cart> existingCart = cartRepository.findByCustomerCustomerId(customerId);
        for (Cart c : existingCart) {
            if (c.getProduct().getProductId().equals(productId)) {
                c.setQuantity(c.getQuantity() + quantity);
                cartRepository.save(c);
                return "Updated quantity in cart";
            }
        }

        // Add new cart item
        Cart cart = new Cart();
        cart.setCustomer(customer);
        cart.setProduct(product);
        cart.setQuantity(quantity);
        cartRepository.save(cart);
        return "Product added to cart";
    }

    // ✅ View cart items
    public List<Cart> viewCart(Long customerId) {
        return cartRepository.findByCustomerCustomerId(customerId);
    }

    // ✅ Remove product from cart
    public void removeFromCart(Long customerId, Long productId) {
        List<Cart> carts = cartRepository.findByCustomerCustomerId(customerId);
        for (Cart c : carts) {
            if (c.getProduct().getProductId().equals(productId)) {
                cartRepository.delete(c);
                break;
            }
        }
    }
}
