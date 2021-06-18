package com.nchu.lecturerplatform.controller.admin;

import com.nchu.lecturerplatform.domain.Applicant;
import com.nchu.lecturerplatform.service.ApplicantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping("/application")
public class ApplicationController {

    @Resource//跟@Autowired功能差不多，都是用来实现依赖注入的
    private ApplicantService applicantService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/receive")
    public String receiveApplication(Applicant applicant,Model model){

        applicant.setViewState(0);//设置默认审阅状态
        applicant.setPassword(passwordEncoder.encode(applicant.getPassword()));//密码编码保护
        applicantService.save(applicant);
        model.addAttribute("response","已申请，等待管理员审核");
        return "redirect:/application/fillView";
    }

    @GetMapping("/fillView")
    public String fillView(Model model) {
        model.addAttribute("applicant",new Applicant());
        return "application";
    }


    @GetMapping("/delete")
    public String delete(Long id) {
        applicantService.delete(id);
        return "redirect:/application/list";
    }
}
