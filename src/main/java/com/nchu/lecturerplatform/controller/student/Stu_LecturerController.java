package com.nchu.lecturerplatform.controller.student;

import com.nchu.lecturerplatform.domain.*;
import com.nchu.lecturerplatform.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/student/lecturer")
public class Stu_LecturerController {

    @Resource
    private CourseRepository courseRepository;
    @Resource
    private LecturerRepository lecturerRepository;
    @Resource
    private StudentRepository studentRepository;
    @Resource
    private CourseNewRepository courseNewRepository;
    @Resource
    private LectCourseNewsRepository lectCourseNewsRepository;
    @Resource
    private LectFollowViewRepository lectFollowViewRepository;
    @Resource
    private StarsCourseRankingRepository starsCourseRankingRepository;
    @Resource
    private CourseRankingRepository courseRankingRepository;

    @GetMapping("/info")
    public String info(Principal principal,long id, Model model){//课程id

        Course course=courseRepository.findById(id).orElse(null);
        Lecturer lecturer=course.getLecturer();
        List<Course> courses=lecturerRepository.findById(lecturer.getUsername()).get().getTeachCourses();
        System.out.println(lecturer.getUsername());
        List<LectCourseNews> lectCourseNewsList=lectCourseNewsRepository.findAllByUsername(lecturer.getUsername());
        model.addAttribute("lectCourseNewsList",lectCourseNewsList);
        model.addAttribute("lecturer",lecturer);
        model.addAttribute("courses",courses);
        //粉丝数量
        LectFollowView lectFollowView=lectFollowViewRepository.findById(lecturer.getUsername()).orElse(null);
        if (lectFollowView != null) {
            model.addAttribute("followNum",lectFollowView.getFollowNum());//粉丝数量
        }else {
            model.addAttribute("followNum",0);//粉丝数量
        }
        //判断受否关注
        Student student=studentRepository.findById(principal.getName()).orElse(null);
        if(student.getLecturers().contains(lecturer)){
            model.addAttribute("followState",true);
        }else {
            model.addAttribute("followState",false);
        }
        model.addAttribute("student",student);
        //查找该教师高评分课程
        List<StarsCourseRanking> starsCourseRankingList=starsCourseRankingRepository.findStarsCourseRankingByUsername(lecturer.getUsername());
        if (starsCourseRankingList.size() > 5) {//取前五条
            starsCourseRankingList=starsCourseRankingList.subList(0,5);
        }
        model.addAttribute("starsCourseRankingList",starsCourseRankingList);
        //查找该讲师热门课程
        List<CourseRanking> courseRankingList=courseRankingRepository.findCourseRankingByUsername(lecturer.getUsername());
        if (courseRankingList.size() > 5) {//取前五条
            courseRankingList=courseRankingList.subList(0,5);
        }
        model.addAttribute("courseRankingList",courseRankingList);
        return "student/stu_lecturerInfo";
    }

    @GetMapping("/lect-info")
    public String lectInfo(Principal principal,String username, Model model){//课讲师username


        Lecturer lecturer=lecturerRepository.findById(username).orElse(null);
        List<Course> courses=lecturerRepository.findById(lecturer.getUsername()).get().getTeachCourses();
        System.out.println(lecturer.getUsername());
        List<LectCourseNews> lectCourseNewsList=lectCourseNewsRepository.findAllByUsername(lecturer.getUsername());
        model.addAttribute("lectCourseNewsList",lectCourseNewsList);
        model.addAttribute("lecturer",lecturer);
        model.addAttribute("courses",courses);
        //粉丝数量
        LectFollowView lectFollowView=lectFollowViewRepository.findById(lecturer.getUsername()).orElse(null);
        if (lectFollowView != null) {
            model.addAttribute("followNum",lectFollowView.getFollowNum());//粉丝数量
        }else {
            model.addAttribute("followNum",0);//粉丝数量
        }
        //判断受否关注
        Student student=studentRepository.findById(principal.getName()).orElse(null);
        if(student.getLecturers().contains(lecturer)){
            model.addAttribute("followState",true);
        }else {
            model.addAttribute("followState",false);
        }
        model.addAttribute("student",student);
        //查找该教师高评分课程
        List<StarsCourseRanking> starsCourseRankingList=starsCourseRankingRepository.findStarsCourseRankingByUsername(lecturer.getUsername());
        if (starsCourseRankingList.size() > 5) {//取前五条
            starsCourseRankingList=starsCourseRankingList.subList(0,5);
        }
        model.addAttribute("starsCourseRankingList",starsCourseRankingList);
        //查找该讲师热门课程
        List<CourseRanking> courseRankingList=courseRankingRepository.findCourseRankingByUsername(lecturer.getUsername());
        if (courseRankingList.size() > 5) {//取前五条
            courseRankingList=courseRankingList.subList(0,5);
        }
        model.addAttribute("courseRankingList",courseRankingList);
        return "student/stu_lecturerInfo";
    }


