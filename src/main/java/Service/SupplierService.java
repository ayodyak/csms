package Service;


import Entity.Supplier;
import Entity.PurchaseRequest;
import Entity.PurchaseRequestDTO;
import Entity.Delivery;
import Entity.SupplierPayment;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import Repository.SupplierRepository;
import Repository.PurchaseRequestRepository;
import Repository.DeliveryRepository;
import Repository.SupplierPaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;
    private final PurchaseRequestRepository purchaseRequestRepository;
    private final DeliveryRepository deliveryRepository;
    private final SupplierPaymentRepository supplierPaymentRepository;

    public SupplierService(SupplierRepository supplierRepository,
                           PurchaseRequestRepository purchaseRequestRepository,
                           DeliveryRepository deliveryRepository,
                           SupplierPaymentRepository supplierPaymentRepository) {
        this.supplierRepository = supplierRepository;
        this.purchaseRequestRepository = purchaseRequestRepository;
        this.deliveryRepository = deliveryRepository;
        this.supplierPaymentRepository = supplierPaymentRepository;
    }

    // ----------------------------
    // CRUD for Supplier
    // ----------------------------
    public Supplier createSupplier(Supplier supplier) {
        // Prevent duplicate emails which violate unique constraint
        supplierRepository.findByEmail(supplier.getEmail()).ifPresent(s -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Supplier email already exists");
        });
        return supplierRepository.save(supplier);
    }

    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    public Supplier getSupplierById(Long id) {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Supplier not found"));
    }

    public Supplier updateSupplier(Long id, Supplier updatedSupplier) {
        Supplier supplier = getSupplierById(id);
        supplier.setName(updatedSupplier.getName());
        supplier.setEmail(updatedSupplier.getEmail());
        supplier.setContactNo(updatedSupplier.getContactNo());
        supplier.setAddress(updatedSupplier.getAddress());
        supplier.setPasswordHash(updatedSupplier.getPasswordHash());
        return supplierRepository.save(supplier);
    }

    public void deleteSupplier(Long id) {
        supplierRepository.deleteById(id);
    }

    // ----------------------------
    // CRUD for PurchaseRequest
    // ----------------------------
    public PurchaseRequest createPurchaseRequest(PurchaseRequest request) {
        return purchaseRequestRepository.save(request);
    }

    public PurchaseRequest getPurchaseRequestById(Long requestId) {
        return purchaseRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Purchase request not found"));
    }

    public List<PurchaseRequest> getAllRequests() {
        return purchaseRequestRepository.findAll();
    }

    public List<PurchaseRequestDTO> getAllRequestsDTO() {
        return purchaseRequestRepository.findAll().stream()
                .map(req -> new PurchaseRequestDTO(
                    req.getRequestId(),
                    req.getProduct().getProducts_id(),
                    req.getSupplier().getSupplierId(),
                    req.getQuantity(),
                    req.getStatus(),
                    req.getRequestDate().toString()
                ))
                .collect(java.util.stream.Collectors.toList());
    }

    public List<PurchaseRequest> getRequestsBySupplier(Long supplierId) {
        Supplier supplier = getSupplierById(supplierId);
        return purchaseRequestRepository.findBySupplier(supplier);
    }

    public PurchaseRequest updatePurchaseRequestStatus(Long requestId, String status) {
        PurchaseRequest request = purchaseRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        request.setStatus(status);
        return purchaseRequestRepository.save(request);
    }

    public void deletePurchaseRequest(Long requestId) {
        purchaseRequestRepository.deleteById(requestId);
    }

    // ----------------------------
    // CRUD for Delivery
    // ----------------------------
    public Delivery createDelivery(Delivery delivery) {
        if (delivery.getPurchaseRequest() == null || delivery.getPurchaseRequest().getRequestId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "purchaseRequest.requestId is required");
        }
        if (delivery.getSupplier() == null || delivery.getSupplier().getSupplierId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "supplier.supplierId is required");
        }

        PurchaseRequest persistedRequest = purchaseRequestRepository.findById(delivery.getPurchaseRequest().getRequestId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Purchase request not found"));
        Supplier persistedSupplier = getSupplierById(delivery.getSupplier().getSupplierId());

        delivery.setPurchaseRequest(persistedRequest);
        delivery.setSupplier(persistedSupplier);

        return deliveryRepository.save(delivery);
    }

    public List<Delivery> getAllDeliveries() {
        return deliveryRepository.findAll();
    }

    public List<Delivery> getDeliveriesBySupplier(Long supplierId) {
        Supplier supplier = getSupplierById(supplierId);
        return deliveryRepository.findBySupplier(supplier);
    }

    public Delivery updateDelivery(Long deliveryId, Delivery updatedDelivery) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new RuntimeException("Delivery not found"));

        delivery.setDeliveryDate(updatedDelivery.getDeliveryDate());
        delivery.setDeliveryTime(updatedDelivery.getDeliveryTime());
        delivery.setAddress(updatedDelivery.getAddress());
        delivery.setCost(updatedDelivery.getCost());
        delivery.setStatus(updatedDelivery.getStatus());

        return deliveryRepository.save(delivery);
    }

    public void deleteDelivery(Long deliveryId) {
        deliveryRepository.deleteById(deliveryId);
    }

    // ----------------------------
    // CRUD for SupplierPayment
    // ----------------------------
    public SupplierPayment createPayment(SupplierPayment payment) {
        return supplierPaymentRepository.save(payment);
    }

    public List<SupplierPayment> getAllPayments() {
        return supplierPaymentRepository.findAll();
    }

    public List<SupplierPayment> getPaymentsBySupplier(Long supplierId) {
        Supplier supplier = getSupplierById(supplierId);
        return supplierPaymentRepository.findBySupplier(supplier);
    }

    public SupplierPayment updatePaymentStatus(Long paymentId, String status) {
        SupplierPayment payment = supplierPaymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        payment.setStatus(status);
        return supplierPaymentRepository.save(payment);
    }

    public void deletePayment(Long paymentId) {
        supplierPaymentRepository.deleteById(paymentId);
    }

    // ----------------------------
    // Business Scenarios
    // ----------------------------

    // ✅ 1. Receive Purchase Requests
    public List<PurchaseRequest> receivePurchaseRequests(Long supplierId) {
        Supplier supplier = getSupplierById(supplierId);
        return purchaseRequestRepository.findBySupplierAndStatus(supplier, "Pending");
    }

    // ✅ 2. Manage Delivery
    public Delivery manageDelivery(Long deliveryId, Delivery updatedDelivery) {
        return updateDelivery(deliveryId, updatedDelivery);
    }

    // ✅ 3. Access Past Payments
    public List<SupplierPayment> getPastPayments(Long supplierId) {
        return getPaymentsBySupplier(supplierId);
    }
}
