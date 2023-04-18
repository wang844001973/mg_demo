package com.security.pwd.insert_pwd_manager_demo.authentication.strategies;

import com.security.pwd.insert_pwd_manager_demo.authentication.AuthenticationResult;
import com.security.pwd.insert_pwd_manager_demo.authentication.AuthenticationStrategy;
import com.security.pwd.insert_pwd_manager_demo.authentication.test.HttpUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("passwordAuthenticationStrategy")
public class PasswordAuthenticationStrategy implements AuthenticationStrategy {
    @Autowired
    private HttpUtils httpUtils;
    private String baseUrl;
    private String appId;
    private String password;
    private String sessionId;
    @Override
    public AuthenticationResult authenticate(Map<String ,String> credentials) {
        String encodedCredentials = Base64.encodeBase64String((appId + ":" + password).getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + encodedCredentials);
        ResponseEntity<String> response = httpUtils.get(baseUrl + "/authenticate",headers);

        if (response.getStatusCode() == HttpStatus.OK) {
            this.sessionId = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
            return AuthenticationResult.success(sessionId);
        } else {
            return AuthenticationResult.unauthorized();
        }
    }
}