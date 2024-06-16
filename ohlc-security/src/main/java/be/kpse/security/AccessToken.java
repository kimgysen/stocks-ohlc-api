package be.kpse.security;

import lombok.Data;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
public class AccessToken {

    public static final String BEARER = "Bearer ";

    private final TokenManager tokenManager;
    private final String jwt;
    private final UserDetails userDetails;


    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userDetails.getAuthorities();
    }


}
