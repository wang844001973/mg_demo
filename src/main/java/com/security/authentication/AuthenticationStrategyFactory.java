package com.security.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationStrategyFactory {
    @Autowired
    @Qualifier("keyAuthenticationStrategy")
    private AuthenticationStrategy keyAuthenticationStrategy;
    @Autowired
    @Qualifier("certificateAuthenticationStrategy")
    private AuthenticationStrategy certificateAuthenticationStrategy;
    @Autowired
    @Qualifier("passwordAuthenticationStrategy")
    private AuthenticationStrategy passwordAuthenticationStrategy;
    public AuthenticationStrategy getStrategy(String strategyName){
        switch (strategyName){
            case "certificateAuthenticationStrategy":
                return certificateAuthenticationStrategy;
            case "keyAuthenticationStrategy":
                return keyAuthenticationStrategy;
            case "passwordAuthenticationStrategy":
                return passwordAuthenticationStrategy;
            default:
                throw new IllegalArgumentException("Invalid Authentication Strategy name: " + strategyName);
        }
    };
}
