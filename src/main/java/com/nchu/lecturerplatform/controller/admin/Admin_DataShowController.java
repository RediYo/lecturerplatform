package com.nchu.lecturerplatform.controller.admin;

import com.nchu.lecturerplatform.domain.CourseCategory;
import com.nchu.lecturerplatform.repository.*;
import com.nchu.lecturerplatform.util.CategoryData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/admin/data")
@Controller
public class Admin_DataShowController {


    @Resource
    private LecturerRepository lecturerRepository;
    @Resource
    private StudentRepository studentRepository;
    @Resource
    private CourseRepository courseRepository;
    @Resource
    private CourseCategoryRepository courseCategoryRepository;
    @Resource
    private OrdersRepository ordersRepository;

    @GetMapping("/show")
    public String show(){

        return "admin/admin_dataShow";
    }


    @GetMapping("/user-data-show")
    @ResponseBody
    public List<Long> userDataShow(){
        List<Long> totals=new ArrayList<>();
        totals.add(lecturerRepository.count());
        totals.add(studentRepository.count());
        totals.add(courseRepository.count());
        totals.add(ordersRepository.count());
        return totals;
    }

    @GetMapping("/category-data-show")
    @ResponseBody
    public CategoryData categoryDataShow(){
        List<String> categories=new ArrayList<>();
        List<CourseCategory> courseCategoryList=(List<CourseCategory>)courseCategoryRepository.findAll();
        for (CourseCategory courseCategory: courseCategoryList) {
            categories.add(courseCategory.getCategory());
        }
        List<Long> categoryNums=new ArrayList<>();
        for (CourseCategory courseCategory :
                courseCategoryList) {
            categoryNums.add(courseRepository.countByCourseCategory(courseCategory));
        }
        CategoryData categoryData=new CategoryData(categories,categoryNums);

        return categoryData;
    }
}
