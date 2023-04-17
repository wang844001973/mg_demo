package com.security.pwd.insert_pwd_manager_demo.authentication.Impl;

import com.security.pwd.insert_pwd_manager_demo.authentication.AuthenticationResult;
import com.security.pwd.insert_pwd_manager_demo.authentication.AuthenticationService;
import com.security.pwd.insert_pwd_manager_demo.authentication.AuthenticationStrategy;
import com.security.pwd.insert_pwd_manager_demo.authentication.AuthenticationStrategyFactory;

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