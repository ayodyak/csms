package Controller;

import Service.SupplierService;
import Entity.Supplier;
import Entity.PurchaseRequest;
import Entity.PurchaseRequestDTO;
import Entity.Delivery;
import Entity.SupplierPayment;
import Service.SupplierService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    // ----------------------------
    // Supplier CRUD
    // ----------------------------

    @PostMapping
    public ResponseEntity<Supplier> createSupplier(@RequestBody Supplier supplier) {
        return ResponseEntity.ok(supplierService.createSupplier(supplier));
    }

    @GetMapping
    public ResponseEntity<List<Supplier>> getAllSuppliers() {
        return ResponseEntity.ok(supplierService.getAllSuppliers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Supplier> getSupplierById(@PathVariable Long id) {
        return ResponseEntity.ok(supplierService.getSupplierById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Supplier> updateSupplier(@PathVariable Long id, @RequestBody Supplier supplier) {
        return ResponseEntity.ok(supplierService.updateSupplier(id, supplier));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
        return ResponseEntity.noContent().build();
    }

    // ----------------------------
    // Purchase Requests
    // ----------------------------

    @GetMapping("/requests")
    public ResponseEntity<List<PurchaseRequestDTO>> getAllRequests() {
        return ResponseEntity.ok(supplierService.getAllRequestsDTO());
    }

    @PostMapping("/requests")
    public ResponseEntity<PurchaseRequest> createPurchaseRequest(@RequestBody PurchaseRequest request) {
        return ResponseEntity.ok(supplierService.createPurchaseRequest(request));
    }

    @GetMapping("/{supplierId}/requests")
    public ResponseEntity<List<PurchaseRequest>> getRequestsBySupplier(@PathVariable Long supplierId) {
        return ResponseEntity.ok(supplierService.getRequestsBySupplier(supplierId));
    }

    @GetMapping("/requests/{requestId}")
    public ResponseEntity<PurchaseRequest> getRequestById(@PathVariable Long requestId) {
        return ResponseEntity.ok(supplierService.getPurchaseRequestById(requestId));
    }

    @PutMapping("/requests/{requestId}/status")
    public ResponseEntity<PurchaseRequest> updateRequestStatus(@PathVariable Long requestId,
                                                               @RequestParam String status) {
        return ResponseEntity.ok(supplierService.updatePurchaseRequestStatus(requestId, status));
    }

    @DeleteMapping("/requests/{requestId}")
    public ResponseEntity<Void> deletePurchaseRequest(@PathVariable Long requestId) {
        supplierService.deletePurchaseRequest(requestId);
        return ResponseEntity.noContent().build();
    }

    // ----------------------------
    // Deliveries
    // ----------------------------

    @GetMapping("/deliveries")
    public ResponseEntity<List<Delivery>> getAllDeliveries() {
        return ResponseEntity.ok(supplierService.getAllDeliveries());
    }

    @PostMapping("/deliveries")
    public ResponseEntity<Delivery> createDelivery(@RequestBody Delivery delivery) {
        return ResponseEntity.ok(supplierService.createDelivery(delivery));
    }

    @GetMapping("/{supplierId}/deliveries")
    public ResponseEntity<List<Delivery>> getDeliveriesBySupplier(@PathVariable Long supplierId) {
        return ResponseEntity.ok(supplierService.getDeliveriesBySupplier(supplierId));
    }

    @PutMapping("/deliveries/{deliveryId}")
    public ResponseEntity<Delivery> updateDelivery(@PathVariable Long deliveryId, @RequestBody Delivery delivery) {
        return ResponseEntity.ok(supplierService.updateDelivery(deliveryId, delivery));
    }

    @DeleteMapping("/deliveries/{deliveryId}")
    public ResponseEntity<Void> deleteDelivery(@PathVariable Long deliveryId) {
        supplierService.deleteDelivery(deliveryId);
        return ResponseEntity.noContent().build();
    }

    // ----------------------------
    // Payments
    // ----------------------------

    @GetMapping("/payments")
    public ResponseEntity<List<SupplierPayment>> getAllPayments() {
        return ResponseEntity.ok(supplierService.getAllPayments());
    }

    @PostMapping("/payments")
    public ResponseEntity<SupplierPayment> createPayment(@RequestBody SupplierPayment payment) {
        return ResponseEntity.ok(supplierService.createPayment(payment));
    }

    @GetMapping("/{supplierId}/payments")
    public ResponseEntity<List<SupplierPayment>> getPaymentsBySupplier(@PathVariable Long supplierId) {
        return ResponseEntity.ok(supplierService.getPaymentsBySupplier(supplierId));
    }

    @PutMapping("/payments/{paymentId}/status")
    public ResponseEntity<SupplierPayment> updatePaymentStatus(@PathVariable Long paymentId,
                                                               @RequestParam String status) {
        return ResponseEntity.ok(supplierService.updatePaymentStatus(paymentId, status));
    }

    @DeleteMapping("/payments/{paymentId}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long paymentId) {
        supplierService.deletePayment(paymentId);
        return ResponseEntity.noContent().build();
    }

    // ----------------------------
    // Business Scenarios
    // ----------------------------

    // ✅ 1. Supplier receives purchase requests
    @GetMapping("/{supplierId}/requests/pending")
    public ResponseEntity<List<PurchaseRequest>> receivePurchaseRequests(@PathVariable Long supplierId) {
        return ResponseEntity.ok(supplierService.receivePurchaseRequests(supplierId));
    }

    // ✅ 2. Supplier manages delivery
    @PutMapping("/deliveries/{deliveryId}/manage")
    public ResponseEntity<Delivery> manageDelivery(@PathVariable Long deliveryId, @RequestBody Delivery delivery) {
        return ResponseEntity.ok(supplierService.manageDelivery(deliveryId, delivery));
    }

    // ✅ 3. Supplier views past payments
    @GetMapping("/{supplierId}/payments/past")
    public ResponseEntity<List<SupplierPayment>> getPastPayments(@PathVariable Long supplierId) {
        return ResponseEntity.ok(supplierService.getPastPayments(supplierId));
    }
}
