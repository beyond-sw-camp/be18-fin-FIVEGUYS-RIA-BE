package com.fiveguys.RIA.RIA_Backend.auth.token;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class UserAuthenticationToken extends UsernamePasswordAuthenticationToken {

    public UserAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public UserAuthenticationToken(Object principal, Object credentials,
                                   Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}
