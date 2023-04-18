package com.security.pwd.insert_pwd_manager_demo.authentication.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class DefaultKeyAuthStrategy implements KeyAuthStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultKeyAuthStrategy.class);

    @Autowired
    private HttpUtils httpUtils;

    @Value("${password-service.auth.server.url}")
    private String authServerUrl;

    @Value("${password-service.auth.server.key.endpoint}")
    private String authServerKeyEndpoint;

    @Value("${password-service.auth.server.auth.endpoint}")
    private String authServerAuthEndpoint;


    private static final String AUTHORIZATION_API_URL = "/app/auth/v1/authentication";
    private static final String SIGN_API_URL = "/auth/key/sign";

    private static final String AUTHENTICATE_API_URL = "/app/auth/v1/authCode";

    private RestTemplate restTemplate;
    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;
    public DefaultKeyAuthStrategy() {
    }

    public DefaultKeyAuthStrategy(RestTemplate restTemplate, RSAPublicKey publicKey, RSAPrivateKey privateKey) {
        this.restTemplate = restTemplate;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

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
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("appId", appId);
        requestBody.put("signedAuthCode", signedAuthCode);
        ResponseEntity<String> authResponse = httpUtils.post(authEndpoint,requestBody,headers);
        if (authResponse.getStatusCode() != HttpStatus.OK) {
            LOGGER.error("Failed to authenticate application identity. Response: {}", authResponse);
            throw new RuntimeException("Failed to authenticate application identity");
        }
        String accessToken = authResponse.getBody();
        LOGGER.info("Got access token: {}", accessToken);
        return accessToken;
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
    private String signWithPrivateKey(String data, String privateKey) {
        // TODO: Implement signing with private key
        return data;
    }
}