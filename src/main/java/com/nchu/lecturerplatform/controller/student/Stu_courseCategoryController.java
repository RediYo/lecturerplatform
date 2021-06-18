package com.nchu.lecturerplatform.controller.student;

import com.nchu.lecturerplatform.domain.CourseCategory;
import com.nchu.lecturerplatform.domain.CourseRanking;
import com.nchu.lecturerplatform.domain.Student;
import com.nchu.lecturerplatform.repository.CourseCategoryRepository;
import com.nchu.lecturerplatform.repository.CourseRankingRepository;
import com.nchu.lecturerplatform.repository.CourseRepository;
import com.nchu.lecturerplatform.repository.StudentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/student/category")
public class Stu_courseCategoryController {

    @Resource
    private CourseRepository courseRepository;
    @Resource
    private CourseCategoryRepository courseCategoryRepository;
    @Resource
    private CourseRankingRepository courseRankingRepository;
    @Resource
    private StudentRepository studentRepository;

    @GetMapping
    public String courseCategoryView(Principal principal,long id, Model model){//分类id

        Student student=studentRepository.findById(principal.getName()).orElse(null);
        model.addAttribute("student",student);
        CourseCategory courseCategory=courseCategoryRepository.findById(id).orElse(null);
        model.addAttribute("courseCategory",courseCategory);
        List<CourseRanking> courseRankingList_hottest=courseRankingRepository.findAllByCategoryIdOrderByNumDesc(id);
        model.addAttribute("courseRankingList_hottest",courseRankingList_hottest);
        List<CourseRanking> courseRankingList_newest=courseRankingRepository.findAllByCategoryIdOrderByIdDesc(id);//id降序排列，即为最新课程排列，因为创建课程id越来越大
        model.addAttribute("courseRankingList_newest",courseRankingList_newest);
        return "student/stu_courseCategory";
    }

    @GetMapping("/hottest")
    public String courseHottest(Model model, Principal principal, @RequestParam(name = "id",defaultValue = "0") Long id){//目录id
        Student student=studentRepository.findById(principal.getName()).orElse(null);
        List<CourseCategory> courseCategoryList=(List<CourseCategory>)courseCategoryRepository.findAll();
        model.addAttribute("courseCategoryList",courseCategoryList);
        model.addAttribute("student",student);
        if (id == 0) {//查找全部
            List<CourseRanking> courseRankingList_hottest=courseRankingRepository.findTop60ByOrderByNumDesc();//只显示前60
            model.addAttribute("courseRankingList_hottest",courseRankingList_hottest);
            model.addAttribute("flagRed","0");
        }else {//分目录查找
            List<CourseRanking> courseRankingList_hottest=courseRankingRepository.findTop60ByCategoryIdOrderByNumDesc(id);
            model.addAttribute("courseRankingList_hottest",courseRankingList_hottest);
            model.addAttribute("flagRed",id);
        }

        return "student/stu_categoryHottest";
    }

}
