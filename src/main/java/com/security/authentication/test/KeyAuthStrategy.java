package com.security.authentication.test;

public interface KeyAuthStrategy {
    String getAuthorizationCode(String appId) throws PasswordServiceException;
    String signAuthorizationCode(String authorizationCode, String privateKey) throws PasswordServiceException;
    TokenResult authenticate(String appId, String signedAuthorizationCode) throws PasswordServiceException;
}
