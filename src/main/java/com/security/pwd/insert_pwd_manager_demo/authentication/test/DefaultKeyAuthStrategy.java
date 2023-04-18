package com.security.pwd.insert_pwd_manager_demo.authentication.test;

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

public class DefaultKeyAuthStrategy implements KeyAuthStrategy {

    private static final String AUTHORIZATION_API_URL = "/auth/key/authorization-code";
    private static final String SIGN_API_URL = "/auth/key/sign";
    private static final String AUTHENTICATE_API_URL = "/auth/key/authenticate";

    private final RestTemplate restTemplate;
    private final RSAPublicKey publicKey;
    private final RSAPrivateKey privateKey;

    public DefaultKeyAuthStrategy(RestTemplate restTemplate, RSAPublicKey publicKey, RSAPrivateKey privateKey) {
        this.restTemplate = restTemplate;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    @Override
    public String getAuthorizationCode(String appId) throws PasswordServiceException {
        String url = AUTHORIZATION_API_URL + "?appId=" + appId;
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            throw new PasswordServiceException("Failed to get authorization code for appId " + appId);
        }
        return responseEntity.getBody();
    }

    @Override
    public String signAuthorizationCode(String authorizationCode, String privateKey) throws PasswordServiceException {
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(this.privateKey);
            signature.update(authorizationCode.getBytes());
            byte[] signatureBytes = signature.sign();
            byte[] encodedSignatureBytes = Base64.getEncoder().encode(signatureBytes);
            return new String(encodedSignatureBytes);
        } catch (Exception e) {
            throw new PasswordServiceException("Failed to sign authorization code: " + e.getMessage());
        }
    }

    @Override
    public TokenResult authenticate(String appId, String signedAuthorizationCode) throws PasswordServiceException {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setAppId(appId);
        authenticationRequest.setSignedAuthorizationCode(signedAuthorizationCode);
        HttpEntity<AuthenticationRequest> requestEntity = new HttpEntity<>(authenticationRequest, headers);
        return null;
    }
}