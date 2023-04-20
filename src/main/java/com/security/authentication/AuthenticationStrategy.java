package com.security.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AuthenticationStrategy {

    AuthenticationResult authenticate(HttpServletRequest req, HttpServletResponse resp);
}