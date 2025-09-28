package org.computerspareparts.csms.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private EmployeeDetailsService employeeDetailsService;

    @Autowired
    private SupplierDetailsService supplierDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(employeeDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public DaoAuthenticationProvider supplierAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(supplierDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new RoleBasedAuthenticationSuccessHandler();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authenticationProvider(authenticationProvider())
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/styles.css", "/script.js").permitAll()
                .requestMatchers("/manager/home").hasRole("MANAGER")
                .requestMatchers("/sales/home").hasRole("SALES_STAFF")
                .requestMatchers("/it/home").hasRole("IT_TECHNICIAN")
                .requestMatchers("/accountant/home").hasRole("ACCOUNTANT")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .successHandler(customAuthenticationSuccessHandler())
                .permitAll()
            )
            .logout(logout -> logout.permitAll());

        return http.build();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain supplierFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/supplier/**", "/supplier/login")
            .authenticationProvider(supplierAuthenticationProvider())
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/supplier/login", "/styles.css", "/script.js").permitAll()
                .requestMatchers("/supplier/home").hasRole("SUPPLIER")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/supplier/login")
                .defaultSuccessUrl("/supplier/home", true)
                .permitAll()
            )
            .logout(logout -> logout.permitAll());
        return http.build();
    }
}
