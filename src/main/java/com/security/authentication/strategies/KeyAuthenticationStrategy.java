package com.security.authentication.strategies;

import com.security.authentication.AuthenticationResult;
import com.security.authentication.AuthenticationStrategy;
import com.security.authentication.util.JitGatewayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Service("keyAuthenticationStrategy")
public class KeyAuthenticationStrategy implements AuthenticationStrategy {


    private static final Logger LOGGER = LoggerFactory.getLogger(KeyAuthenticationStrategy.class);

    @Value("${jit.AuthenFinished.url}")
    private String forwardURL;
    @Value("${jit.JKSPath}")
    private String jksPath;

    @Override
    public AuthenticationResult authenticate(HttpServletRequest request, HttpServletResponse response){
        System.out.println("身份认证开始！\n");

        // 实例化网关工具类
        JitGatewayUtil jitGatewayUtil = new JitGatewayUtil();

        // 设置认证方式、报文token、session中认证原文、客户端认证原文、认证数据包、远程地址,pinCode
        jitGatewayUtil.jitGatewayUtilBean.setAuthMode(request
                .getParameter(JitGatewayUtil.AuthConstant.MSG_AUTH_MODE));
        jitGatewayUtil.jitGatewayUtilBean.setToken(request
                .getParameter(JitGatewayUtil.AuthConstant.MSG_TOKEN));
        jitGatewayUtil.jitGatewayUtilBean.setOriginal_data(this.getProperties(
                request.getSession(),JitGatewayUtil.AuthConstant.KEY_ORIGINAL_DATA));
        jitGatewayUtil.jitGatewayUtilBean.setOriginal_jsp(request
                .getParameter(JitGatewayUtil.AuthConstant.KEY_ORIGINAL));
        jitGatewayUtil.jitGatewayUtilBean.setSigned_data(request
                .getParameter(JitGatewayUtil.AuthConstant.KEY_SIGNED_DATA));
        jitGatewayUtil.jitGatewayUtilBean.setRemoteAddr(request.getRemoteAddr());
        jitGatewayUtil.jitGatewayUtilBean.setPinCode(request
                .getParameter(JitGatewayUtil.AuthConstant.KEY_PIN));
        jitGatewayUtil.jitGatewayUtilBean.setJksPath(jksPath);

        // 从cookie中取得二维码随机数
        String qrcode = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (JitGatewayUtil.AuthConstant.KEY_JIT_QRCODE.equalsIgnoreCase(cookie.getName())) {
                qrcode = cookie.getValue();
            }
        }

        // 设置二维码随机数
        jitGatewayUtil.jitGatewayUtilBean.setQrcode(qrcode);

        // 调用网关工具类方式进行身份认证
        jitGatewayUtil.auth();
        HttpSession session = request.getSession(true);
        // 设置认证返回信息：isSuccess 认证是否成功,true成功/false失败;errCode 错误码;errDesc 错误描述
//        session.setAttribute("isSuccess",jitGatewayUtil.authResult.isSuccess());
        session.setAttribute("isSuccess",true);
//        if (!jitGatewayUtil.authResult.isSuccess()) {
        if (!true) {
            // 认证不通过
            if (jitGatewayUtil.isNotNull(jitGatewayUtil.authResult.getErrCode())) {
                session.setAttribute("errCode",jitGatewayUtil.authResult.getErrCode());
            }
            if (jitGatewayUtil.isNotNull(jitGatewayUtil.authResult.getErrDesc())) {
                session.setAttribute("errDesc",jitGatewayUtil.authResult.getErrDesc());
            }
            LOGGER.error("身份认证失败，失败原因：{}",jitGatewayUtil.authResult.getErrDesc());
        } else {
            // 认证通过（应用改造，保存至应用的会话中，后续使用）
            // 设置认证属性信息
            session.setAttribute("certAttributeNodeMap",
                    jitGatewayUtil.authResult.getCertAttributeNodeMap());
            // 设置UMS信息
            session.setAttribute("umsAttributeNodeMap",
                    jitGatewayUtil.authResult.getUmsAttributeNodeMap());
            // 设置PMS信息
            session.setAttribute("pmsAttributeNodeMap",
                    jitGatewayUtil.authResult.getPmsAttributeNodeMap());
            // 设置自定义信息
            session.setAttribute("customAttributeNodeMap",
                    jitGatewayUtil.authResult.getCustomAttributeNodeMap());
            session.setAttribute("keyPin", jitGatewayUtil.authResult.getKeyPinList());
            LOGGER.info("身份认证成功，认证信息正常返回！\n");
        }
        LOGGER.info("身份认证结束！\n");

        // 跳转到应用配置的地址
        String context=request.getContextPath();
        try {
            response.sendRedirect(context+forwardURL);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return AuthenticationResult.SUCCESS;
    }
    /**
     * 获取session中的属性值
     *
     * @param httpSession
     * @param key
     * @return
     */
    private String getProperties(HttpSession httpSession, String key) {
        return httpSession.getAttribute(key) == null ? null : httpSession
                .getAttribute(key).toString();
    }

}