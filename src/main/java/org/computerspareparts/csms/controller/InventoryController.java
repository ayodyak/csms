package org.computerspareparts.csms.controller;

import org.computerspareparts.csms.entity.Part;
import org.computerspareparts.csms.repository.PartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class InventoryController {
    @Autowired
    private PartRepository partRepository;

    @GetMapping("/manager/inventory")
    public String showInventory(Model model) {
        List<Part> parts = partRepository.findAll();
        model.addAttribute("parts", parts);
        return "inventory";
    }
}

