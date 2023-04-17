package com.security.pwd.insert_pwd_manager_demo.authentication;

import java.util.Map;

public interface AuthenticationStrategy {
    AuthenticationResult authenticate(Map<String ,String> credentials);
}