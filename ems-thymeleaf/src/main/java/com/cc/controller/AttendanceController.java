package com.cc.controller;

import com.cc.entity.Attendance;
import com.cc.entity.Employee;
import com.cc.service.EmployeeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.Year;
import java.util.List;

@Controller
@RequestMapping("worker")
public class AttendanceController {
  private static final Logger log= LoggerFactory.getLogger(EmployeeController.class);
  private final EmployeeService employeeService;
  @Autowired
  public AttendanceController(EmployeeService employeeService) {this.employeeService = employeeService;}

  //  安全退出
  @RequestMapping("logout")
  public String logout(HttpSession session) {
    session.invalidate();
    return "redirect:/worker/loging";
  }

  @RequestMapping("loging")
  public String loginForm(Model model) {
    model.addAttribute("employee", new Employee());
    return "employeelogin";
  }

  @RequestMapping("login")
  public String login(@ModelAttribute("employee") Employee employee, Model model, HttpSession session, RedirectAttributes ra) {
    log.debug("本地登录姓名：{}",employee.getEmployee_id());
    log.debug("本地登录密码：{}",employee.getPassword());

    Integer employee_id =employee.getEmployee_id();
    String password = employee.getPassword();

    if (employee_id==null) {
      model.addAttribute("error","社員番号を入力してください！");
      return "employeelogin";
    } else if (StringUtils.isEmpty(employee.getPassword())) {
      model.addAttribute("error", "パスワードを入力してください!");
      return "employeelogin";
    }else if (!employeeService.isPasswordValid(employee_id,password)) {
       model.addAttribute("error", "社員IDまだはパスワードが違います!");
       return "employeelogin";
    }
    String employee_name = employeeService.findById(employee_id).getEmployee_name();
    ra.addFlashAttribute("error","ログインしました！");
    session.setAttribute("employee_id",employee_id);
    session.setAttribute("employee_name",employee_name);
    return "redirect:/worker/attendance?employee_id="+employee_id;
  }

  @RequestMapping("attendance")
  public String getAttendance(Model model, @RequestParam(defaultValue = "1",value = "pageNum")Integer pageNum,@RequestParam Integer employee_id){
    PageHelper.clearPage();
    PageHelper.startPage(pageNum,10);
    List<Attendance>attendances = employeeService.getAllAttendances(employee_id);
    PageInfo<Attendance> pageInfo = new PageInfo<>(attendances);
    model.addAttribute("pageInfo",pageInfo);
    return "attendance";
  }

  @RequestMapping("search")
  public String searchDate(
          @RequestParam(defaultValue = "1",value = "pageNum")Integer pageNum,
          @RequestParam(required = false) Integer year,
          @RequestParam(required = false)Integer month,
          @RequestParam(required = false)Integer day,
          @RequestParam Integer employee_id,
          Model model){
    if (year == null && month == null){
      year = Year.now().getValue();
      month = LocalDate.now().getMonthValue();
    }

    PageHelper.clearPage();
    PageHelper.startPage(pageNum,10);
    List<Attendance>attendances = employeeService.searchDate(employee_id,year,month,day);
    PageInfo<Attendance> pageInfo = new PageInfo<>(attendances);
    model.addAttribute("pageInfo",pageInfo);
    model.addAttribute("searchYear",year);
    model.addAttribute("searchYear",month);
    model.addAttribute("searchYear",day);
    return "attendance";
  }



}
