package com.nchu.lecturerplatform.controller;

import com.nchu.lecturerplatform.domain.*;
import com.nchu.lecturerplatform.repository.*;
import com.nchu.lecturerplatform.service.CourseService;
import com.nchu.lecturerplatform.service.RecommendService;
import org.bouncycastle.math.raw.Mod;
import org.springframework.data.domain.Page;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {

    @Resource
    private CourseRepository courseRepository;
    @Resource
    private StudentRepository studentRepository;
    @Resource
    private LecturerRepository lecturerRepository;
    @Resource
    private AdminRepository adminRepository;
    @Resource
    private CourseCategoryRepository courseCategoryRepository;
    @Resource
    private CourseRankingRepository courseRankingRepository;
    @Resource
    StarsCourseRankingRepository starsCourseRankingRepository;
    @Resource
    private RecommendService recommendService;
    @Resource
    private SearchKeyRankingRepository searchKeyRankingRepository;
    @Resource
    private RecentLearnCourseRepository recentLearnCourseRepository;
    @Resource
    private LectFollowViewRepository lectFollowViewRepository;
    @Resource
    private CommentRepository commentRepository;
    @Resource
    private FeedbackRepository feedbackRepository;
    @Resource
    private CourseService courseService;


    @GetMapping({"/", "/index"})
    public String courseData(Model model, Principal principal, SecurityContextHolderAwareRequestWrapper request) {
        if (request.isUserInRole("ROLE_LECTURER") || request.isUserInRole("ROLE_ADMIN")) {
            return "redirect:/logout";//若讲师和管理员想访问首页必须自动退出登录
        }
        /*List<Course> courses = courseRepository.findAll();
        model.addAttribute("courses", courses);*/
        List<Course> week_courses = courseRepository.findTop15ByOrderByWeekNumDesc();//每周课程排行
        model.addAttribute("week_courses", week_courses);
        List<Course> courses_newest = courseRepository.findTop10ByOrderByIdDesc();//最新课程排序
        model.addAttribute("courses_newest", courses_newest);
        List<CourseRanking> courseRankingList_hottest = courseRankingRepository.findTop10ByOrderByNumDesc();//最热课程排序,（参加人数)
        model.addAttribute("courseRankingList_hottest", courseRankingList_hottest);
        List<StarsCourseRanking> starsCourseRankingList = starsCourseRankingRepository.findTop10ByOrderByAvgDesc();
        model.addAttribute("starsCourseRankingList", starsCourseRankingList);//高评分课程排序
        String username = "";
        if (principal != null) {
            //推荐系统
            List<CourseRanking> recommendCourseRankingList = recommendService.findAllCourseRankingByUsername(principal.getName());
            model.addAttribute("recommendCourseRankingList", recommendCourseRankingList);//推荐课程排序
            username = principal.getName();
            Student student = studentRepository.findById(principal.getName()).orElse(null);
            model.addAttribute("student", student);
        } else {
            //当用户未登录时，根据就好评推荐（好评排行），登陆后使用推荐系统
            List<StarsCourseRanking> recommendCourseRankingList = starsCourseRankingRepository.findTop10ByOrderByAvgDesc();
            model.addAttribute("recommendCourseRankingList", recommendCourseRankingList);//推荐课程排序
        }
        //找出最近学习课程
        List<RecentLearnCourse> recentLearnCourseList = recentLearnCourseRepository.findTop3ByUsernameOrderByDatetimeDesc(username);
        model.addAttribute("recentLearnCourseList", recentLearnCourseList);
        //最受关注的讲师
        List<LectFollowView> lectFollowViewList = lectFollowViewRepository.findTop5ByOrderByFollowNumDesc();
        model.addAttribute("lectFollowViewList", lectFollowViewList);
        return "home";
    }

    @GetMapping("teachers")
    public String teacherList(Model model,Principal principal) {
        //最受关注的讲师,100人
        if (principal != null) {
            Student student = studentRepository.findById(principal.getName()).orElse(null);
            model.addAttribute("student", student);
        }
        List<LectFollowView> lectFollowViewList = lectFollowViewRepository.findTop100ByOrderByFollowNumDesc();
        model.addAttribute("lectFollowViewList", lectFollowViewList);
        return "teacherList";
    }

    @GetMapping("/feedback")
    public String feedback(Principal principal, Model model){
        if (principal != null) {
            Student student = studentRepository.findById(principal.getName()).orElse(null);
            model.addAttribute("student", student);
        }
        return "feedback";
    }

    @PostMapping("/feedback")
    public String feedbackHandler(String content,String email){
        Feedback feedback=new Feedback(content,email);
        feedbackRepository.save(feedback);
        return "redirect:/feedback";
    }


    @GetMapping("/category")
    @ResponseBody
    public List<CourseCategory> requestCategoryProcess() {

        List<CourseCategory> courseCategoryList = (List<CourseCategory>) courseCategoryRepository.findAll();
        return courseCategoryList;
    }

    @Transactional
    @ResponseBody
    @GetMapping("/message-num")
    public Long returnMessageNum(Principal principal) {
        if (principal == null) {
            return 0L;
        } else {
            return commentRepository.countByReplyUsernameAndViewState(principal.getName(), 0);
        }
    }

    @GetMapping("/search")
    public String search(@RequestParam(value = "courseKey", defaultValue = "") String courseKey, Model model, Principal principal, @RequestParam(value = "pageNum", defaultValue = "0") int pageNum, @RequestParam(value = "pageSize", defaultValue = "5") int pageSize) {
        //List<Course> courseList = courseRepository.findAllByNameContaining(courseKey);
        model.addAttribute("courseKey",courseKey);
        Page<Course> courseList = courseService.getCourseListByNameContaining(pageNum,pageSize,courseKey);
        model.addAttribute("courseList", courseList);
        List<CourseRanking> courseRankingList = courseRankingRepository.findAllByNameContainingOrderByNumDesc(courseKey);
        if (courseRankingList.size() > 5) {//取前五条
            courseRankingList = courseRankingList.subList(0, 5);
        }
        model.addAttribute("courseRankingList", courseRankingList);
        //存入搜索数据，方便数据推荐
        SearchKeyRanking searchKeyRanking = searchKeyRankingRepository.findById(courseKey).orElse(null);
        if (searchKeyRanking != null) {
            searchKeyRanking.setSearchNum(searchKeyRanking.getSearchNum() + 1);
            searchKeyRankingRepository.save(searchKeyRanking);
        } else {
            searchKeyRankingRepository.save(new SearchKeyRanking(courseKey, 1L));
        }
        List<SearchKeyRanking> searchKeyRankingList = searchKeyRankingRepository.findTop8ByOrderBySearchNumDesc();
        model.addAttribute("searchKeyRankingList", searchKeyRankingList);
        if (principal != null) {
            Student student = studentRepository.findById(principal.getName()).orElse(null);
            model.addAttribute("student", student);
        }
        return "course_search";
    }


    @GetMapping("/authenticate")
    public String authenticate(Principal principal) {//判断跳转
        Optional<Student> stu_repo = studentRepository.findById(principal.getName());
        Optional<Lecturer> lect_repo = lecturerRepository.findById(principal.getName());
        Optional<Admin> admin_repo = adminRepository.findById(principal.getName());
        if (stu_repo.isPresent()) {
            return "redirect:/index";
        } else if (lect_repo.isPresent()) {
            return "redirect:/teacher/course/list";
        } else {
            return "redirect:/admin/application/list";
        }
    }

    @GetMapping("/questions")
    public String questions(){

        return "questions";
    }

    @GetMapping("/admin-login")
    public String adminLogin() {
        return "admin_login";
    }


    @GetMapping("/stu-info-center")
    public String info() {
        return "redirect:/student/course/list";
    }

    @GetMapping("/teach-info-center")
    public String teachInfo() {
        return "redirect:/teacher/course/list";
    }

    @GetMapping("/courses-management")
    public String courses() {
        return "redirect:/student/course/list";
    }

}
