package org.computerspareparts.csms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ManagerHomeController {
    @GetMapping("/manager/home")
    public String home() {
        return "manager_home";
    }
}

