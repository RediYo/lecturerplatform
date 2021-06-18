package com.nchu.lecturerplatform.controller.student;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.nchu.lecturerplatform.config.AlipayConfig;
import com.nchu.lecturerplatform.domain.*;
import com.nchu.lecturerplatform.repository.*;
import com.nchu.lecturerplatform.service.CourseService;
import com.nchu.lecturerplatform.service.PayService;
import com.nchu.lecturerplatform.service.RecommendService;
import com.nchu.lecturerplatform.util.CourseStudyStatus;
import com.nchu.lecturerplatform.util.ReadFile;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/student/course")
public class Stu_CourseController {

    @Resource
    private CourseService courseService;
    @Resource
    private StudentRepository studentRepository;
    @Resource
    private StuCourseRepository stuCourseRepository;
    @Resource
    private StuCourseEvaluationRepository stuCourseEvaluationRepository;
    @Resource
    private StuLectCourseNewsRepository stuLectCourseNewsRepository;
    @Resource
    private CommentRepository commentRepository;
    @Resource
    private RecommendService recommendService;
    @Resource
    private LecturerRepository lecturerRepository;
    @Resource
    private RecentLearnCourseRepository recentLearnCourseRepository;
    @Resource
    private StarsCourseRankingRepository starsCourseRankingRepository;
    @Resource
    private CourseRankingRepository courseRankingRepository;
    @Resource
    private TopTitleRepository topTitleRepository;
    @Resource
    private SubTitleRepository subTitleRepository;
    @Resource
    private NoteRepository noteRepository;
    @Resource
    private ContinueStudyRepository continueStudyRepository;
    @Resource
    private PayService payService;
    @Resource
    private OrdersRepository ordersRepository;
    @Resource
    private ShareDocRepository shareDocRepository;
    @Resource
    private CourseStudyStatus courseStudyStatus;


    @GetMapping("/notes")
    public String notesView(Principal principal, Long id, Model model) {//课程id
        Student student = studentRepository.findById(principal.getName()).orElse(null);
        model.addAttribute("student", student);
        Course course = courseService.findCourseById(id);
        model.addAttribute("course", course);
        List<Note> noteList = noteRepository.findByCourseAndUsername(course, principal.getName());
        model.addAttribute("noteList", noteList);
        return "/student/stu_courseNotes";
    }

    @GetMapping("/view")
    public String view(Principal principal, long id, Model model) {//课程id
        Student student = studentRepository.findById(principal.getName()).orElse(null);
        model.addAttribute("student", student);
        Course course = courseService.findCourseById(id);
        if (student.getStuCourses().contains(course)) {
            model.addAttribute("addState", 1);
        } else {
            model.addAttribute("addState", 0);
        }
        List<StuCourseEvaluation> stuCourseEvaluationList = stuCourseEvaluationRepository.findAllByCourseIdAndEvaluationState(id, 1);
        model.addAttribute("stuCourseEvaluationList", stuCourseEvaluationList);
        if (student.getStuCollections().contains(course)) {
            model.addAttribute("collectState", 1);
        } else {
            model.addAttribute("collectState", 0);
        }
        Collections.sort(course.getTopTitleList());
        model.addAttribute("course", course);
        StarsCourseRanking starsCourseRanking = starsCourseRankingRepository.findById(id).orElse(new StarsCourseRanking(0,0));
        model.addAttribute("starsCourseRanking", starsCourseRanking);
        List<CourseRanking> courseRankingList = courseRankingRepository.findAllByCategoryIdOrderByNumDesc(courseService.findCourseById(id).getCourseCategory().getId());
        model.addAttribute("courseRankingList", courseRankingList);
        //浏览课程，类别权重+1
        recommendService.StudentCategoryWeightProcess(principal.getName(), course.getCourseCategory().getId(), 1);
        List<Course> recommendCourseList = recommendService.recommend(principal.getName(), id);//协同过滤算法
        model.addAttribute("recommendCourseList", recommendCourseList);

        return "student/stu_courseView";
    }

