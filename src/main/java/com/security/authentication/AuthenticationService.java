package com.security.authentication;

import java.util.Map;

public interface AuthenticationService {
    AuthenticationResult authenticate(String strategyName, Map<String, String> credentials);

}
