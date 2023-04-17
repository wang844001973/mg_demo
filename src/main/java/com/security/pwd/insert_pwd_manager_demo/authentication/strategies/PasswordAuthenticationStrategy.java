package com.security.pwd.insert_pwd_manager_demo.authentication.strategies;

import com.security.pwd.insert_pwd_manager_demo.authentication.AuthenticationResult;
import com.security.pwd.insert_pwd_manager_demo.authentication.AuthenticationStrategy;
import com.security.pwd.insert_pwd_manager_demo.authentication.util.HttpUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("passwordAuthenticationStrategy")
public class PasswordAuthenticationStrategy implements AuthenticationStrategy {
    private String baseUrl;
    private String appId;
    private String password;
    private String sessionId;
    @Override
    public AuthenticationResult authenticate(Map<String ,String> credentials) {
        String encodedCredentials = Base64.encodeBase64String((appId + ":" + password).getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + encodedCredentials);
        ResponseEntity<String> response = HttpUtils.sendRequest(baseUrl + "/authenticate", HttpMethod.GET, headers, null, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            this.sessionId = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
            return AuthenticationResult.success(sessionId);
        } else {
            return AuthenticationResult.unauthorized();
        }
    }
}