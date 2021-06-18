package com.nchu.lecturerplatform.controller.student;

import com.nchu.lecturerplatform.domain.Comment;
import com.nchu.lecturerplatform.domain.Student;
import com.nchu.lecturerplatform.repository.CommentRepository;
import com.nchu.lecturerplatform.repository.StudentRepository;
import com.nchu.lecturerplatform.service.CommentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/student/info")
public class Stu_InfoController {

    @Resource
    private StudentRepository studentRepository;
    @Resource
    private CommentRepository commentRepository;
    @Resource
    private CommentService commentService;

    @GetMapping
    public String InfoShow(Principal principal, Model model) {
        //数据处理
        Optional<Student> student = studentRepository.findById(principal.getName());
        if (student.isPresent()) {
            model.addAttribute("student", student.get());
        }
        return "student/stu_info";
    }

    @GetMapping("/messages")
    public String messages(Principal principal,Model model){
        List<Comment> commentList_read=commentRepository.findByReplyUsernameAndViewState(principal.getName(),1);
        Collections.reverse(commentList_read);
        model.addAttribute("commentList_read",commentList_read);
        List<Comment> commentList_unread=commentRepository.findByReplyUsernameAndViewState(principal.getName(),0);
        model.addAttribute("commentList_unread",commentList_unread);
        Student student=studentRepository.findById(principal.getName()).orElse(null);
        model.addAttribute("student", student);
        return "student/stu_messages";
    }

    @PostMapping("/edit")
    public String InfoEditProcess(Principal principal, Student student, @RequestParam(value = "file") MultipartFile file) {

        Student student_repo = studentRepository.findById(principal.getName()).orElse(null);
        if(!file.isEmpty()){
            String fileName = file.getOriginalFilename();  // 文件名
            String suffixName = fileName.substring(fileName.lastIndexOf("."));  // 只获取后缀名
            String filePath = "D://LecturerPlatform//avatars//"; // 上传后的路径
            fileName = principal.getName() + suffixName; // 新头像文件名为用户名
            File dest = new File(filePath + fileName);
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            try {
                file.transferTo(dest);
            } catch (IOException e) {
                e.printStackTrace();
            }
            student_repo.setImageSite("/avatars/"+fileName);//头像图片不空才需要修改
            commentService.modifyImageSiteByUsername(principal.getName(),student_repo.getImageSite());
        }
        student_repo.setNickname(student.getNickname());
        student_repo.setSex(student.getSex());
        student_repo.setProfile(student.getProfile());
        studentRepository.save(student_repo);


        return "redirect:/student/info";
    }
}
