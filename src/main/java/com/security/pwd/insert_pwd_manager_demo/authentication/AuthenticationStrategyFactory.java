package com.security.pwd.insert_pwd_manager_demo.authentication;

public interface AuthenticationStrategyFactory {
    AuthenticationStrategy getStrategy(String strategyName);
}
