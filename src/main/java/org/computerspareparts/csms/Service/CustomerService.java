package org.computerspareparts.csms.Service;

import org.computerspareparts.csms.Entity.Customer;
import org.computerspareparts.csms.Repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // 🔹 Sign Up
    public Customer registerCustomer(Customer customer) {
        if (customerRepository.findByEmail(customer.getEmail()) != null) {
            throw new RuntimeException("User Already Registered with this email!");
        }
        return customerRepository.save(customer);
    }

    // 🔹 Login
    public Customer login(String email, String nic) {
        Customer existing = customerRepository.findByEmail(email);
        if (existing == null || !existing.getNic().equals(nic)) {
            throw new RuntimeException("Invalid credentials!");
        }
        return existing;
    }

    // ✅ Basic CRUD (optional)
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
}
