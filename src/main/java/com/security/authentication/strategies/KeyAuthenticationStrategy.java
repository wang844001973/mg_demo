package com.security.authentication.strategies;

import com.security.authentication.AuthenticationResult;
import com.security.authentication.AuthenticationStrategy;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("keyAuthenticationStrategy")
public class KeyAuthenticationStrategy implements AuthenticationStrategy {

    @Override
    public AuthenticationResult authenticate(Map<String, String> credentials) {
        return null;
    }

}