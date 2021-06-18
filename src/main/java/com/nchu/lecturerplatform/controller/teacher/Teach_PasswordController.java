package com.nchu.lecturerplatform.controller.teacher;

import com.nchu.lecturerplatform.domain.Lecturer;
import com.nchu.lecturerplatform.repository.LecturerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/teacher/password")
public class Teach_PasswordController {

    @Resource
    LecturerRepository lecturerRepository;
    @Resource
    private PasswordEncoder passwordEncoder;

    @ResponseBody
    @PostMapping("/modify")
    public String PasswordModify(Principal principal, Model model, String rawPassword, String newPassword){
        //两次密码确认部分在前端完成

        Optional<Lecturer> lecturer_repo=lecturerRepository.findById(principal.getName());
        Lecturer lecturer=lecturer_repo.get();
        String str="修改成功！";
        if (passwordEncoder.matches(rawPassword,lecturer.getPassword())) {
            lecturer.setPassword(passwordEncoder.encode(newPassword));
            lecturerRepository.save(lecturer);
        }else {
            str="密码错误，修改失败！";
        }
        return str;
    }

    @GetMapping
    public String PasswordShow(Principal principal,Model model){
        model.addAttribute("lecturer",lecturerRepository.findById(principal.getName()).orElse(null));
        return "teacher/teach_password";
    }

}
