package org.computerspareparts.csms.Controller;

import jakarta.servlet.http.HttpSession;
import org.computerspareparts.csms.Entity.Customer;
import org.computerspareparts.csms.Repository.CustomerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class LoginController {

    private final CustomerRepository customerRepository;

    public LoginController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email,
                                        @RequestParam String password,
                                        HttpSession session) {

        Customer customer = customerRepository.findByEmail(email);

        if (customer == null) {
            return ResponseEntity.badRequest().body("No account found with this email!");
        }

        if (!customer.getPassword().equals(password)) {
            return ResponseEntity.badRequest().body("Incorrect password!");
        }

        // ✅ Store user session
        session.setAttribute("customerId", customer.getCustomerId());
        session.setAttribute("customerEmail", customer.getEmail());

        return ResponseEntity.ok("success");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logged out successfully!");
    }
}
