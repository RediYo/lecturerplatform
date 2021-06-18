package com.nchu.lecturerplatform.controller;

import com.nchu.lecturerplatform.domain.Lecturer;
import com.nchu.lecturerplatform.domain.Role;
import com.nchu.lecturerplatform.domain.Student;
import com.nchu.lecturerplatform.repository.AuthorityRepository;
import com.nchu.lecturerplatform.repository.LecturerRepository;
import com.nchu.lecturerplatform.repository.StudentRepository;
import com.nchu.lecturerplatform.service.MailService;
import com.nchu.lecturerplatform.service.StudentService;
import com.nchu.lecturerplatform.util.RegistrationForm;
import com.nchu.lecturerplatform.util.UUIDUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    @Resource
    private StudentRepository studentRepository;
    @Resource
    private AuthorityRepository authorityRepository;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private MailService mailService;
    @Resource
    private StudentService studentService;
    @Resource
    private LecturerRepository lecturerRepository;

    @GetMapping
    public String registerForm() {//跳转到注册页面
        return "registration";
    }

    @ResponseBody
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    @PostMapping
    public void processRegistration(RegistrationForm form) {//RegistrationForm form为注册表单
        Student student = form.toStudent(passwordEncoder);//默认注册为学生
        Role role = authorityRepository.findByAuthority("ROLE_STUDENT");
        student.getStuRoles().add(role);
        String code = UUIDUtil.getUUID();
        student.setEnabled(0);
        student.setCode(code);
        studentRepository.save(student);
        //用户注册，同时发送一封激活邮件
        System.out.println(code);
        //主题
        String subject = "来自智学讲师平台的激活邮件";
        //student/checkCode?code=code(激活码)是我们点击邮件链接之后根据激活码查询用户，如果存在说明一致，将用户状态修改为“1”激活
        //上面的激活码发送到用户注册邮箱
        //注意:此处的链接地址,是项目内部地址,如果我们没有正式的服务器地址,暂时无法从qq邮箱中跳转到我们自己项目的激活页面
        String context = "<a href=\"http://localhost:8080/register/checkCode?code=" + code + "\">激活请点击:" + code + "</a>";
        //发送激活邮件
        mailService.sendHtmlMail(student.getUsername(), subject, context);
        //userAuthoritiesRepository.save(new UserAuthorities(form.getUsername(),0));
        /*return "redirect:/login";//注册完成，跳转到登录页面*/
    }


    @GetMapping("/checkCode")
    public String checkCode(String code) {
        Student student = studentService.checkCode(code);
        //如果用户不等于null，把用户状态修改enabled=1
        if (student != null) {
            student.setEnabled(1);
            //把code验证码清空
            student.setCode("");
            studentService.updateStudentStatus(student);
            System.out.println("激活成功");
        } else {
            System.out.println("不存在该用户");
        }
        return "redirect:/login";
    }

    @GetMapping("/application/checkCode")
    public String checkCodeForApplication(String code) {
        Lecturer lecturer = lecturerRepository.findByCode(code);
        //如果用户不等于null，把用户状态修改status=1
        if (lecturer != null) {
            lecturer.setEnabled(1);
            //把code验证码清空，已经不需要了
            lecturer.setCode("");
            lecturerRepository.saveAndFlush(lecturer);
            System.out.println("激活成功");
        } else {
            System.out.println("不存在该用户");
        }
        return "redirect:/login";
    }

}
