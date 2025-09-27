package org.computerspareparts.csms.service;

import org.computerspareparts.csms.entity.PurchaseRequest;
import org.computerspareparts.csms.repository.PurchaseRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PurchaseRequestService {
    @Autowired
    private PurchaseRequestRepository purchaseRequestRepository;

    public List<PurchaseRequest> getAllRequests() {
        return purchaseRequestRepository.findAll();
    }

    public Optional<PurchaseRequest> getRequestById(Long id) {
        return purchaseRequestRepository.findById(id);
    }

    public PurchaseRequest saveRequest(PurchaseRequest request) {
        return purchaseRequestRepository.save(request);
    }

    public void deleteRequest(Long id) {
        purchaseRequestRepository.deleteById(id);
    }

    public List<PurchaseRequest> getRequestsByManagerId(Integer managerId) {
        return purchaseRequestRepository.findByManagerId(managerId);
    }
}
