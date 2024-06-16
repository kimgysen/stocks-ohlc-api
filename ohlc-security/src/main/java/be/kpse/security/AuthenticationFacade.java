package be.kpse.security;

import java.util.UUID;
import org.springframework.security.core.Authentication;

/**
 * Provide a facade to easily inject the authentication object provided by Spring security
 */
public interface AuthenticationFacade {
    Authentication getAuthentication();
    UUID getAuthenticatedUserId();
}
