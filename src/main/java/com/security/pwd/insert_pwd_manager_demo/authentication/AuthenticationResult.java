package com.security.pwd.insert_pwd_manager_demo.authentication;

import org.springframework.http.HttpStatus;

public enum AuthenticationResult {
    SUCCESS(HttpStatus.OK), UNAUTHORIZED(HttpStatus.UNAUTHORIZED), FORBIDDEN(HttpStatus.FORBIDDEN);

    private final HttpStatus httpStatus;

    AuthenticationResult(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    // 生成认证成功状态
    public static AuthenticationResult success(String sessionId) {
        AuthenticationResult result = SUCCESS;
        result.sessionId = sessionId;
        return result;
    }

    // 生成未授权状态
    public static AuthenticationResult unauthorized() {
        return UNAUTHORIZED;
    }

    // 生成没有访问权限状态
    public static AuthenticationResult forbidden() {
        return FORBIDDEN;
    }

    private String sessionId;

    public String getSessionId() {
        return sessionId;
    }
}
