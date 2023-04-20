package com.security.authentication.strategies;

import com.security.authentication.AuthenticationResult;
import com.security.authentication.AuthenticationStrategy;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service("passwordAuthenticationStrategy")
public class PasswordAuthenticationStrategy implements AuthenticationStrategy {

    @Override
    public AuthenticationResult authenticate(HttpServletRequest req, HttpServletResponse resp) {
        //todo 待实现
        return null;
    }
}