package org.computerspareparts.csms.Controller;

import org.computerspareparts.csms.Entity.Customer;
import org.computerspareparts.csms.Repository.CustomerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class RegisterController {

    private final CustomerRepository customerRepository;

    public RegisterController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestParam String firstName,
                                               @RequestParam String lastName,
                                               @RequestParam String gender,
                                               @RequestParam String nic,
                                               @RequestParam String address,
                                               @RequestParam String contactNo,
                                               @RequestParam String email,
                                               @RequestParam String password) {

        // Check for duplicate email or NIC
        if (customerRepository.findByEmail(email) != null) {
            return ResponseEntity.badRequest().body("Email already exists!");
        }
        if (customerRepository.findByNic(nic) != null) {
            return ResponseEntity.badRequest().body("NIC already exists!");
        }

        // Save new customer
        Customer customer = new Customer();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setGender(gender);
        customer.setNic(nic);
        customer.setAddress(address);
        customer.setContactNo(contactNo);
        customer.setEmail(email);
        customer.setPassword(password); // later you should encrypt

        customerRepository.save(customer);

        return ResponseEntity.ok("success");
    }
}
