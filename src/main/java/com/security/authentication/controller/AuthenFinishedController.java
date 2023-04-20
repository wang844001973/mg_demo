package com.security.authentication.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

/**
 * 认证完成后转向的servlet，应用参照扩展
 * @author weichang_ding
 *
 */
@Controller
public class AuthenFinishedController{

	// 生成原文后的跳转地址
	@Value("${jit.AuthenSuccess.url}")
	private String forwardURL = null;

	@RequestMapping("/authenFinished")
	protected void authenFinished(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		// 认证后的信息已放入request,以下代码说明request中的内容
		// 应用改造时可把认证信息保存到其它对象中比如session,供应用使用
		
		// isSuccess:boolean类型，认证成功标记,true成功/false失败
		HttpSession session = request.getSession(false);
		boolean isSuccess = ((Boolean)session.getAttribute("isSuccess")).booleanValue();
		System.out.println("认证成功标记:" + isSuccess);
		
		if(isSuccess){// 认证成功
			// 以下4类信息都是存放在request中，具体使用可参照context.jsp
			// certAttributeNodeMap:Map类型，证书信息
			Map certAttributeNodeMap = (Map)session.getAttribute("certAttributeNodeMap");
			// umsAttributeNodeMap:Map类型，UMS信息
			Map umsAttributeNodeMap = (Map)session.getAttribute("umsAttributeNodeMap");
			// pmsAttributeNodeMap类型，PMS信息
			Map pmsAttributeNodeMap = (Map)session.getAttribute("pmsAttributeNodeMap");
			// customAttributeNodeMap：String类型，自定义信息
			String customAttributeNodeMap = session.getAttribute("customAttributeNodeMap") == null?
					null:session.getAttribute("customAttributeNodeMap").toString();
		}else{// 认证失败
			// errCode:String类型，错误编码
			if (null != session.getAttribute("errCode")) {
				String errCode = session.getAttribute("errCode").toString();
				System.out.println("错误编码:" + errCode);
			}
			
			// errDesc:String类型，错误描述
			if (null != session.getAttribute("errDesc")) {
				String errDesc = session.getAttribute("errDesc").toString();
				System.out.println("错误描述:" + errDesc);
			}
		}
		
		// 设置跳转页面
		request.getRequestDispatcher(forwardURL).forward(request, response);
	}

}
