package org.computerspareparts.csms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SupplierLoginController {
    @GetMapping("/supplier/login")
    public String supplierLogin() {
        return "supplier_login";
    }
}

