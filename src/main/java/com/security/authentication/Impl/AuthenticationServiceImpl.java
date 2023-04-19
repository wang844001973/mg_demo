package com.security.authentication.Impl;

import com.security.authentication.AuthenticationResult;
import com.security.authentication.AuthenticationService;
import com.security.authentication.AuthenticationStrategy;
import com.security.authentication.AuthenticationStrategyFactory;

import java.util.Map;

public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationStrategyFactory strategyFactory;

    public AuthenticationServiceImpl(AuthenticationStrategyFactory strategyFactory) {
        this.strategyFactory = strategyFactory;
    }

    @Override
    public AuthenticationResult authenticate(String strategyName, Map<String, String> credentials) {
        AuthenticationStrategy strategy = strategyFactory.getStrategy(strategyName);
        return strategy.authenticate(credentials);
    }
}