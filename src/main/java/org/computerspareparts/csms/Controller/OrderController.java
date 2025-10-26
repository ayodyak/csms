package org.computerspareparts.csms.Controller;

import org.computerspareparts.csms.Controller.dto.OrderDTO;
import org.computerspareparts.csms.Entity.Order;
import org.computerspareparts.csms.Entity.OrderItem;
import org.computerspareparts.csms.Entity.Part;
import org.computerspareparts.csms.Service.OrderService;
import org.computerspareparts.csms.Repository.OrderRepository;
import org.computerspareparts.csms.Repository.PartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PartRepository partRepository;

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Integer id) {
        Optional<Order> order = orderService.getOrderById(id);
        return order.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        return orderService.createOrder(order);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Integer id, @RequestBody Order orderDetails) {
        Order updatedOrder = orderService.updateOrder(id, orderDetails);
        if (updatedOrder != null) {
            return ResponseEntity.ok(updatedOrder);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Integer id) {
        if (orderService.deleteOrder(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> changeOrderStatus(@PathVariable Integer id, @RequestParam String status) {
        Optional<Order> orderOpt = orderService.getOrderById(id);
        if (orderOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Order order = orderOpt.get();
        String previousStatus = order.getStatus();
        boolean statusChanged = !status.equals(previousStatus);
        // If status is being changed to Cancelled and was not previously Cancelled, restock parts
        if (statusChanged && "Cancelled".equalsIgnoreCase(status) && !"Cancelled".equalsIgnoreCase(previousStatus)) {
            if (order.getOrderItems() != null) {
                for (OrderItem item : order.getOrderItems()) {
                    if (item.getPartId() != null && item.getQuantity() != null) {
                        partRepository.findById(item.getPartId()).ifPresent(part -> {
                            int newStock = (part.getStockQuantity() != null ? part.getStockQuantity() : 0) + item.getQuantity();
                            part.setStockQuantity(newStock);
                            partRepository.save(part);
                        });
                    }
                }
            }
        }
        // Change status as usual
        if (orderService.changeOrderStatus(id, status)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/dto")
    public List<OrderDTO> getAllOrderDTOs() {
        List<Order> orders = orderService.getAllOrders();
        List<OrderDTO> dtos = new java.util.ArrayList<>();
        for (Order order : orders) {
            OrderDTO dto = new OrderDTO();
            dto.orderId = order.getOrderId();
            dto.customerName = order.getCustomer() != null ? order.getCustomer().getName() : "";
            dto.employeeName = order.getEmployee() != null ? order.getEmployee().getName() : "";
            dto.orderDate = order.getOrderDate();
            dto.status = order.getStatus();
            dto.totalAmount = order.getTotalAmount();
            dto.items = new java.util.ArrayList<>();
            if (order.getOrderItems() != null) {
                for (OrderItem item : order.getOrderItems()) {
                    OrderDTO.OrderItemDTO itemDTO = new OrderDTO.OrderItemDTO();
                    itemDTO.partId = item.getPartId();
                    Part part = item.getPartId() != null ? partRepository.findById(item.getPartId()).orElse(null) : null;
                    itemDTO.partName = part != null ? part.getName() : "";
                    itemDTO.quantity = item.getQuantity();
                    itemDTO.price = item.getPrice();
                    dto.items.add(itemDTO);
                }
            }
            dtos.add(dto);
        }
        return dtos;
    }

    @PostMapping("/{orderId}/reduce-stock")
    public ResponseEntity<?> reduceStock(@PathVariable Integer orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null || order.getOrderItems() == null) {
            return ResponseEntity.badRequest().body("Order not found or has no items");
        }
        for (OrderItem item : order.getOrderItems()) {
            if (item.getPartId() != null && item.getQuantity() != null) {
                partRepository.findById(item.getPartId()).ifPresent(part -> {
                    int newStock = (part.getStockQuantity() != null ? part.getStockQuantity() : 0) - item.getQuantity();
                    part.setStockQuantity(Math.max(newStock, 0));
                    partRepository.save(part);
                });
            }
        }
        return ResponseEntity.ok("Stock updated");
    }
}
