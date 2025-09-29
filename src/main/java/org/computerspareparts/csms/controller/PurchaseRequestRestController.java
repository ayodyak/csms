package org.computerspareparts.csms.controller;

import org.computerspareparts.csms.entity.PurchaseRequest;
import org.computerspareparts.csms.entity.PurchaseRequestItem;
import org.computerspareparts.csms.repository.PurchaseRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/purchase-requests")
public class PurchaseRequestRestController {
    @Autowired
    private PurchaseRequestRepository purchaseRequestRepository;

    @GetMapping
    public List<PurchaseRequest> getAllRequests() {
        return purchaseRequestRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> createRequest(@RequestBody PurchaseRequest request) {
        final int MAX_OPEN_REQUESTS = 5; // Set your desired limit here
        if (request.getManagerId() == null) {
            return ResponseEntity.badRequest().body("Manager ID is required.");
        }
        long openRequests = purchaseRequestRepository.countOpenRequestsByManagerId(request.getManagerId());
        if (openRequests >= MAX_OPEN_REQUESTS) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Manager has reached the maximum number of open purchase requests.");
        }
        request.setRequestId(null);
        if (request.getItems() != null) {
            for (PurchaseRequestItem item : request.getItems()) {
                item.setPurchaseRequest(request); // Set parent reference
            }
        } else {
            request.setItems(new ArrayList<>());
        }
        return ResponseEntity.ok(purchaseRequestRepository.save(request));
    }

    @PutMapping("/{id}/status")
    public PurchaseRequest updateStatus(@PathVariable Long id, @RequestBody StatusUpdate statusUpdate) {
        PurchaseRequest req = purchaseRequestRepository.findById(id).orElse(null);
        if (req != null) {
            req.setStatus(statusUpdate.getStatus());
            return purchaseRequestRepository.save(req);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void deletePurchaseRequest(@PathVariable Long id) {
        purchaseRequestRepository.deleteById(id);
    }

    public static class StatusUpdate {
        private String status;
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}
