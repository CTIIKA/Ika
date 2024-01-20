package com.cc.controller;

import com.cc.dto.EmployeeDepartmentDto;
import com.cc.entity.Employee;
import com.cc.service.EmployeeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.tomcat.util.http.fileupload.impl.FileUploadIOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.Servlet;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("employee")
public class EmployeeController {

    private static final Logger log = LoggerFactory.getLogger(EmployeeController.class);

    private EmployeeService employeeService;

  @Value("${resume.file.dir}")
  private String realpath;
  @Autowired
  public EmployeeController(EmployeeService employeeService) {
    this.employeeService = employeeService;
  }

//  搜索
  @RequestMapping("search")
  public String search(
          @RequestParam(defaultValue = "1",value = "pageNum")Integer pageNum,
          @RequestParam(required = false) Integer employee_id,
          @RequestParam(required = false)String employee_name,
          @RequestParam(required = false)String department,
          @RequestParam(required = false)String address, Model model){
    PageHelper.clearPage();
    PageHelper.startPage(pageNum,5);
    List<EmployeeDepartmentDto>employee = employeeService.search(employee_id,employee_name,department,address);
    PageInfo<EmployeeDepartmentDto> pageInfo = new PageInfo<>(employee);
    model.addAttribute("pageInfo",pageInfo);
    model.addAttribute("employee_id",employee_id);
    model.addAttribute("employee_name",employee_name);
    model.addAttribute("department",department);
    model.addAttribute("address",address);
    return "emplist";
  }

//  删除员工信息
  @RequestMapping("delete")
  public String delete (Integer employee_id,RedirectAttributes ra){
    log.debug("删除的员工id:{}",employee_id);
    String oldResume = employeeService.findById(employee_id).getResume();
    if (oldResume != null)  {
      File oldFile = new File(realpath, oldResume);
      oldFile.delete();
    }
    employeeService.delete(employee_id);
    ra.addFlashAttribute("msg1","削除しました！");
    return "redirect:/employee/lists";  //跳转员工列表
  }


//  更新员工信息
  @RequestMapping("update")
  public String update(@ModelAttribute("employee")@Valid Employee employee,BindingResult rs,RedirectAttributes ra,Model model,MultipartFile file) throws IOException {
    log.debug("更新之后员工信息：社員名:{},役職名:{},性別:{},部署名:{},住所:{},雇用形態:{},生年月日：{},入社年月日:{}",employee.getEmployee_name(),employee.getJob_title(),employee.getSex(),employee.getDepartment(),employee.getAddress(),employee.getEmployment_status(),employee.getBirth_date(),employee.getHire_date());

    String originalFilename = file.getOriginalFilename();

    if (rs.hasErrors()) {
      return "updateEmp";
    } else if (employee.getBirth_date() != null && employeeService.isEmployeeAgeValid(employee.getBirth_date())) {
      model.addAttribute("errorMsg4", "社員の年齢は18歳以上、60歳以下である必要があります!");
      return "updateEmp";
    } else {
      // 删除旧文件
      String oldResume = employeeService.findById(employee.getEmployee_id()).getResume();
      if (!file.isEmpty() && oldResume != null)  {
        File oldFile = new File(realpath, oldResume);
        oldFile.delete();
      }
      if (!file.isEmpty()) {
//      处理文件上传 & 修改文件名称
        String fileNamePrefix = new SimpleDateFormat("yyyyMMddHHmmsss").format(new Date());
        String fileNameSuffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFileName = fileNamePrefix + fileNameSuffix;
        file.transferTo(new File(realpath, newFileName));
        employee.setResume(newFileName);    // 保存新文件名字
      }
      employeeService.update(employee);
      ra.addFlashAttribute("msg2", "更新しました！");
      return "redirect:/employee/lists";    // 更新成功，跳转员工列表
    }
  }



//  根据id查询员工详细信息
  @RequestMapping("detail")
  public String detail(Integer employee_id,Model model){
      log.debug("当前查询员工id:{}",employee_id);
//      根据id查询一个
    Employee employee = employeeService.findById(employee_id);
    model.addAttribute("employee",employee);
    model.addAttribute("departments",employeeService.findDepartments());
    model.addAttribute("positionRanks",employeeService.findPositionRank());
    return "updateEmp";    //跳转更新页面
  }


  @RequestMapping("add")
  public String addForm(Model model){
    model.addAttribute("employee",new Employee());
    model.addAttribute("departments",employeeService.findDepartments());
    model.addAttribute("positionRanks",employeeService.findPositionRank());
    return "addEmp";
  }
//    保存员工信息

    @RequestMapping("save")
    public String save(@ModelAttribute("employee")@Valid Employee employee, BindingResult bindingResult, Model model, RedirectAttributes ra, MultipartFile file) throws IOException {
      log.debug("社員番号:{},社員名:{},役職名:{},性別:{},部署名:{},住所:{},雇用形態:{},生年月日：{},入社年月日:{}", employee.getEmployee_id(), employee.getEmployee_name(), employee.getJob_title(), employee.getSex(), employee.getDepartment(), employee.getAddress(), employee.getEmployment_status(), employee.getBirth_date(), employee.getHire_date());

      String originalFilename = file.getOriginalFilename();
      log.debug("履歴書:{}", originalFilename);
      log.debug("履歴書大小:{}", file.getSize());
      log.debug("上传路径:{}", realpath);

      if (bindingResult.hasErrors()) {
        return "addEmp";
      } else if (employeeService.isEmployeeValid(employee.getEmployee_id())) {
        model.addAttribute("errorMsg", "この社員番号がすでに登録されています!");
        return "addEmp";
      } else if (employee.getBirth_date() != null && employeeService.isEmployeeAgeValid(employee.getBirth_date())) {
        model.addAttribute("errorMsg4", "社員の年齢は18歳以上、60歳以下である必要があります!");
        return "addEmp";
      } else {
        if (!file.isEmpty()) {
//          处理文件上传 & 修改文件名称
          String fileNamePrefix = new SimpleDateFormat("yyyyMMddHHmmsss").format(new Date());
          String fileNameSuffix = originalFilename.substring(originalFilename.lastIndexOf("."));
          String newFileName = fileNamePrefix + fileNameSuffix;
          file.transferTo(new File(realpath, newFileName));
          employee.setResume(newFileName);    //保存文件名字
        }
        employeeService.save(employee);
        ra.addFlashAttribute("msg3", "登録しました！");
        return "redirect:/employee/lists";
      }
    }


//    下载文件
    @RequestMapping("download")
    public void download(String resume,HttpServletResponse response) throws IOException{
//    在指定目录中读取文件
      File file = new File(realpath,resume);
//    将文件读取为文件输入流
      FileInputStream is = new FileInputStream(file);
//    设置以附件形式下载
      response.setHeader("content-disposition","attachment;filename=");
//    获取响应输出流
      ServletOutputStream os = response.getOutputStream();
//    输入流复制给输出流
      int len = 0;
      byte[] b =new byte[1024];
      while (true){
        len = is.read(b);
        if (len == -1)break;

        os.write(b,0,len);
      }
      is.close();
    }

  //员工列表

    @RequestMapping("lists")
    public String getAllUser(Model model, @RequestParam(defaultValue = "1",value = "pageNum")Integer pageNum){
      PageHelper.clearPage();
      PageHelper.startPage(pageNum,5);

      List<EmployeeDepartmentDto> employees = employeeService.getEmployeeWithDepartments();

      PageInfo<EmployeeDepartmentDto> pageInfo = new PageInfo<>(employees);
      model.addAttribute("pageInfo",pageInfo);
      return "emplist";
    }
}

