package com.security.authentication;

import java.util.Map;

public interface AuthenticationStrategy {
    AuthenticationResult authenticate(Map<String ,String> credentials);
}