package com.ssafy.security.config;

import java.util.LinkedHashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.ssafy.security.filter.JwtAuthenticationFilter;
import com.ssafy.util.JwtUtil;
import javax.servlet.Filter;
import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	@Value("${jwt.secret}")
	private String secret;
	
    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }
	@Override
	public void configure(HttpSecurity http) throws Exception{
		Filter filter = new JwtAuthenticationFilter(authenticationManager(), jwtUtil());
		
		http.csrf().disable()
		.formLogin().disable()
		.addFilter(filter)
		.authorizeRequests()
		.antMatchers("/oauth/**").permitAll()
		.antMatchers("/resources/upload/*").permitAll()
		.antMatchers("/static/image/*").permitAll()
		.antMatchers(HttpMethod.GET,"/api/user/").permitAll()
		.antMatchers(HttpMethod.GET,"/**").permitAll()
		.anyRequest().authenticated()
		.and()
		.cors()
		.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	@Bean
	public JwtUtil jwtUtil() {
		return new JwtUtil(secret);
	}
}