    @GetMapping("/collection")
    public String collection(Principal principal, Model model) {//课程id

        Student student = studentRepository.findById(principal.getName()).orElse(null);
        model.addAttribute("courses", student.getStuCollections());
        model.addAttribute("student", student);
        return "student/stu_courseCollection";
    }

    @GetMapping("/collection-cancel")
    public String collectionCancel(Principal principal, long id, Model model) {//课程id

        Student student = studentRepository.findById(principal.getName()).orElse(null);
        Course course = courseService.findCourseById(id);
        student.getStuCollections().remove(course);
        studentRepository.save(student);
        return "redirect:/student/course/collection";
    }

    @GetMapping("/collect")
    public String collect(Principal principal, long id, Model model) {//课程id

        Student student = studentRepository.findById(principal.getName()).orElse(null);
        Course course = courseService.findCourseById(id);
        student.getStuCollections().add(course);
        studentRepository.save(student);
        return "redirect:/student/course/view?id=" + id;
    }

    @GetMapping("/collect-cancel")
    public String collectCancel(Principal principal, long id, Model model) {//课程id

        Student student = studentRepository.findById(principal.getName()).orElse(null);
        Course course = courseService.findCourseById(id);
        student.getStuCollections().remove(course);
        studentRepository.save(student);
        return "redirect:/student/course/view?id=" + id;
    }

    @GetMapping("/add")
    public String add(Principal principal, long id, Model model) {//课程id

        Student student = studentRepository.findById(principal.getName()).orElse(null);
        model.addAttribute("student", student);
        Course course = courseService.findCourseById(id);
        if(!student.getStuCourses().contains(course)){
            int weekNum = course.getWeekNum();
            course.setWeekNum(weekNum + 1);//每周参加该课程的人数+1
            student.getStuCourses().add(course);
            studentRepository.save(student);
            //参加课程，类别权重+10
            recommendService.StudentCategoryWeightProcess(principal.getName(), course.getCourseCategory().getId(), 10);
        }

        /*model.addAttribute("addState", 1);
        model.addAttribute("courseState", course.getEnabled());
        model.addAttribute("course", course);*/
        return "redirect:/student/course/view?id="+id;
    }

    @GetMapping("/learn")
    public String courseListView(Principal principal, long id, Model model) {

        Student student = studentRepository.findById(principal.getName()).orElse(null);
        model.addAttribute("student", student);
        Course course = courseService.findCourseById(id);
        ReadFile rf = new ReadFile();
        List<String> fileNameList = rf.getFileNameList(id, "videos");
        model.addAttribute("fileNameList", fileNameList);
        model.addAttribute("course", course);
        Collections.sort(course.getTopTitleList());
        //保存最近学习的课程
        recentLearnCourseRepository.save(new RecentLearnCourse(id, course.getName(), principal.getName()));

        //继续学习
        StuCourse stuCourse = stuCourseRepository.findByCourseIdAndUsername(id, principal.getName());
        if (stuCourse == null) {
            return "redirect:/index";
        }
        Long currentVideoId = stuCourse.getCurrentVideoId();
        if (currentVideoId == null) {
            currentVideoId = 0L;
        }
        System.out.println(currentVideoId + " " + principal.getName());
        ContinueStudy continueStudy = continueStudyRepository.findByIdAndUsername(currentVideoId, principal.getName());
        model.addAttribute("continueStudy", continueStudy);
        return "student/stu_courseLearn";
    }

