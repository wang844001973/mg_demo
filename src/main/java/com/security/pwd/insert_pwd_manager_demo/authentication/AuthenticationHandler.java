package com.security.pwd.insert_pwd_manager_demo.authentication;

import java.security.cert.X509Certificate;

public interface AuthenticationHandler {
    AuthenticationResult authenticateWithPassword(String username, String password);
    AuthenticationResult authenticateWithCertificate(X509Certificate certificate);
    AuthenticationResult authenticateWithKey(String username, String key);
}
