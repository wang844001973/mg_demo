package com.security.pwd.insert_pwd_manager_demo.authentication.test;

public interface KeyAuthStrategy extends PasswordStrategy {
    String getAuthorizationCode(String appId) throws PasswordServiceException;
    String signAuthorizationCode(String authorizationCode, String privateKey) throws PasswordServiceException;
    TokenResult authenticate(String appId, String signedAuthorizationCode) throws PasswordServiceException;
}