    @GetMapping("/notice")
    public String courseNoticeView(long id, Model model, Principal principal) {//课程id

        Student student = studentRepository.findById(principal.getName()).orElse(null);
        model.addAttribute("student", student);
        StuCourse stuCourse = stuCourseRepository.findByUsernameAndCourseId(principal.getName(), id);
        if(stuCourse==null){
            return "redirect:/index";
        }
        model.addAttribute("evaluationState", stuCourse.getEvaluationState());
        model.addAttribute("evaluationStars", stuCourse.getEvaluationStars());
        model.addAttribute("evaluationContent", stuCourse.getEvaluation());
        Course course = courseService.findCourseById(id);
        List<Notice> notices = course.getNotices();
        model.addAttribute("course", course);
        Collections.reverse(notices);
        model.addAttribute("notices", notices);
        return "student/stu_courseNotice";
    }

    @PostMapping("/evaluate")
    public String courseEvaluate(Model model, String evaluation, long id, int stars, Principal principal) {//课程id
        StuCourse stuCourse = stuCourseRepository.findByUsernameAndCourseId(principal.getName(), id);
        if (stuCourse == null) {
            return "redirect:/index";
        }
        stuCourse.setEvaluation(evaluation);
        stuCourse.setEvaluationState(1);
        stuCourse.setEvaluationStars(stars);
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        stuCourse.setEvaluationTime(sdf.format(d));
        stuCourseRepository.save(stuCourse);
        Student student = studentRepository.findById(principal.getName()).orElse(null);
        model.addAttribute("student", student);
        return "redirect:/student/course/notice?id=" + id;//每次评价后跳转到公告页
    }

    @GetMapping("/list")
    public String myCourse(Principal principal, Model model) {
        List<Course> courses = studentRepository.findById(principal.getName()).get().getStuCourses();
        Collections.reverse(courses);
        model.addAttribute("courses", courses);
        model.addAttribute("student", studentRepository.findById(principal.getName()).get());
        return "student/stu_courseList";
    }

    @GetMapping("/news")
    public String courseNews(Principal principal, Model model) {
        List<StuLectCourseNews> stuLectCourseNewsList = stuLectCourseNewsRepository.findAllByStuUsername(principal.getName());
        model.addAttribute("student", studentRepository.findById(principal.getName()).orElse(null));
        model.addAttribute("stuLectCourseNewsList", stuLectCourseNewsList);
        return "student/stu_courseNews";
    }

    @GetMapping("/share")
    public String shareDoc(Principal principal, long id, Model model) {

        Student student = studentRepository.findById(principal.getName()).orElse(null);
        model.addAttribute("student", student);
        Course course = courseService.findCourseById(id);
        /*ReadFile rf = new ReadFile();
        List<String> fileNameList = rf.getFileNameList(id, "docs");
        model.addAttribute("fileNameList", fileNameList);*/
        model.addAttribute("course", course);
        return "student/stu_courseShareDoc";
    }

    @GetMapping("/comment")
    public String commentView(Long id, @RequestParam(value = "questionSelect", defaultValue = "0") int questionSelect, @RequestParam(value = "keyword", defaultValue = "") String keyword, Model model, Principal principal) {

        Student student = studentRepository.findById(principal.getName()).orElse(null);
        model.addAttribute("student", student);
        Course course = courseService.findCourseById(id);
        model.addAttribute("course", course);
        List<Comment> comments = null;
        if (questionSelect == 0) {
            comments = commentRepository.findAllByCourseAndParentIdAndTitleContaining(course, 0L, keyword);//parentId为0即为问题首发,
        } else {//我的问题
            comments = commentRepository.findAllByCourseAndParentIdAndUsernameAndTitleContaining(course, 0L, principal.getName(), keyword);
        }
        model.addAttribute("comments", comments);
        model.addAttribute("questionSelect", questionSelect);
        return "student/stu_courseComment";
    }

    @PostMapping("/comment/search")
    public String searchByKeyword(Long id, String keyword, int questionSelect) throws UnsupportedEncodingException {//按关键字模糊查询,课程id
        return "redirect:/student/course/comment?id=" + id + "&&questionSelect=" + questionSelect + "&&keyword=" + URLEncoder.encode(keyword, "UTF-8");
    }

