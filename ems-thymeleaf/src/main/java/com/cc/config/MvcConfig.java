package com.cc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class MvcConfig implements WebMvcConfigurer {
//通过这里面配置，不需要为每一个访问thymeleaf模板页面单独开发一个controller请求了
  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
//    viewController 请求路径  viewName：跳转视图
    registry.addViewController("login").setViewName("login");
    registry.addViewController("register").setViewName("regist");
    registry.addViewController("addEmp").setViewName("addEmp");
    registry.addViewController("emplist").setViewName("emplist");
    registry.addViewController("updateEmp").setViewName("updateEmp");
    registry.addViewController("employeelogin").setViewName("employeelogin");
    registry.addViewController("attendance").setViewName("attendance");
    registry.addViewController("clock").setViewName("clock");
    registry.addViewController("clockUpdate").setViewName("clockUpdate");
  }
}
