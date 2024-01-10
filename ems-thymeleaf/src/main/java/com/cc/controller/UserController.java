package com.cc.controller;

import com.cc.entity.User;
import com.cc.utils.VerifyCodeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import com.cc.service.UserService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("user")
public class UserController {
  private static final Logger log = LoggerFactory.getLogger(UserController.class);

  private UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }


  //  安全退出
  @RequestMapping("logout")
  public String logout(HttpSession session) {
    session.invalidate();
    return "redirect:/employee/lists";
  }

//  用户登录
//  初始化表单
  @RequestMapping("loging")
  public String loginForm(Model model) {
    model.addAttribute("user", new User());
    return "login";
  }

  @RequestMapping("login")
  public String login(@ModelAttribute("user") @Valid User user,
                      BindingResult rs, Model model,HttpSession httpSession, RedirectAttributes ra) {
    log.debug("本地登录姓名：{}", user.getUser_name());
    log.debug("本地登录密码：{}", user.getPassword());

    String user_name = user.getUser_name();
    String password = user.getPassword();

    if (rs.hasErrors()) {
      return "login";
    } else if (!userService.isUserValid(user_name, password)) {
      model.addAttribute("errorMsg", "ユーザIDまだはパスワードが違います!");
      return "login";
    } else {
      httpSession.setAttribute("user",user);
      ra.addFlashAttribute("msg4","ログインしました！");
      return "redirect:/employee/lists";
    }
  }


  //  用户注册
  //  初始化
  @RequestMapping("registering")
  public String registerForm(Model model){
    model.addAttribute("user",new User());
    return "regist";
  }
  @RequestMapping("register")
  public String register(@ModelAttribute("user") @Valid User user, BindingResult rs, Model model, String code, HttpSession session, RedirectAttributes ra) {
    log.debug("用户名：{},密码：{}", user.getUser_name(), user.getPassword());
    log.debug("用户输入验证码：{}", code);

    String user_name = user.getUser_name();
    String sessionCode = session.getAttribute("code").toString();


    if (rs.hasErrors()) {
      return "regist";
    } else if (!sessionCode.equalsIgnoreCase(code)) {
      model.addAttribute("errorMsg1", "認証コードが違います!");
      return "regist";
    }  else if (userService.isUserExisted(user_name)) {
      model.addAttribute("errorMsg2", "既に登録されています!");
      return "regist";
    }else if (!userService.isPasswordValid(user.getPassword())) {
      model.addAttribute("errorMsg3","英数字混合6~15文字で入力して下さい!");
      return "regist";
    } else {
      userService.register(user);
      ra.addFlashAttribute("msg", "アカウントを作成しました！");
      return "redirect:/user/loging";
    }
  }

  //  生成验证码
  @RequestMapping("generateImageCode")
  public void generateImageCode (HttpSession session, HttpServletResponse response) throws IOException {
    //生成4位随机数
    String code = VerifyCodeUtils.generateVerifyCode(4);
    //保存到session作用域
    session.setAttribute("code", code);
    //根据随机数生成图片 && 通过response响应图片 && 设置响应类型
    response.setContentType("image/png");
    ServletOutputStream os = response.getOutputStream();
    VerifyCodeUtils.outputImage(180, 50, os, code);
  }
}