    @PostMapping("/comment-add")
    public String commentAdd(Principal principal, Model model, String title, String content, long courseId, @RequestParam(value = "videoSite", defaultValue = "") String videoSite) {

        Student student = studentRepository.findById(principal.getName()).orElse(null);
        model.addAttribute("student", student);
        Comment comment = new Comment(title, content, student.getUsername(), student.getNickname(), student.getImageSite(), courseService.findCourseById(courseId), "", "", 0);//回复username为空
        comment.setVideoSite(videoSite);
        commentRepository.save(comment);
        return "redirect:/student/course/comment?id=" + courseId;

    }

    @ResponseBody
    @PostMapping("/learn/comment-add")
    public String learnCommentAdd(Principal principal, Model model, String title, String content, long courseId, @RequestParam(value = "videoSite", defaultValue = "") String videoSite) {

        Student student = studentRepository.findById(principal.getName()).orElse(null);
        model.addAttribute("student", student);
        Comment comment = new Comment(title, content, student.getUsername(), student.getNickname(), student.getImageSite(), courseService.findCourseById(courseId), "", "", 0);//回复username为空
        comment.setVideoSite(videoSite);
        commentRepository.save(comment);
        return "Add successfully";

    }

    @Transactional
    @GetMapping("/comment-detail")
    public String commentMoreView(Principal principal, long courseId, long commentId, Model model) {//课程id,某人评论细节,commentId为主题评论id

        Student student = studentRepository.findById(principal.getName()).orElse(null);
        model.addAttribute("student", student);
        Course course = courseService.findCourseById(courseId);
        model.addAttribute("course", course);
        Comment comment = commentRepository.findById(commentId).orElse(null);
        model.addAttribute("comment", comment);
        List<Comment> relevant_commentList = commentRepository.findAllByTitleContaining(comment.getTitle());
        model.addAttribute("relevant_commentList", relevant_commentList);
        List<Comment> comments = comment.getCommentList();
        model.addAttribute("comments", comments);

        //该问题下的该用户评论消息提示置为0，即查阅状态置为1
        int influenceNum = commentRepository.updateViewState(commentId, principal.getName());
        System.out.println(commentId + " " + influenceNum);
        for (Comment comment_each :
                comments) {
            commentRepository.updateViewState(comment_each.getParentId(), principal.getName());
            System.out.println(comment_each.getParentId() + " " + influenceNum);
            for (Comment comment_each2 :
                    comment_each.getCommentList()) {
                commentRepository.updateViewState(comment_each2.getParentId(), principal.getName());
                System.out.println(comment_each2.getParentId() + " " + influenceNum);
            }
        }

        return "student/stu_courseCommentDetail";
    }

    @PostMapping("/comment-reply")
    public String commentReply(Model model, Long rawReplyId, Long levelCommentId, Long replyId, Long courseId, String content, Principal principal) {//回复的评论id（是给某人回复，某人的评论id）
        Comment comment = null;
        if (levelCommentId == null) {//为null为一级评论
            comment = commentRepository.findById(replyId).orElse(null);
        } else {//二级评论得放到一级评论levelCommentId下
            comment = commentRepository.findById(levelCommentId).orElse(null);
        }
        Course course = courseService.findCourseById(courseId);
        Student student = studentRepository.findById(principal.getName()).orElse(null);
        Lecturer reply_lecturer = lecturerRepository.findById(comment.getUsername()).orElse(null);
        Student reply_student = studentRepository.findById(comment.getUsername()).orElse(null);
        Comment comment_us = null;
        if (reply_lecturer != null) {//为了显示头像，因为学生与讲师为不同的表
            comment_us = new Comment("", content, student.getUsername(), student.getNickname(), student.getImageSite(), course, comment.getUsername(), reply_lecturer.getNickname(), 0);//type为学生评论
            System.out.println("回复的用户名为讲师" + reply_lecturer.getNickname());
        } else if (reply_student != null) {
            comment_us = new Comment("", content, student.getUsername(), student.getNickname(), student.getImageSite(), course, comment.getUsername(), reply_student.getNickname(), 0);//type为0即学生评论
            System.out.println("回复的用户名为学生" + reply_student.getNickname());
        }
        comment_us.setTopComment(commentRepository.findById(rawReplyId).orElse(null));//设置主问题，主要为了消息提示，跳转
        commentRepository.save(comment_us);
        comment.getCommentList().add(comment_us);
        commentRepository.save(comment);

        return "redirect:/student/course/comment-detail?courseId=" + courseId + "&&commentId=" + rawReplyId;
    }


