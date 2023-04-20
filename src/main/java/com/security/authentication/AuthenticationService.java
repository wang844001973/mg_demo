package com.security.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AuthenticationService {
    AuthenticationResult authenticate(String strategyName, HttpServletRequest request, HttpServletResponse response);

}
