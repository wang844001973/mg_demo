package com.security.pwd.insert_pwd_manager_demo.authentication;

import java.util.Map;

public interface AuthenticationService {
    AuthenticationResult authenticate(String strategyName, Map<String, String> credentials);

}
