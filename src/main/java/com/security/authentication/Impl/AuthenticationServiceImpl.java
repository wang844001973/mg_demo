package com.security.authentication.Impl;

import com.security.authentication.AuthenticationResult;
import com.security.authentication.AuthenticationService;
import com.security.authentication.AuthenticationStrategy;
import com.security.authentication.AuthenticationStrategyFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationStrategyFactory strategyFactory;

    public AuthenticationServiceImpl(AuthenticationStrategyFactory strategyFactory) {
        this.strategyFactory = strategyFactory;
    }

    @Override
    public AuthenticationResult authenticate(String strategyName, HttpServletRequest request, HttpServletResponse response) {
        AuthenticationStrategy strategy = strategyFactory.getStrategy(strategyName);
        return strategy.authenticate(request,response);
    }
}