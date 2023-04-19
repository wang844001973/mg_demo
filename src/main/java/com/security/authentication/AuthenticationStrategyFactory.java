package com.security.authentication;

public interface AuthenticationStrategyFactory {
    AuthenticationStrategy getStrategy(String strategyName);
}
