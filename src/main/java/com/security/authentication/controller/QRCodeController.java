package com.security.authentication.controller;

import com.security.authentication.util.JitGatewayUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * 生成二维码及查询二维码状态的servlet
 * @author weichang_ding
 *
 */

@Controller
public class QRCodeController{

	@RequestMapping("/jitGWQrcode")
	protected void jitGWQrcode(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 实例化网关工具类
		JitGatewayUtil jitGatewayUtil = new JitGatewayUtil();
		
		// 服务类型
		String serviceType = request.getParameter("Service_Type");
		
		// 根据参数调用不同服务
		if("qrcode_poll".equals(serviceType)){
			// 查询二维码状态
			// 获取cookie中的jitcode
			String code = null;
			Cookie[] cookies = request.getCookies();
            for(Cookie c :cookies ){
            	if(JitGatewayUtil.QRConstant.KEY_JIT_QRCODE.equals(c.getName())){
            		code = c.getValue();
            	}
            }
            
            // 查询二维码状态
            String qrCodeState = jitGatewayUtil.queryQRCodeState(code);
            
            // 设置response
            response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println(qrCodeState);
			out.flush();
			out.close();
		}else{
			// 调用网关工具类生成二维码
			JitGatewayUtil.QRCodeResult qrCodeResult = jitGatewayUtil.gengrateQRCode();
			
			// 设置cookie信息
			org.apache.commons.httpclient.Cookie[] cookies = qrCodeResult.getCookies();
            for (org.apache.commons.httpclient.Cookie cookie : cookies) {
				Cookie c = new Cookie(cookie.getName(),cookie.getValue());
				response.addCookie(c);
			}
			
			// 设置response
			response.setContentType("image/jpeg");
			OutputStream os = response.getOutputStream();  
	        os.write(qrCodeResult.getBytes());
	        os.flush();  
	        os.close();
		}
	}
}
