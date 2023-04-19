package com.security.authentication.test;

import javax.naming.AuthenticationException;
import java.util.Map;

public abstract class PasswordStrategy {
    /**
     * 进行认证，并返回会话标识。
     *
     * @param appId  应用标识
     * @param params 认证所需的参数
     * @return 认证成功后的会话标识
     * @throws AuthenticationException 认证失败时抛出此异常
     */
    public abstract String authenticate(String appId, Map<String, String> params)
            throws AuthenticationException;
}
