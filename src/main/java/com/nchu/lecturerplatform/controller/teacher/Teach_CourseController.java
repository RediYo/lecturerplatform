package com.nchu.lecturerplatform.controller.teacher;

import com.nchu.lecturerplatform.domain.Course;
import com.nchu.lecturerplatform.domain.CourseCategory;
import com.nchu.lecturerplatform.domain.LectCourseCategory;
import com.nchu.lecturerplatform.domain.Lecturer;
import com.nchu.lecturerplatform.repository.*;
import com.nchu.lecturerplatform.service.CourseService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/teacher/course")
public class Teach_CourseController {

    @Resource
    private CourseService courseService;
    @Resource
    private CourseRepository courseRepository;
    @Resource
    private LecturerRepository lecturerRepository;
    @Resource
    private CourseCategoryRepository courseCategoryRepository;
    @Resource
    private LectCourseCategoryRepository lectCourseCategoryRepository;
    @Resource
    private OrdersRepository ordersRepository;

    @GetMapping("/category")
    @ResponseBody
    public List<LectCourseCategory> requestCategoryProcess(Principal principal){

        List<LectCourseCategory> lectCourseCategoryList=lectCourseCategoryRepository.findDistinctByUsername(principal.getName());

        return lectCourseCategoryList;
    }

    @GetMapping("/delete")
    public String deleteCourse(Model model, long id) {
        courseService.delete(id);
        return "redirect:/teacher/course/list";
    }

    @GetMapping("/edit")
    public String editCourseView(Model model, long id,Principal principal) {
        Course course = courseService.findCourseById(id);
        model.addAttribute("course", course);
        model.addAttribute("lecturer", lecturerRepository.findById(principal.getName()).orElse(null));
        model.addAttribute("imageSite", course.getImageSite());
        model.addAttribute("categoryList",courseCategoryRepository.findAll());
        return "teacher/teach_editCourse";
    }

