package org.computerspareparts.csms.Controller;

import org.computerspareparts.csms.Entity.Part;
import org.computerspareparts.csms.Repository.PartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parts")
public class PartController {
    @Autowired
    private PartRepository partRepository;

    @GetMapping("/{id}/inventory")
    public ResponseEntity<?> getInventory(@PathVariable Integer id) {
        return partRepository.findById(id)
            .map(part -> ResponseEntity.ok(new InventoryDTO(part.getStockQuantity(), part.getRecorderLevel())))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Part> getPart(@PathVariable Integer id) {
        return partRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    public static class InventoryDTO {
        public Integer stockQuantity;
        public Integer recorderLevel;
        public InventoryDTO(Integer stockQuantity, Integer recorderLevel) {
            this.stockQuantity = stockQuantity;
            this.recorderLevel = recorderLevel;
        }
    }
}
