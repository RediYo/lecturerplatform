package com.nchu.lecturerplatform.controller.teacher;

import com.nchu.lecturerplatform.domain.*;
import com.nchu.lecturerplatform.repository.*;
import com.nchu.lecturerplatform.service.CourseService;
import com.nchu.lecturerplatform.util.FileUtil;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.Principal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/teacher/teach")
public class Teach_TeachController {//授课系统

    @Resource
    private CourseService courseService;
    @Resource
    private LecturerRepository lecturerRepository;
    @Resource
    NoticeRepository noticeRepository;
    @Resource
    private CommentRepository commentRepository;
    @Resource
    private TopTitleRepository topTitleRepository;
    @Resource
    private SubTitleRepository subTitleRepository;
    @Resource
    private StudentRepository studentRepository;
    @Resource
    private ShareDocRepository shareDocRepository;
    @Resource
    private CourseRepository courseRepository;


    @GetMapping("/share-doc")
    public String courseDocShareView(Long id,Model model,Principal principal){
        Course course=courseService.findCourseById(id);
        /*ReadFile rf = new ReadFile();
        List<String> fileNameList=rf.getFileNameList(id,"docs");
        model.addAttribute("fileNameList",fileNameList);*/
        model.addAttribute("course",course);
        model.addAttribute("lecturer",lecturerRepository.findById(principal.getName()).orElse(null));
        return "teacher/teach_shareDoc";
    }
    @GetMapping("/share/delete")
    public String shareDocDelete(long id,long docId){
        shareDocRepository.deleteById(docId);
        return "redirect:/teacher/teach/share-doc?id="+id;
    }

    /*@PostMapping("/doc-add")
    public String courseDocShareProcess(Long id,@RequestParam(value = "file") MultipartFile file){

        String fileName = file.getOriginalFilename();  // 文件名
        String filePath = "D://LecturerPlatform//courses//docs//"+id+"//"; // 上传后的路径
        File dest = new File(filePath + fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Course course=courseService.findCourseById(id);
        ShareDoc shareDoc=new ShareDoc(fileName,"/docs/"+course.getId()+"/"+fileName,course);
        shareDocRepository.save(shareDoc);
        *//*course.setVideoSite("/videos/"+course.getId()+"/"+fileName);//更新视频地址
        courseService.save(course);*//*
        return "redirect:/teacher/teach/share-doc?id="+id;
    }*/

