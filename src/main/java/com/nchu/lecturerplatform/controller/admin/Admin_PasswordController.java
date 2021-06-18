package com.nchu.lecturerplatform.controller.admin;

import com.nchu.lecturerplatform.domain.Admin;
import com.nchu.lecturerplatform.repository.AdminRepository;
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
@RequestMapping("/admin/password")
public class Admin_PasswordController {

    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private AdminRepository adminRepository;

    @ResponseBody
    @PostMapping("/modify")
    public String PasswordModify(Principal principal, Model model, String rawPassword, String newPassword){
        //两次密码确认部分在前端完成

        Admin admin=adminRepository.findById(principal.getName()).get();
        String str="修改成功！";
        if (passwordEncoder.matches(rawPassword,admin.getPassword())) {
            admin.setPassword(passwordEncoder.encode(newPassword));
            adminRepository.save(admin);
        }else {
            str="密码错误，修改失败！";
        }
        return str;
    }

    @GetMapping
    public String PasswordShow(Principal principal, Model model){
        Admin admin=adminRepository.findById(principal.getName()).orElse(null);
        model.addAttribute("admin",admin);
        return "admin/admin_password";
    }

}
