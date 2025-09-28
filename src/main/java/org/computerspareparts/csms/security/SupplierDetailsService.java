package org.computerspareparts.csms.security;

import org.computerspareparts.csms.entity.Supplier;
import org.computerspareparts.csms.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SupplierDetailsService implements UserDetailsService {
    @Autowired
    private SupplierRepository supplierRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Supplier supplier = supplierRepository.findAll().stream()
                .filter(s -> email.equals(s.getEmail()))
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("Supplier not found"));
        return org.springframework.security.core.userdetails.User
                .withUsername(supplier.getEmail())
                .password(supplier.getPassword())
                .roles("SUPPLIER")
                .build();
    }
}
