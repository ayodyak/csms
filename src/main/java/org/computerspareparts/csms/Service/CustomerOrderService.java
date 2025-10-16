package org.computerspareparts.csms.Service;

import org.computerspareparts.csms.Entity.Product;
import org.computerspareparts.csms.Entity.Cart;
import org.computerspareparts.csms.Entity.Customer;
import org.computerspareparts.csms.Entity.CustomerOrder;
import org.computerspareparts.csms.Repository.CustomerOrderRepository;
import org.computerspareparts.csms.Repository.CustomerRepository;
import org.computerspareparts.csms.Repository.CartRepository;
import org.computerspareparts.csms.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CustomerOrderService {
    private final CustomerOrderRepository customerOrderRepository;
    private final CustomerRepository customerRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CustomerOrderService(CustomerOrderRepository customerOrderRepository,
                                CustomerRepository customerRepository,
                                CartRepository cartRepository,
                                ProductRepository productRepository) {
        this.customerOrderRepository = customerOrderRepository;
        this.customerRepository = customerRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    // ✅ Place an order (checkout)
    public CustomerOrder placeOrder(Long customerId, String status) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // 1. Get cart items
        List<Cart> cartItems = cartRepository.findByCustomerCustomerId(customerId);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty!");
        }

        // 2. Create new order
        CustomerOrder order = new CustomerOrder();
        order.setCustomer(customer);
        order.setOrderDate(LocalDate.now());
        order.setStatus(status);

        CustomerOrder savedOrder = customerOrderRepository.save(order);

        // 3. Deduct stock for each product in cart
        for (Cart cartItem : cartItems) {
            Product product = cartItem.getProduct();
            if (product.getStock() < cartItem.getQuantity()) {
                throw new RuntimeException("Not enough stock for: " + product.getProductName());
            }
            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);
        }

        // 4. Clear cart
        cartRepository.deleteAll(cartItems);

        return savedOrder;
    }

    // ✅ Get all orders
    public List<CustomerOrder> getAllOrders() {
        return customerOrderRepository.findAll();
    }

    // ✅ Get order by ID
    public CustomerOrder getOrderById(Long id) {
        return customerOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    // ✅ Get orders by customer
    public List<CustomerOrder> getOrdersByCustomer(Long customerId) {
        return customerOrderRepository.findByCustomerCustomerId(customerId);
    }

    // ✅ Update order status
    public CustomerOrder updateOrderStatus(Long id, String status) {
        CustomerOrder order = customerOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        return customerOrderRepository.save(order);
    }

    // ✅ Delete order
    public void deleteOrder(Long id) {
        customerOrderRepository.deleteById(id);
    }
}