    @GetMapping("/follow-process")//讲师主页那个关注处理，关注不关注，不关注变关注
    public String followProcess(Principal principal,String username,Model model){//讲师username
        Lecturer lecturer=lecturerRepository.findById(username).orElse(null);
        Student student=studentRepository.findById(principal.getName()).orElse(null);
        model.addAttribute("student",student);
        int followNum=lecturer.getFollowNum();
        if(student.getLecturers().contains(lecturer)){//取消关注
            student.getLecturers().remove(lecturer);
            lecturer.setFollowNum(followNum-1);
            //model.addAttribute("followState",false);
        }else {//关注
            student.getLecturers().add(lecturer);
            lecturer.setFollowNum(followNum+1);
            //model.addAttribute("followState",true);
        }
        lecturerRepository.save(lecturer);
        studentRepository.save(student);
        /*List<Course> courses=lecturer.getTeachCourses();
        model.addAttribute("lecturer",lecturer);
        model.addAttribute("courses",courses);*/
        return "redirect:/student/lecturer/lect-info?username="+username;
    }

    @GetMapping("/follow-list-info")//关注列表，查看讲师个人信息
    public String followListInfoView(Principal principal,String username,Model model){//讲师username
        Lecturer lecturer=lecturerRepository.findById(username).orElse(null);
        List<Course> courses=lecturer.getTeachCourses();
        List<LectCourseNews> lectCourseNewsList=lectCourseNewsRepository.findAllByUsername(lecturer.getUsername());
        model.addAttribute("lectCourseNewsList",lectCourseNewsList);
        model.addAttribute("lecturer",lecturer);
        model.addAttribute("courses",courses);
        //粉丝数量
        LectFollowView lectFollowView=lectFollowViewRepository.findById(lecturer.getUsername()).orElse(null);
        if (lectFollowView != null) {
            model.addAttribute("followNum",lectFollowView.getFollowNum());//粉丝数量
        }else {
            model.addAttribute("followNum",0);//粉丝数量
        }
        //判断受否关注
        Student student=studentRepository.findById(principal.getName()).orElse(null);
        model.addAttribute("student",student);
        if(student.getLecturers().contains(lecturer)){
            model.addAttribute("followState",true);
        }else {
            model.addAttribute("followState",false);
        }
        return "student/stu_lecturerInfo";
    }

    @GetMapping("/follow-list-process")//关注讲师列表-》取消关注
    public String followListProcess(Principal principal,String username,Model model){//讲师username
        Lecturer lecturer=lecturerRepository.findById(username).orElse(null);
        Student student=studentRepository.findById(principal.getName()).orElse(null);
        student.getLecturers().remove(lecturer);
        model.addAttribute("followState",false);
        studentRepository.save(student);
        model.addAttribute("student",student);
        model.addAttribute("lecturers",student.getLecturers());
        return "student/stu_lecturerFollowList";
    }

    @GetMapping("/follow-list")
    public String followListView(Principal principal,Model model){//查看关注讲师列表

        Student student=studentRepository.findById(principal.getName()).orElse(null);
        model.addAttribute("student",student);
        model.addAttribute("lecturers",student.getLecturers());
        return "student/stu_lecturerFollowList";
    }



}
