package com.security.pwd.insert_pwd_manager_demo.authentication.strategies;

import com.security.pwd.insert_pwd_manager_demo.authentication.AuthenticationResult;
import com.security.pwd.insert_pwd_manager_demo.authentication.AuthenticationStrategy;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("keyAuthenticationStrategy")
public class KeyAuthenticationStrategy implements AuthenticationStrategy {

    @Override
    public AuthenticationResult authenticate(Map<String, String> credentials) {
        return null;
    }

}