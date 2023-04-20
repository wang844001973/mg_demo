package com.security.authentication.controller;

import com.security.authentication.service.AuthenSuccessService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class AuthenSuccessController {
    private AuthenSuccessService authenSuccessService;

    public AuthenSuccessController(AuthenSuccessService authenSuccessService) {
        this.authenSuccessService = authenSuccessService;
    }

    @RequestMapping("${jit.AuthenSuccess.url }")
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authenSuccessService.doPointLogin(request,response);
    }
}
