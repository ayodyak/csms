package org.computerspareparts.csms.Controller;

import org.computerspareparts.csms.Entity.CustomerOrder;
import org.computerspareparts.csms.Service.CustomerOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer-orders")
public class CustomerOrderController {
    private final CustomerOrderService customerOrderService;

    @Autowired
    public CustomerOrderController(CustomerOrderService customerOrderService) {
        this.customerOrderService = customerOrderService;
    }

    // List all customer orders
    @GetMapping
    public ResponseEntity<List<CustomerOrder>> getAllOrders() {
        return ResponseEntity.ok(customerOrderService.getAllOrders());
    }

    // Get order by ID
    @GetMapping("/{orderId}")
    public ResponseEntity<CustomerOrder> getOrderById(@PathVariable Long orderId) {
        return ResponseEntity.ok(customerOrderService.getOrderById(orderId));
    }

    // Get orders by customer
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<CustomerOrder>> getOrdersByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(customerOrderService.getOrdersByCustomer(customerId));
    }

    // Place a new order for a customer
    @PostMapping("/place")
    public ResponseEntity<CustomerOrder> placeOrder(@RequestParam Long customerId, @RequestParam String status) {
        CustomerOrder order = customerOrderService.placeOrder(customerId, status);
        return ResponseEntity.ok(order);
    }

    // Update order status
    @PutMapping("/{orderId}/status")
    public ResponseEntity<CustomerOrder> updateOrderStatus(@PathVariable Long orderId, @RequestParam String status) {
        CustomerOrder updatedOrder = customerOrderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(updatedOrder);
    }

    // Delete order
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
        customerOrderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }

}
