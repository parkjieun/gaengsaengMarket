package com.ssafy.chat.security.filter;


import org.springframework.security.authentication.AbstractAuthenticationToken;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtAuthenticationToken extends AbstractAuthenticationToken{
    private static final long serialVersionUID = 1L;
    private final Object principal;

    public JwtAuthenticationToken(String userId) {
        super(null);
        
        super.setAuthenticated(true); // must use super, as we override
        this.principal=userId;
    }
 
    @Override
    public Object getCredentials() {
        return "";
    }
 
    @Override
    public Object getPrincipal() {
        return principal;
    }

}
