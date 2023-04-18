package com.security.pwd.insert_pwd_manager_demo.authentication.controller;

import com.security.pwd.insert_pwd_manager_demo.authentication.AuthenticationResult;
import com.security.pwd.insert_pwd_manager_demo.authentication.util.JitGatewayUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class RandomController {

    // 配置文件地址
    private String propertiesURL = null;

    // 配置文件正确标记
    private boolean configItemRightFlag = true;

    public RandomController(@Value("${propertiesURL}") String propertiesURL) {
        System.out.println("创建类：RandomController");
        this.propertiesURL = propertiesURL;
    }

    @PostConstruct
    public void init() {
        System.out.println("生成原文初始化开始");

        // 调用工具类方法初始化配置
        configItemRightFlag = JitGatewayUtil.initConfigBean(propertiesURL);

        System.out.println("生成原文初始化结束");
    }

    @GetMapping("/jitGWRandom")
    public Object getRandom(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // 配置文件不正确则返回
        if (!configItemRightFlag) {
            System.out.println("配置文件不正确");
            return AuthenticationResult.unauthorized();
        }

        // 实例化网关工具类
        JitGatewayUtil jitGatewayUtil = new JitGatewayUtil();

        //是否开启二维码
        Object qrCodeAuthObj = JitGatewayUtil.configMap.get(JitGatewayUtil.ConfigConstant.KEY_QRCODE_AUTH);
        String qrCodeAuthStr = String.valueOf(qrCodeAuthObj);
        String randNum = null;
        String pinCode = "false";
        if ("false".equals(qrCodeAuthStr)) {
            System.out.println("生成原文开始");

            // 设置页面不缓存
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);

            // 调用网关工具方法请求认证原文
            randNum = jitGatewayUtil.generateRandomNum();

            // 如果请求的认证原文为空，设置错误状态并返回
            if (!jitGatewayUtil.isNotNull(randNum)) {
                System.out.println("生成原文为空！");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return AuthenticationResult.unauthorized();
            }

            //获取pinCode的状态，传到客户端
            pinCode = (String) JitGatewayUtil.configMap.get(JitGatewayUtil.ConfigConstant.KEY_PIN_CODE);

            // 设置认证原文到session，用于程序向后传递，通讯报文中使用
            request.getSession().setAttribute("original_data", randNum);

            System.out.println("生成原文结束，成功生成原文：" + randNum);
        }

        return "{\"original_data\":\"" + randNum + "\",\"QRCodeAuth\":\"" + qrCodeAuthStr + "\",\"original\":\"" + randNum + "\",\"pinCode\":\"" + pinCode + "\"}";
    }
}
