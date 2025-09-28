package org.computerspareparts.csms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RoleHomeController {
    @GetMapping("/sales/home")
    public String salesHome() {
        return "sales_home";
    }

    @GetMapping("/it/home")
    public String itHome() {
        return "it_home";
    }

    @GetMapping("/accountant/home")
    public String accountantHome() {
        return "accountant_home";
    }
}
