package org.computerspareparts.csms.controller;

import org.computerspareparts.csms.entity.Part;
import org.computerspareparts.csms.repository.PartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@Controller
@RequestMapping("/manager/part")
public class PartController {
    @Autowired
    private PartRepository partRepository;

    // List all parts (redirect to inventory)
    @GetMapping("/list")
    public String listParts(Model model) {
        model.addAttribute("parts", partRepository.findAll());
        return "inventory";
    }

    // Show add part form
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("part", new Part());
        return "add_part";
    }

    // Handle add part submission
    @PostMapping("/add")
    public String addPart(@ModelAttribute Part part) {
        partRepository.save(part);
        return "redirect:/manager/inventory";
    }

    // Show edit part form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model) {
        Optional<Part> partOpt = partRepository.findById(id);
        if (partOpt.isPresent()) {
            model.addAttribute("part", partOpt.get());
            return "edit_part";
        } else {
            return "redirect:/manager/inventory";
        }
    }

    // Handle edit part submission
    @PostMapping("/edit/{id}")
    public String editPart(@PathVariable("id") Integer id, @ModelAttribute Part part) {
        part.setPartId(id);
        partRepository.save(part);
        return "redirect:/manager/inventory";
    }

    // Delete part
    @PostMapping("/delete/{id}")
    public String deletePart(@PathVariable("id") Integer id) {
        partRepository.deleteById(id);
        return "redirect:/manager/inventory";
    }
}

