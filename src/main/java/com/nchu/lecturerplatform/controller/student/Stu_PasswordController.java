package com.nchu.lecturerplatform.controller.student;

import com.nchu.lecturerplatform.domain.Student;
import com.nchu.lecturerplatform.repository.StudentRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.security.Principal;

@Controller
@RequestMapping("/student/password")
public class Stu_PasswordController {

    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private StudentRepository studentRepository;


    @ResponseBody
    @PostMapping("/modify")
    public String PasswordModify(Principal principal, Model model, String rawPassword, String newPassword){
        //两次密码确认部分在前端完成

        Student student=studentRepository.findById(principal.getName()).get();
        model.addAttribute("student",student);
        String str="修改成功！";
        if (passwordEncoder.matches(rawPassword,student.getPassword())) {
            student.setPassword(passwordEncoder.encode(newPassword));
            studentRepository.save(student);
        }else {
            str="密码错误，修改失败！";
        }
        return str;
    }

    @GetMapping
    public String PasswordShow(Principal principal, Model model){
        Student student=studentRepository.findById(principal.getName()).orElse(null);
        model.addAttribute("student",student);
        return "student/stu_password";
    }
}
