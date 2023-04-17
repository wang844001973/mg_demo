package com.security.pwd.insert_pwd_manager_demo.authentication.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.HashMap;
import java.util.Map;

@Service
public class DefaultKeyAuthenticationStrategy implements KeyAuthStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultKeyAuthenticationStrategy.class);

    @Autowired
    private HttpUtils httpUtils;

    @Value("${password-service.auth.server.url}")
    private String authServerUrl;

    @Value("${password-service.auth.server.key.endpoint}")
    private String authServerKeyEndpoint;

    @Value("${password-service.auth.server.auth.endpoint}")
    private String authServerAuthEndpoint;

    @Override
    public String getAccessToken(String appId, String appPrivateKey) {
        // Step 1: Get authorization code
        String authCodeEndpoint = authServerUrl + authServerKeyEndpoint;
        String authCodeUrl = String.format("%s?appId=%s", authCodeEndpoint, appId);
        ResponseEntity<String> authCodeResponse = httpUtils.get(authCodeUrl,new HttpHeaders());
        if (authCodeResponse.getStatusCode() != HttpStatus.OK) {
            LOGGER.error("Failed to get authorization code from auth server. Response: {}", authCodeResponse);
            throw new RuntimeException("Failed to get authorization code from auth server");
        }
        String authCode = authCodeResponse.getBody();
        LOGGER.info("Got authorization code: {}", authCode);

        // Step 2: Sign the authorization code with the application private key
        String signedAuthCode = signWithPrivateKey(authCode, appPrivateKey);
        LOGGER.info("Signed authorization code: {}", signedAuthCode);

        // Step 3: Authenticate the application identity and get access token
        String authEndpoint = authServerUrl + authServerAuthEndpoint;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("appId", appId);
        requestBody.put("signedAuthCode", signedAuthCode);
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> authResponse = httpUtils.post(authEndpoint, requestEntity);
        if (authResponse.getStatusCode() != HttpStatus.OK) {
            LOGGER.error("Failed to authenticate application identity. Response: {}", authResponse);
            throw new RuntimeException("Failed to authenticate application identity");
        }
        String accessToken = authResponse.getBody();
        LOGGER.info("Got access token: {}", accessToken);
        return accessToken;
    }

    private String signWithPrivateKey(String data, String privateKey) {
        // TODO: Implement signing with private key
        return data;
    }

    @Override
    public String authenticate(String appId, Map<String, String> params) throws AuthenticationException {
        return null;
    }

    @Override
    public String getAuthorizationCode(String appId) throws PasswordServiceException {
        return null;
    }

    @Override
    public String signAuthorizationCode(String authorizationCode, String privateKey) throws PasswordServiceException {
        return null;
    }

    @Override
    public TokenResult authenticate(String appId, String signedAuthorizationCode) throws PasswordServiceException {
        return null;
    }
}
