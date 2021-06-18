package com.nchu.lecturerplatform.controller.teacher;

import com.nchu.lecturerplatform.domain.Comment;
import com.nchu.lecturerplatform.domain.Lecturer;
import com.nchu.lecturerplatform.repository.CommentRepository;
import com.nchu.lecturerplatform.repository.LecturerRepository;
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
@RequestMapping("/teacher/info")
public class Teach_InfoController {

    @Resource
    private LecturerRepository lecturerRepository;
    @Resource
    private CommentRepository commentRepository;
    @Resource
    private CommentService commentService;

    @GetMapping
    public String InfoShow(Principal principal, Model model){
        //数据处理
        Optional<Lecturer> lecturer=lecturerRepository.findById(principal.getName());
        if(lecturer.isPresent()){
            model.addAttribute("lecturer",lecturer.get());
        }
        return "teacher/teach_info";
    }

    @GetMapping("/messages")
    public String messages(Principal principal,Model model){
        List<Comment> commentList_read=commentRepository.findByReplyUsernameAndViewState(principal.getName(),1);
        Collections.reverse(commentList_read);
        model.addAttribute("commentList_read",commentList_read);
        List<Comment> commentList_unread=commentRepository.findByReplyUsernameAndViewState(principal.getName(),0);
        model.addAttribute("commentList_unread",commentList_unread);
        return "teacher/teach_messages";
    }

    @PostMapping("/edit")
    public String InfoEditProcess(Principal principal, Model model, Lecturer lecturer, @RequestParam(value = "file") MultipartFile file){

        Lecturer lecturer_repo = lecturerRepository.findById(principal.getName()).orElse(null);
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
            lecturer_repo.setImageSite("/avatars/"+fileName);
            //更新评论头像
            commentRepository.updateImageSiteByUsername(principal.getName(),lecturer_repo.getImageSite());
        }
        lecturer_repo.setNickname(lecturer.getNickname());
        lecturer_repo.setSex(lecturer.getSex());
        lecturer_repo.setProfile(lecturer.getProfile());
        lecturerRepository.save(lecturer_repo);

        return "redirect:/teacher/info";
    }
}
