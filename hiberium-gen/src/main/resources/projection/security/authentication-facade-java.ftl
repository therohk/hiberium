package ${package_base}.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade {

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public User getPrincipalUser() {
        return (User) getAuthentication().getPrincipal();
    }

    public String getUsername() {
        return getPrincipalUser().getUsername();
    }

    public boolean isAuthenticated() {
        return getAuthentication().isAuthenticated();
    }
}