package com.security.authentication.controller;

import com.security.authentication.AuthenticationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/**
 * 身份认证servlet demo
 * @author weichang_ding
 *
 */
@Controller
public class AuthenController {
	private AuthenticationService authenticationService;

	public AuthenController(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	@RequestMapping("/jitGWAuth")
	protected void jitGWAuth(HttpServletRequest req, HttpServletResponse resp) {
		authenticationService.authenticate("keyAuthenticationStrategy",req,resp);
	}

}
