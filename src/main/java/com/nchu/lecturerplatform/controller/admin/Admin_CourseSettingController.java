package com.nchu.lecturerplatform.controller.admin;

import com.nchu.lecturerplatform.domain.CourseCategory;
import com.nchu.lecturerplatform.repository.CourseCategoryRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

@RequestMapping("/admin/setting")
@Controller
public class Admin_CourseSettingController {

    @Resource
    private CourseCategoryRepository courseCategoryRepository;

    @GetMapping
    public String courseSettingCategory(Model model){//目前只有课程分类设置

        List<CourseCategory> courseCategoryList=(List<CourseCategory>)courseCategoryRepository.findAll();
        model.addAttribute("courseCategoryList",courseCategoryList);
        return "admin/admin_courseSetting";
    }

    @PostMapping("/delete")
    public String courseSettingCategoryDelete(long deleteCategoryId){//目前只有课程分类设置

        courseCategoryRepository.deleteById(deleteCategoryId);
        return "redirect:/admin/setting";
    }

    @PostMapping("/edit")
    public String courseSettingCategoryEdit(long updateCategoryId,String updateName){//目前只有课程分类设置

        CourseCategory courseCategory=courseCategoryRepository.findById(updateCategoryId).get();
        courseCategory.setCategory(updateName);
        courseCategoryRepository.save(courseCategory);
        return "redirect:/admin/setting";
    }

    @PostMapping("add")
    public String courseSettingCategoryAdd(String addCategory){//目前只有课程分类设置

        CourseCategory courseCategory=new CourseCategory();
        courseCategory.setCategory(addCategory);
        courseCategoryRepository.save(courseCategory);
        return "redirect:/admin/setting";
    }

}