    @PostMapping("/edit")
    public String editCourseProcess(Course course,long categoryId, @RequestParam(value = "file") MultipartFile file) {

        //System.out.println(course.getImageSite()+"123");course.getImageSite()??????
        Course course_repo = courseService.findCourseById(course.getId());
        CourseCategory courseCategory=courseCategoryRepository.findById(categoryId).orElse(null);
        course_repo.setCourseCategory(courseCategory);
        course_repo.setName(course.getName());
        course_repo.setMoney(course.getMoney());
        course_repo.setProfile(course.getProfile());
        if (!file.isEmpty()) {//??????????????????????????????,??????
            String fileName = file.getOriginalFilename();  // ?????????
            String suffixName = fileName.substring(fileName.lastIndexOf("."));  // ??????????????????
            String filePath = "D://LecturerPlatform//courses//images//"; // ??????????????????
            fileName = course_repo.getId() + suffixName; // ????????????
            System.out.println(fileName);
            File dest = new File(filePath + fileName);
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            try {
                file.transferTo(dest);
                course_repo.setImageSite("/course_images/" + fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        courseService.save(course_repo);
        return "redirect:/teacher/course/list";
    }

    @GetMapping("/add")
    public String addCourseView(Model model,Principal principal) {//???????????????
        model.addAttribute("lecturer",lecturerRepository.findById(principal.getName()).orElse(null));
        model.addAttribute("course", new Course());
        model.addAttribute("categoryList",courseCategoryRepository.findAll());
        return "teacher/teach_addCourse";
    }

    @PostMapping("/add")
    public String addCourseProcess(Principal principal,Course course,long categoryId, @RequestParam(value = "file",defaultValue = "null") MultipartFile file) {

        String fileName="";
        course=courseRepository.save(course);
        if (!file.isEmpty()) {
            //courseService.save(course);//????????????????????????course.getId()??????id???????????????????????????????????????????????????????????????????????????????????????????????????????????????id
            fileName = file.getOriginalFilename();  // ?????????
            String suffixName = fileName.substring(fileName.lastIndexOf("."));  // ??????????????????
            String filePath = "D://LecturerPlatform//courses//images//"; // ??????????????????
            fileName = course.getId() + suffixName; // ????????????
            File dest = new File(filePath + fileName);
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            try {
                file.transferTo(dest);
            } catch (IOException e) {
                e.printStackTrace();
            }
            course.setImageSite("/course_images/" + fileName);
        }else {//????????????????????????????????????
            course.setImageSite("/images/courseImage-default.jpeg");
        }

        //course.setUsername(principal.getName());//????????????
        course.setLecturer(lecturerRepository.findById(principal.getName()).get());
        course.setEnabled(0);//?????????????????????
        course.setCourseCategory(courseCategoryRepository.findById(categoryId).orElse(null));//????????????
        //course.setImageSite("/course_images/" + fileName);
        course.setVideoSite("/course_videos/" + course.getId() + "/");//????????????????????????????????????????????????????????????
        course.setShareDocSite("/docs/" + course.getId() + "/");//????????????????????????????????????????????????????????????
        courseService.save(course);

        //course news
        Lecturer lecturer=lecturerRepository.findById(principal.getName()).orElse(null);
        lecturer.getLecturerNewsCourses().add(course);
        lecturerRepository.save(lecturer);

        return "redirect:/teacher/course/list";
    }

    @PostMapping("/search")
    public String searchByKeyword(String keyword,Long categoryId) throws UnsupportedEncodingException {//????????????????????????
        return "redirect:/teacher/course/list?keyword="+ URLEncoder.encode(keyword, "UTF-8")+"&&categoryId="+categoryId;
    }

    /*@GetMapping("/orders")
    public String orders(Principal principal, @RequestParam(value = "keyword", defaultValue = "") String keyword,@RequestParam(value = "categoryId", defaultValue = "0")Long categoryId, Model model, @RequestParam(value = "pageNum", defaultValue = "0") int pageNum, @RequestParam(value = "pageSize", defaultValue = "5") int pageSize){
        System.out.println(keyword);
        System.out.println("============================");
        //Page<Course> courses = courseService.getCourseList(pageNum, pageSize);
        Page<Course> courses;
        if (categoryId == 0) {
            courses= courseService.getCourseListByNameAndLecturer(pageNum, pageSize, keyword,lecturerRepository.findById(principal.getName()).get());
        }else {
            courses = courseService.getCourseListByNameAndLecturerAndCategoryId(pageNum, pageSize, keyword,categoryId,lecturerRepository.findById(principal.getName()).get());
        }
        model.addAttribute("categoryId",categoryId);
        System.out.println("?????????" + courses.getTotalPages());
        System.out.println("???????????????" + pageNum);

        System.out.println("?????????????????????");
        model.addAttribute("courses", courses);
        return "/teacher/teach_orders";
    }*/

    @GetMapping("/list")
    public String courseList(Principal principal, @RequestParam(value = "keyword", defaultValue = "") String keyword,@RequestParam(value = "categoryId", defaultValue = "0")Long categoryId, Model model, @RequestParam(value = "pageNum", defaultValue = "0") int pageNum, @RequestParam(value = "pageSize", defaultValue = "5") int pageSize) {

        System.out.println(keyword);
        System.out.println("============================");
        //Page<Course> courses = courseService.getCourseList(pageNum, pageSize);
        Page<Course> courses;
        if (categoryId == 0) {
            courses= courseService.getCourseListByNameAndLecturer(pageNum, pageSize, keyword,lecturerRepository.findById(principal.getName()).get());
        }else {
            courses = courseService.getCourseListByNameAndLecturerAndCategoryId(pageNum, pageSize, keyword,categoryId,lecturerRepository.findById(principal.getName()).get());
        }
        System.out.println("?????????" + courses.getTotalPages());
        System.out.println("???????????????" + pageNum);

        System.out.println("?????????????????????");
        /*Iterator<Course> u = courses.iterator();
        while (u.hasNext()) {
            System.out.println(u.next().toString());
        }*/
        model.addAttribute("courses", courses);
        model.addAttribute("lecturer",lecturerRepository.findById(principal.getName()).orElse(null));
        model.addAttribute("categoryId",categoryId);
        return "teacher/teach_courses";
    }
}
