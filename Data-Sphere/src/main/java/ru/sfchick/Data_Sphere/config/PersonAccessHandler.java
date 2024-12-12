package ru.sfchick.Data_Sphere.config;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ru.sfchick.Data_Sphere.security.PersonDetails;

@Component("personAccessHandler")
public class PersonAccessHandler {

    public boolean isVerified(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof PersonDetails) {
            return ((PersonDetails) principal).isVerified();
        }

        return false;
    }
}

