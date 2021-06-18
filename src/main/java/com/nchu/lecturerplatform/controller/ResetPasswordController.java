package com.nchu.lecturerplatform.controller;

import com.nchu.lecturerplatform.domain.Lecturer;
import com.nchu.lecturerplatform.domain.Student;
import com.nchu.lecturerplatform.repository.LecturerRepository;
import com.nchu.lecturerplatform.repository.StudentRepository;
import com.nchu.lecturerplatform.service.MailService;
import com.nchu.lecturerplatform.service.StudentService;
import com.nchu.lecturerplatform.util.UUIDUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@RequestMapping("/password/reset")
@Controller
public class ResetPasswordController {

    @Resource
    private MailService mailService;
    @Resource
    private StudentRepository studentRepository;
    @Resource
    private LecturerRepository lecturerRepository;
    @Resource
    private StudentService studentService;
    @Resource
    private PasswordEncoder passwordEncoder;

    @PostMapping
    public String reset(String username){

        //先发送邮件通知，用户点击之后再重置密码
        Student student=studentRepository.findById(username).orElse(null);
        Lecturer lecturer=lecturerRepository.findById(username).orElse(null);
        String code = UUIDUtil.getUUID();
        if (student != null) {
            student.setCode(code);
            studentRepository.saveAndFlush(student);
        } else if (lecturer != null) {
            lecturer.setCode(code);
            lecturerRepository.saveAndFlush(lecturer);
        }else {
            System.out.println("无此用户");
        }
        String subject = "来自智学讲师平台的密码重置邮件";
        String context = "<a href=\"http://localhost:8080/password/reset/checkCode?code="+code+"\">激活请点击，密码重置为：123456</a>";
        //发送激活邮件
        mailService.sendHtmlMail (username,subject,context);
        return "delayResponse";
    }

    /**
     * 校验邮箱中的code激活账户
     * 首先根据激活码code查询用户，之后再把状态修改为"1"
     */
    @GetMapping("/checkCode")
    public String checkCode(String code){

        Lecturer lecturer=lecturerRepository.findByCode(code);
        Student student=studentRepository.findStudentByCode(code);
        if (student !=null){
            //把code验证码清空，已经不需要了
            student.setCode("");
            student.setPassword(passwordEncoder.encode("123456"));
            studentRepository.saveAndFlush(student);
            System.out.println("密码重置成功");
        }else if(lecturer!=null) {
            //把code验证码清空，已经不需要了
            lecturer.setCode("");
            lecturer.setPassword(passwordEncoder.encode("123456"));
            lecturerRepository.saveAndFlush(lecturer);
            System.out.println("密码重置成功");
        }else {
            System.out.println("不存在该用户");
        }
        return "redirect:/login";
    }
}
