package com.security.pwd.insert_pwd_manager_demo.authentication.test;

import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.util.DigestUtils;

import java.security.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KeyAuthenticationStrategy implements PasswordStrategy {
    private static final String AUTH_PATH = "/keyAuth";

    private String appKey;
    private PrivateKey privateKey;

    public KeyAuthenticationStrategy(String appKey, PrivateKey privateKey) {
        this.appKey = appKey;
        this.privateKey = privateKey;
    }

    @Override
    public String authenticate() {
        String nonce = generateNonce();
        long timestamp = System.currentTimeMillis() / 1000;

        String authCode = getAuthCode(nonce, timestamp);
        String signature = getSignature(authCode);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("appKey", appKey);
        headers.put("signature", signature);

        Map<String, Object> body = new HashMap<>();
        body.put("appKey", appKey);
        body.put("authCode", authCode);
        body.put("nonce", nonce);
        body.put("timestamp", timestamp);

        String response = HttpUtils.postRequest(getAuthUrl(), headers, body);
        JSONObject jsonObject = JSON.parseObject(response);

        if (jsonObject.getIntValue("code") == 0) {
            return jsonObject.getJSONObject("data").getString("token");
        } else {
            throw new AuthenticationException("Key authentication failed");
        }
    }

    private String getAuthUrl() {
        return PasswordService.BASE_URL + AUTH_PATH;
    }

    private String generateNonce() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    private String getAuthCode(String nonce, long timestamp) {
        String plain = String.format("%s:%d:%s", appKey, timestamp, nonce);
        return DigestUtils.md5DigestAsHex(plain.getBytes());
    }

    private String getSignature(String authCode) {
        try {
            Signature sign = Signature.getInstance("SHA256withRSA");
            sign.initSign(privateKey);
            sign.update(authCode.getBytes());
            byte[] signature = sign.sign();
            return Base64.getEncoder().encodeToString(signature);
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            throw new AuthenticationException("Error signing auth code", e);
        }
    }
}
