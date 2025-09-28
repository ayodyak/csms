package org.computerspareparts.csms.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

public class RoleBasedAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String redirectUrl = "/login";
        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();
            if (role.equals("ROLE_MANAGER")) {
                redirectUrl = "/manager/home";
                break;
            } else if (role.equals("ROLE_SALES_STAFF")) {
                redirectUrl = "/sales/home";
                break;
            } else if (role.equals("ROLE_IT_TECHNICIAN")) {
                redirectUrl = "/it/home";
                break;
            } else if (role.equals("ROLE_ACCOUNTANT")) {
                redirectUrl = "/accountant/home";
                break;
            }
        }
        response.sendRedirect(redirectUrl);
    }
}
