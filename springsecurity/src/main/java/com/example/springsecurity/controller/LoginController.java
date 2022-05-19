package com.example.springsecurity.controller;

import org.springframework.lang.UsesSunHttpServer;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {

    //@Secured("ROLE_abc1")
    // PreAuthorize可以以ROLE_开头，也可以不用它开头
    @PreAuthorize("hasAnyRole('ROLE_abc')")
    @RequestMapping("toMain")
    public String toMain(){
        return "redirect:main.html";
    }

    @RequestMapping("toError")
    public String toError(){
        return "redirect:error.html";
    }

    @GetMapping("demo")
    @ResponseBody
    public String demo(){
        return "demo";
    }
}
