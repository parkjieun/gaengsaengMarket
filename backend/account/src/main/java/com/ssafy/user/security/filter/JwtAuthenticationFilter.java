package com.ssafy.user.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ssafy.user.model.User;
import com.ssafy.user.repository.UserRepository;
import com.ssafy.user.security.config.JwtProperties;
import com.ssafy.user.util.JwtUtil;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
@Slf4j
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    private JwtUtil jwtUtil;
    
    private UserRepository userRepository;
    
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        super(authenticationManager);
        this.jwtUtil =jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws IOException, ServletException
    {
        String header = request.getHeader(JwtProperties.HEADER_STRING);

        // If header does not contain BEARER or is null delegate to Spring impl and exit
        if(header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)){
            // rest of the spring pipeline
            chain.doFilter(request, response);
            return;
        }
        Authentication authentication = getAuthentication(request);
        if(authentication != null){
            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(authentication);
        }
        
        chain.doFilter(request, response);
        
    }

    private Authentication getAuthentication(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        
        if(token == null) {
            return null;
        }
        if(token != null){
            // parse the token and validate it (decode)
            String socialId = JWT.require(Algorithm.HMAC256(JwtProperties.SECRET.getBytes()))
                    .build()
                    .verify(token.replace(JwtProperties.TOKEN_PREFIX,""))
                    .getClaim("socialId").asString();
            
            if(socialId != null){
                return new JwtAuthenticationToken(jwtUtil.getClaims(token.substring("Bearer ".length())));
            }
            
            return null;
        }
        return null;
        
    }
}