package com.security.authentication.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;

@Controller
public class LoginController {
    @GetMapping("/index")
    public String index(){
        return "/gw/index.jsp";
    }
    @GetMapping("/login1")
    public String index1(){
        return "/login";
    }
    @GetMapping("/login2")
    public String index2(){
        return "/login.jsp";
    }
    @GetMapping("/test")
    @ResponseBody
    public Object test(){
        ArrayList<Object> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");
        return list;
    }
}
