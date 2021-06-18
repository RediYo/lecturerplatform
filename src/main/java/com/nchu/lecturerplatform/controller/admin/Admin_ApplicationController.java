package com.nchu.lecturerplatform.controller.admin;

import com.nchu.lecturerplatform.domain.Applicant;
import com.nchu.lecturerplatform.domain.Lecturer;
import com.nchu.lecturerplatform.domain.Role;
import com.nchu.lecturerplatform.repository.AuthorityRepository;
import com.nchu.lecturerplatform.repository.LecturerRepository;
import com.nchu.lecturerplatform.service.ApplicantService;
import com.nchu.lecturerplatform.service.MailService;
import com.nchu.lecturerplatform.util.UUIDUtil;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.Iterator;

@Controller
@RequestMapping("/admin/application")
public class Admin_ApplicationController {

    @Resource//跟@Autowired功能差不多，都是用来实现依赖注入的
    private ApplicantService applicantService;
    @Resource
    private LecturerRepository lecturerRepository;
    @Resource
    private AuthorityRepository authorityRepository;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private MailService mailService;

    @PostMapping("/review-process")
    public String reviewProcess(Long id){
        //修改审阅状态为1 添加该讲师账号，注意还需添加进入权限表
        Applicant applicant=applicantService.findApplicantById(id);
        applicant.setViewState(1);
        applicantService.save(applicant);

        Lecturer lecturer=new Lecturer();
        lecturer.setUsername(applicant.getUsername());
        lecturer.setNickname(applicant.getUsername());
        lecturer.setSex(applicant.getSex());
        lecturer.setProfile(applicant.getProfile());
        lecturer.setPassword(applicant.getPassword());
        lecturer.setEnabled(0);//账号状态默认禁用，邮箱验证后启用
        String code = UUIDUtil.getUUID();
        lecturer.setCode(code);
        lecturer.setImageSite("/images/default-pic.png");
        Role role =authorityRepository.findByAuthority("ROLE_LECTURER");
        lecturer.getLecturerRoles().add(role);
        lecturerRepository.save(lecturer);
        //用户注册，同时发送一封激活邮件
        System.out.println(lecturer.getUsername()+code);
        //主题
        String subject = "来自智学讲师平台的激活邮件";
        //lecturer/checkCode?code=code(激活码)是我们点击邮件链接之后根据激活码查询用户，如果存在说明一致，将用户状态修改为“1”激活
        //上面的激活码发送到用户注册邮箱
        //注意:此处的链接地址,是项目内部地址,如果我们没有正式的服务器地址,暂时无法从qq邮箱中跳转到我们自己项目的激活页面
        String context = "<a href=\"http://localhost:8080/register/application/checkCode?code="+code+"\">激活请点击:"+code+"</a>";
        //发送激活邮件
        mailService.sendHtmlMail (lecturer.getUsername(),subject,context);
        return "redirect:/admin/application/list";
    }


    @GetMapping("/view")
    public String view(Model model,Long id){
        Applicant applicant=applicantService.findApplicantById(id);
        model.addAttribute("applicant",applicant);
        return "admin/admin_applicationView";
    }

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value = "pageNum", defaultValue = "0") int pageNum,@RequestParam(value = "viewState", defaultValue = "-1")int viewState, @RequestParam(value = "pageSize", defaultValue = "5") int pageSize) {
        System.out.println("============================");
        Page<Applicant> applicants;
        if (viewState == -1) {
            applicants = applicantService.getApplicantList(pageNum, pageSize);
        }else {
            applicants = applicantService.getApplicantListByViewState(viewState,pageNum, pageSize);
        }
        model.addAttribute("viewState",viewState);
        System.out.println("总页数" + applicants.getTotalPages());
        System.out.println("当前页是：" + pageNum);

        System.out.println("分页数据：");
        Iterator<Applicant> u = applicants.iterator();
        while (u.hasNext()) {
            System.out.println(u.next().toString());
        }

        model.addAttribute("applicants", applicants);

        return "admin/admin_applications";
    }


}