    @GetMapping("/delete-comment")
    public String deleteQuestion(Long commentId, Long courseId) {

        commentRepository.deleteById(commentId);
        return "redirect:/student/course/comment?id=" + courseId;
    }

    @ResponseBody
    @GetMapping("/learn/delete-comment")
    public String deleteComment(Long commentId) {
        Comment comment=commentRepository.findById(commentId).orElse(null);
        if (comment != null) {
            commentRepository.deleteById(commentId);
        }
        return "delete successfully";
    }


    @GetMapping("/video-learn")
    public String videoLearn(Principal principal, Model model, long id, Long topId, Long subId, String videoSite) {//课程id，选中的视频地址

        Student student = studentRepository.findById(principal.getName()).orElse(null);
        model.addAttribute("student", student);
        Course course = courseService.findCourseById(id);
        Collections.sort(course.getTopTitleList());
        /*ReadFile rf = new ReadFile();
        List<String> fileNameList = rf.getFileNameList(id, "videos");
        model.addAttribute("fileNameList", fileNameList);*/
        model.addAttribute("course", course);
        model.addAttribute("videoSite", videoSite);
        //TopTitle topTitle_repo = topTitleRepository.findByTitleAndCourse(topTitle, course);
        TopTitle topTitle_repo = topTitleRepository.findById(topId).orElse(null);
        model.addAttribute("topTitle", topTitle_repo);
        //SubTitle subTitle_repo = subTitleRepository.findByTopTitleAndTitle(topTitle_repo, subTitle);
        SubTitle subTitle_repo = subTitleRepository.findById(subId).orElse(null);
        model.addAttribute("subTitle", subTitle_repo);

        //问答
        String video_site = course.getId() + String.valueOf(topTitle_repo.getId()) + subTitle_repo.getId() + "";
        System.out.println(video_site);
        List<Comment> comments = commentRepository.findAllByCourseAndParentIdAndVideoSite(course, 0L, video_site);//parentId为0即为问题首发,
        model.addAttribute("comments", comments);
        //笔记
        Note note = noteRepository.findByCourseAndTopTitleAndSubTitle(course, topTitle_repo, subTitle_repo);
        if (note == null) {
            note = new Note();
            note.setContent("");
        }
        model.addAttribute("note", note);
        //继续学习
        ContinueStudy continueStudy = continueStudyRepository.findBySubTitleAndUsername(subTitle_repo, principal.getName());
        if (continueStudy == null) {
            continueStudy = new ContinueStudy(0L, principal.getName(), course, topTitle_repo, subTitle_repo);
            continueStudyRepository.save(continueStudy);
        }
        model.addAttribute("continueStudy", continueStudy);
        StuCourse stuCourse = stuCourseRepository.findByCourseIdAndUsername(id, principal.getName());
        stuCourse.setCurrentVideoId(continueStudy.getId());
        stuCourseRepository.save(stuCourse);
        return "student/stu_videoLearn";
    }

    @ResponseBody
    @GetMapping("/continueTimeUpdate")
    public String updateTime(Principal principal, Long courseId, Long topId, Long subId, Long currentTime) {

        Course course = courseService.findCourseById(courseId);
        TopTitle topTitle = topTitleRepository.findById(topId).orElse(null);
        SubTitle subTitle = subTitleRepository.findById(subId).orElse(null);
        ContinueStudy continueStudy = continueStudyRepository.findBySubTitleAndUsername(subTitle, principal.getName());
        if (continueStudy == null) {
            continueStudy = new ContinueStudy(currentTime, principal.getName(), course, topTitle, subTitle);
        } else {
            continueStudy.setContinueTime(currentTime);
        }

        continueStudyRepository.save(continueStudy);
        return "save successfully";
    }

