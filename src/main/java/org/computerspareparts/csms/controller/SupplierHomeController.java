package org.computerspareparts.csms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SupplierHomeController {
    @GetMapping("/supplier/home")
    public String supplierHome() {
        return "supplier_home";
    }
}

