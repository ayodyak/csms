package org.computerspareparts.csms.service;

import org.computerspareparts.csms.entity.PurchaseRequestItem;
import org.computerspareparts.csms.repository.PurchaseRequestItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseRequestItemService {
    @Autowired
    private PurchaseRequestItemRepository purchaseRequestItemRepository;

    public List<PurchaseRequestItem> getAllItems() {
        return purchaseRequestItemRepository.findAll();
    }

    public PurchaseRequestItem saveItem(PurchaseRequestItem item) {
        return purchaseRequestItemRepository.save(item);
    }

    public void deleteItem(Long id) {
        purchaseRequestItemRepository.deleteById(id);
    }
}

