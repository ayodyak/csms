package org.computerspareparts.csms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.computerspareparts.csms.repository.PartRepository;
import org.computerspareparts.csms.entity.Part;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class ManagerHomeController {
    @Autowired
    private PartRepository partRepository;

    @GetMapping("/manager/home")
    public String home(Model model) {
        List<Part> lowStockParts = partRepository.findLowStockParts();
        model.addAttribute("lowStockParts", lowStockParts);
        return "manager_home";
    }
}
