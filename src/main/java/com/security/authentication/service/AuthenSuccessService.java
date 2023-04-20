package com.security.authentication.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class AuthenSuccessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenSuccessService.class);
    public String doPointLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String ret;
        ret = "/sso/loginSubmitIn.action?userId="+"userid"+"&psign="+"security"+
                "&redirectTo="+"redirecTo"+"&hostName="+"hostName"+"&devicekey=UKey&deviceinfo=UKey";
        response.sendRedirect(ret);
        return "res";
    }
}