    @ResponseBody
    @PostMapping("/doc-add")
    public String courseDocShareProcess(Long id,@RequestParam(value = "file") MultipartFile file){

        String fileName = file.getOriginalFilename();  // 文件名
        String filePath = "D://LecturerPlatform//courses//docs//"+id+"//"; // 上传后的路径
        File dest = new File(filePath + fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Course course=courseService.findCourseById(id);
        ShareDoc shareDoc=new ShareDoc(fileName,"/docs/"+course.getId()+"/"+fileName,course);
        shareDocRepository.save(shareDoc);
        /*course.setVideoSite("/videos/"+course.getId()+"/"+fileName);//更新视频地址
        courseService.save(course);*/
        return "上传成功！";
    }


    @GetMapping("/video-view")
    public String courseVideoEditView(Long id,Model model,Principal principal){

        Course course=courseService.findCourseById(id);
        /*ReadFile rf = new ReadFile();
        List<String> fileNameList=rf.getFileNameList(id,"videos");
        model.addAttribute("fileNameList",fileNameList);*/
        Collections.sort(course.getTopTitleList());
        model.addAttribute("course",course);
        model.addAttribute("lecturer",lecturerRepository.findById(principal.getName()).orElse(null));
        return "teacher/teach_videoView";
    }


    @ResponseBody
    @PostMapping("/video-add")
    public String courseVideoAddProcess(Long id,@RequestParam(value = "file") MultipartFile file,String topTitle) {//课程id
        System.out.println(topTitle);
        TopTitle topTitle_repo=topTitleRepository.findByTitleAndCourse(topTitle,courseService.findCourseById(id));
        String fileName = file.getOriginalFilename();  // 文件名
        System.out.println(fileName);
        //String filePath = "D://LecturerPlatform//courses//videos//"+id+"//"+topTitle+"//"; // 上传后的路径
        SubTitle subTitle=new SubTitle();
        subTitle.setTitle(fileName);//视频文件名为子目录名
        subTitle.setTopTitle(topTitle_repo);
        //subTitle.setDuration(FileUtil.getVideoDuration(dest));
        subTitle=subTitleRepository.save(subTitle);
        String filePath = "D://LecturerPlatform//courses//videos//"+id+"//"+topTitle_repo.getId()+"//"; // 上传后的路径,id为路径
        //File dest = new File(filePath + fileName);
        File dest = new File(filePath + subTitle.getId()+FileUtil.getFileExtension(fileName));//路径均有id组成，具有唯一性
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
            subTitle.setDuration(FileUtil.getVideoDuration(dest));
            subTitleRepository.save(subTitle);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return subTitle.getTitle();
    }

    @GetMapping("/notice")
    public String addNoticeView(Long id,Model model,Principal principal) {//课程id，发布课程
        Course course=courseService.findCourseById(id);
        Collections.reverse(course.getNotices());
        model.addAttribute("notices", course.getNotices());
        model.addAttribute("course",course);
        model.addAttribute("lecturer",lecturerRepository.findById(principal.getName()).orElse(null));
        return "teacher/teach_notice";
    }

    @PostMapping("/notice")
    public String addNoticeProcess(Long id,String text) {//课程id，发布课程，公告文本
        System.out.println(text);
        Course course=courseService.findCourseById(id);
        Notice notice=new Notice(new Date(),text);
        notice.setCourse(course);
        noticeRepository.save(notice);
        return "redirect:/teacher/teach/notice?id="+id;
    }

    @GetMapping("/notice/delete")
    public String noticeDelete(Long id,Long courseId){
        noticeRepository.deleteById(id);
        return "redirect:/teacher/teach/notice?id="+courseId;
    }

    @GetMapping("/course-state")
    public String courseStateProcess(long id,int enabled){
        Course course=courseService.findCourseById(id);
        if(enabled==1){
            course.setEnabled(0);
        }else {
            course.setEnabled(1);
        }
        courseService.save(course);
        return "redirect:/teacher/teach/list";
    }

    @PostMapping("/search")
    public String searchByKeyword(String keyword,Long categoryId) throws UnsupportedEncodingException {//按关键字模糊查询
        return "redirect:/teacher/teach/list?keyword="+ URLEncoder.encode(keyword, "UTF-8")+"&&categoryId="+categoryId;
    }

    @GetMapping("/list")
    public String courseList(Principal principal, @RequestParam(value = "keyword", defaultValue = "") String keyword,@RequestParam(value = "categoryId", defaultValue = "0")Long categoryId, Model model, @RequestParam(value = "pageNum", defaultValue = "0") int pageNum, @RequestParam(value = "pageSize", defaultValue = "5") int pageSize){

        System.out.println("============================");
        Page<Course> courses;
        if (categoryId == 0) {
            courses= courseService.getCourseListByNameAndLecturer(pageNum, pageSize, keyword,lecturerRepository.findById(principal.getName()).get());
        }else {
            courses = courseService.getCourseListByNameAndLecturerAndCategoryId(pageNum, pageSize, keyword,categoryId,lecturerRepository.findById(principal.getName()).get());
        }
        System.out.println("总页数" + courses.getTotalPages());
        System.out.println("当前页是：" + pageNum);

        System.out.println("分页数据：省略");
        /*Iterator<Course> u = courses.iterator();
        while (u.hasNext()) {
            System.out.println(u.next().toString());
        }*/
        model.addAttribute("courses", courses);
        model.addAttribute("lecturer",lecturerRepository.findById(principal.getName()).orElse(null));
        model.addAttribute("categoryId",categoryId);

        return "teacher/teach_teachList";
    }

    @GetMapping("/comment")
    public String commentView(Long id,Model model,Principal principal){
        Lecturer lecturer=lecturerRepository.findById(principal.getName()).orElse(null);
        model.addAttribute("lecturer",lecturer);
        Course course=courseService.findCourseById(id);
        model.addAttribute("course",course);
        List<Comment> comments= commentRepository.findAllByCourseAndParentId(course,0L);//parentId为0即为问题首发
        model.addAttribute("comments",comments);
        return "teacher/teach_courseComment";
    }

    @PostMapping("/comment-add")
    public String commentAdd(Principal principal,String title,String content,long courseId){
        Lecturer lecturer=lecturerRepository.findById(principal.getName()).orElse(null);
        Comment comment=new Comment(title,content,lecturer.getUsername(),lecturer.getNickname(),lecturer.getImageSite(),courseService.findCourseById(courseId),"","",1);//回复username为空
        commentRepository.save(comment);
        return "redirect:/teacher/teach/comment?id="+courseId;
    }

    @GetMapping("/delete-comment")
    public String deleteQuestion(Long commentId,Long courseId){

        commentRepository.deleteById(commentId);
        return "redirect:/teacher/teach/comment?id="+courseId;
    }

    @Transactional
    @GetMapping("/comment-detail")
    public String commentMoreView(Principal principal,long courseId,long commentId,Model model){//课程id,某人评论细节

        Course course=courseService.findCourseById(courseId);
        model.addAttribute("course",course);
        Comment comment=commentRepository.findById(commentId).orElse(null);
        model.addAttribute("comment",comment);
        List<Comment> comments=comment.getCommentList();
        model.addAttribute("comments",comments);
        model.addAttribute("lecturer", lecturerRepository.findById(principal.getName()).orElse(null));

        //该问题下的该用户评论消息提示置为0，即查阅状态置为1
        int influenceNum=commentRepository.updateViewState(commentId,principal.getName());
        System.out.println(commentId+" "+influenceNum);
        for (Comment comment_each:
                comments) {
            commentRepository.updateViewState(comment_each.getParentId(),principal.getName());
            System.out.println(comment_each.getParentId()+" "+influenceNum);
            for (Comment comment_each2:
                    comment_each.getCommentList()) {
                commentRepository.updateViewState(comment_each2.getParentId(),principal.getName());
                System.out.println(comment_each2.getParentId()+" "+influenceNum);
            }
        }
        return "teacher/teach_courseCommentDetail";
    }

    @PostMapping("/comment-reply")
    public String commentReply(Long rawReplyId,Long levelCommentId,Long replyId,Long courseId,String content,Principal principal){//回复的评论id（是给某人回复，某人的评论id）
        Comment comment=null;
        if (levelCommentId == null) {//一级评论
            comment=commentRepository.findById(replyId).orElse(null);
        }else {
            comment=commentRepository.findById(levelCommentId).orElse(null);
        }
        Course course=courseService.findCourseById(courseId);
        Lecturer lecturer=lecturerRepository.findById(principal.getName()).orElse(null);
        Lecturer reply_lecture=lecturerRepository.findById(comment.getUsername()).orElse(null);
        Student reply_student=studentRepository.findById(comment.getUsername()).orElse(null);
        Comment comment_us;
        if(reply_lecture!=null){
            comment_us=new Comment("",content,lecturer.getUsername(),lecturer.getNickname(),lecturer.getImageSite(),course,comment.getUsername(),reply_lecture.getNickname(),1);//type为1即讲师评论
        }else {
            comment_us=new Comment("",content,lecturer.getUsername(),lecturer.getNickname(),lecturer.getImageSite(),course,comment.getUsername(),reply_student.getNickname(),1);//type为1即讲师评论
        }
        comment_us.setTopComment(commentRepository.findById(rawReplyId).orElse(null));//设置主问题，主要为了消息提示，跳转
        commentRepository.save(comment_us);
        comment.getCommentList().add(comment_us);
        commentRepository.save(comment);

        return "redirect:/teacher/teach/comment-detail?courseId="+courseId+"&&commentId="+rawReplyId;
    }

    @ResponseBody
    @PostMapping("/addTopTitle")
    public String addTopTitle(String title,Long courseId){
        TopTitle topTitle=new TopTitle();
        topTitle.setTitle(title);
        Course course=courseService.findCourseById(courseId);
        topTitle.setCourse(course);
        topTitleRepository.save(topTitle);
        String filePath="D:/LecturerPlatform/courses/videos/"+courseId+"/"+topTitle.getId();
        File dir = new File(filePath);
        if (!dir.exists()) {
            System.out.println(filePath);
            dir.mkdirs();
        }else {
            System.out.println("一级目录未创建");
        }
        return topTitle.getId()+"";
    }

    @ResponseBody
    @PostMapping("/modifyTopTitle")
    public String modifyTopTitle(@RequestParam(value = "id",defaultValue = "0") Long id,String title){
        System.out.println(id);
        System.out.println(title);
        TopTitle topTitle=topTitleRepository.findById(id).orElse(null);
        topTitle.setTitle(title);
        topTitleRepository.save(topTitle);
        return "modify successfully";
    }

    @ResponseBody
    @Transactional
    @PostMapping("/deleteTopTitle")
    public void deleteTopTitle(String title,Long courseId){
        Course course=courseService.findCourseById(courseId);
        topTitleRepository.deleteByTitleAndCourse(title,course);
        //目录未删，仍保留
    }

    @ResponseBody
    @PostMapping("/subTitleList")
    public List<SubTitle> subTitleList(String title,Long courseId){
        System.out.println(title);
        Course course=courseService.findCourseById(courseId);
        TopTitle topTitle=topTitleRepository.findByTitleAndCourse(title,course);
        return topTitle.getSubTitleList();
    }

    @ResponseBody
    @Transactional
    @PostMapping("/subTitleDelete")
    public String subTitleDelete(String topTitle,String subTitle,Long courseId){
        Course course=courseService.findCourseById(courseId);
        TopTitle topTitle_repo=topTitleRepository.findByTitleAndCourse(topTitle,course);
        subTitleRepository.deleteByTitleAndTopTitle(subTitle,topTitle_repo);
        System.out.println(subTitle+"删除成功！");
        return "删除成功！";
    }

}