    @ResponseBody
    @PostMapping("/note")
    public String updateNote(Principal principal, Long courseId, Long topId, Long subId, String content) {

        Course course = courseService.findCourseById(courseId);
        TopTitle topTitle = topTitleRepository.findById(topId).orElse(null);
        SubTitle subTitle = subTitleRepository.findById(subId).orElse(null);
        Note note = noteRepository.findByCourseAndTopTitleAndSubTitleAndUsername(course, topTitle, subTitle, principal.getName());
        if (note == null) {//暂无笔记
            if (!content.equals("")) {//笔记内容不为空
                //创建笔记
                note = new Note(content, principal.getName(), course, topTitle, subTitle);
                noteRepository.save(note);
            }
        } else {
            if (content.equals("")) {//笔记内容为空,删除笔记
                noteRepository.delete(note);
            }else {
                note.setContent(content);
                noteRepository.save(note);
            }
        }
        return "save successfully";
    }

    @PostMapping("/update/note")
    @ResponseBody
    public String updateListNote(@RequestParam(value = "id", defaultValue = "0") Long id, String content) {
        System.out.println(id);
        Note note = noteRepository.findById(id).orElse(null);
        if (note != null) {
            note.setContent(content);
            noteRepository.save(note);
        }
        return content;
    }

    @GetMapping("/note/delete")
    @ResponseBody
    public String noteDelete(@RequestParam(value = "id", defaultValue = "0") Long id) {
        System.out.println(id);
        noteRepository.deleteById(id);
        return "delete successfully";
    }

    @GetMapping("/exit")
    public String exit(Principal principal, Long id) {//课程id，退出课程
        Student student = studentRepository.findById(principal.getName()).orElse(null);
        Course course = courseService.findCourseById(id);
        student.getStuCourses().remove(course);
        studentRepository.save(student);
        return "redirect:/student/course/list";
    }

    @GetMapping("/orders")
    public String myOrders(Principal principal, Model model) {

        Student student = studentRepository.findById(principal.getName()).orElse(null);
        model.addAttribute("student", student);
        List<Order> orderList = ordersRepository.findAllByStudentAndPayStatus(student,1);
        model.addAttribute("orderList", orderList);
        return "/student/stu_myOrders";
    }

    @GetMapping("/pay")
    public String pay(Principal principal, Long id, Model model) {//课程id
        Student student = studentRepository.findById(principal.getName()).orElse(null);
        model.addAttribute("student", student);
        Course course = courseService.findCourseById(id);
        model.addAttribute("course", course);
        Order order = ordersRepository.findByStudentAndCourse(student, course);
        if (order != null) {
            return "redirect:/student/course/add?id=" + id;
        } else {
            return "/student/stu_coursePay";
        }

    }

    @ResponseBody
    @GetMapping("/AliPay")
    public String payForCourse(Principal principal, Long id) throws AlipayApiException {

        Course course = courseService.findCourseById(id);
        Student student = studentRepository.findById(principal.getName()).orElse(null);
        //学生暂时不加入课程，只填入订单
        /*student.getStuCourses().add(course);//加入了课程
        studentRepository.save(student);*/
        Order order = new Order(course, student);
        Order order_repo = ordersRepository.save(order);
        return payService.pay(order_repo);
    }

