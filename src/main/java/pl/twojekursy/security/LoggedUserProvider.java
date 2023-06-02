package pl.twojekursy.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pl.twojekursy.user.User;

@Component
public class LoggedUserProvider {
    public User provideLoggedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        User user = (User) principal;
        return user;
    }
}
