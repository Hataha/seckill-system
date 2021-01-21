package com.kinoko.controller;

import com.kinoko.service.StudentService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
    @Autowired
    private StudentService studentService;

    //登录controller
    @PostMapping("/doLogin")
    public String doLogin(@RequestParam("id") String id,
                          @RequestParam("password") String password,
                          Model model, HttpSession session){
        try {
            studentService.login(id,password);
            session.setAttribute("loginId",id);
            return "dashboard";
        } catch (AuthenticationException e) {
            model.addAttribute("msg","用户名或密码错误");
            return "index";
        }
    }

    //未登录认证，返回登录页
    @GetMapping("/unauthorized")
    public String unAuthorized(Model model){
        model.addAttribute("msg","您还未登录，请先登录");
        return "index";
    }

    //进入首页
    @GetMapping("/toDashboard")
    public String dashboard(){
        return "dashboard";
    }

    //shiro可以自动完成logout操作
    @GetMapping("/logout")
    public String logout(){
        return "index";
    }
}