    @ResponseBody
    @PostMapping("/alipay/notify")//异步通知，非公网无法接受，有点问题，改为同步
    public void notifyAlipay(String charset,String out_trade_no,String method,String total_amount,String sign,String trade_no,String auth_app_id,String version,String app_id,String sign_type,String seller_id,String timestamp) throws AlipayApiException {

        Map<String, String> paramsMap = new HashMap<>();  //将异步通知中收到的所有参数都存放到 map 中
        paramsMap.put("charset", charset);
        paramsMap.put("out_trade_no", out_trade_no);
        paramsMap.put("method", method);
        paramsMap.put("total_amount", total_amount);
        paramsMap.put("sign", sign);
        paramsMap.put("trade_no", trade_no);
        paramsMap.put("auth_app_id", auth_app_id);
        paramsMap.put("version", version);
        paramsMap.put("app_id", app_id);
        paramsMap.put("sign_type", sign_type);
        paramsMap.put("seller_id", seller_id);
        paramsMap.put("timestamp", timestamp);
        boolean  signVerified = AlipaySignature.rsaCheckV1(paramsMap, AlipayConfig.alipay_public_key, "UTF-8", AlipayConfig.sign_type);  //调用SDK验证签名
        if (signVerified){
            //验签成功后，按照支付结果异步通知中的描述，对支付结果中的业务内容进行二次校验，校验成功后在response中返回success并继续商户自身业务处理，校验失败返回failure
            //订单支付状态改为1
            Order order=ordersRepository.findById(Long.valueOf(out_trade_no)).orElse(null);
            order.setPayStatus(1);
            ordersRepository.save(order);
            Course course = order.getCourse();
            Student student=order.getStudent();
            student.getStuCourses().add(course);//正式加入课程
            studentRepository.save(student);
            System.out.println("支付成功！");
        } else {
            //验签失败则记录异常日志，并在response中返回failure.
            System.out.println("订单交易失败------");
        }
        //return " a li pay notify ";
    }

    //@ResponseBody
    @GetMapping("/alipay/return")//同步通知
    public String returnAlipay(String charset,String out_trade_no,String method,String total_amount,String sign,String trade_no,String auth_app_id,String version,String app_id,String sign_type,String seller_id,String timestamp) throws AlipayApiException {

        Map<String, String> paramsMap = new HashMap<>();  //将异步通知中收到的所有参数都存放到 map 中
        paramsMap.put("charset", charset);
        paramsMap.put("out_trade_no", out_trade_no);
        paramsMap.put("method", method);
        paramsMap.put("total_amount", total_amount);
        paramsMap.put("sign", sign);
        paramsMap.put("trade_no", trade_no);
        paramsMap.put("auth_app_id", auth_app_id);
        paramsMap.put("version", version);
        paramsMap.put("app_id", app_id);
        paramsMap.put("sign_type", sign_type);
        paramsMap.put("seller_id", seller_id);
        paramsMap.put("timestamp", timestamp);
        boolean  signVerified = AlipaySignature.rsaCheckV1(paramsMap, AlipayConfig.alipay_public_key, "UTF-8", AlipayConfig.sign_type);  //调用SDK验证签名
        if (signVerified){
            //验签成功后，按照支付结果异步通知中的描述，对支付结果中的业务内容进行二次校验，校验成功后在response中返回success并继续商户自身业务处理，校验失败返回failure
            //订单支付状态改为1
            Order order=ordersRepository.findById(Long.valueOf(out_trade_no)).orElse(null);
            order.setPayStatus(1);
            ordersRepository.save(order);
            System.out.println("支付成功！");
            //讲师余额+
            Course course=order.getCourse();
            Lecturer lecturer=course.getLecturer();
            int money=lecturer.getMoney();
            lecturer.setMoney(money+course.getMoney());
            lecturerRepository.save(lecturer);

            //Course course = order.getCourse();
            Student student=order.getStudent();
            student.getStuCourses().add(course);//正式加入课程
            studentRepository.save(student);
        } else {
            //验签失败则记录异常日志，并在response中返回failure.
            System.out.println("订单交易失败------");
        }
        //return " a li pay notify ";
        return "redirect:/student/course/orders";
    }

}
