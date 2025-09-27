package org.computerspareparts.csms.controller;

import org.computerspareparts.csms.entity.Part;
import org.computerspareparts.csms.repository.PartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parts")
public class PartsRestController {
    @Autowired
    private PartRepository partRepository;

    @GetMapping
    public List<Part> getAllParts() {
        return partRepository.findAll();
    }

    @GetMapping("/low-stock")
    public List<Part> getLowStockParts() {
        return partRepository.findAll().stream()
            .filter(p -> p.getStockLevel() != null && p.getReorderLevel() != null && p.getStockLevel() <= p.getReorderLevel())
            .toList();
    }

    @GetMapping("/{id}")
    public Part getPart(@PathVariable Integer id) {
        return partRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Part addPart(@RequestBody Part part) {
        part.setPartId(null);
        return partRepository.save(part);
    }

    @PutMapping("/{id}")
    public Part updatePart(@PathVariable Integer id, @RequestBody Part part) {
        part.setPartId(id);
        return partRepository.save(part);
    }

    @DeleteMapping("/{id}")
    public void deletePart(@PathVariable Integer id) {
        partRepository.deleteById(id);
    }
}

