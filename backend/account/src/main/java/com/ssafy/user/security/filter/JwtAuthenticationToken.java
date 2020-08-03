package com.ssafy.user.security.filter;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import com.ssafy.user.util.JwtUtil;

import io.jsonwebtoken.JwtParserBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtAuthenticationToken extends AbstractAuthenticationToken{
    private static final long serialVersionUID = 1L;
    private final Object principal;

    public JwtAuthenticationToken(String socialId) {
        super(null);
        
        super.setAuthenticated(true); // must use super, as we override
        this.principal=socialId;
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
