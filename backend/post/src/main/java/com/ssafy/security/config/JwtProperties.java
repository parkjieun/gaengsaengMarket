package com.ssafy.security.config;

import org.springframework.beans.factory.annotation.Value;

public class JwtProperties {

    public static final String SECRET = "secret0key0in0ssafyasdfasdfasdfasdfasdf";
    public static final int EXPIRATION_TIME = 864000000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
}
