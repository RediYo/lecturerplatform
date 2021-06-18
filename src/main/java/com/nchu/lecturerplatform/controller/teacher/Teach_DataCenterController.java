package com.nchu.lecturerplatform.controller.teacher;

import com.nchu.lecturerplatform.domain.*;
import com.nchu.lecturerplatform.repository.*;
import com.nchu.lecturerplatform.service.CourseService;
import com.nchu.lecturerplatform.util.CategoryData;
import com.nchu.lecturerplatform.util.DayDate;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/teacher/data")
@Controller
public class Teach_DataCenterController {

    @Resource
    private CourseService courseService;
    @Resource
    private LecturerRepository lecturerRepository;
    @Resource
    private CourseDayDataRepository courseDayDataRepository;
    @Resource
    private SearchKeyRankingRepository searchKeyRankingRepository;
    @Resource
    private CourseRepository courseRepository;
    @Resource
    private LectFollowViewRepository lectFollowViewRepository;
    @Resource
    private OrdersRepository ordersRepository;

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
        model.addAttribute("categoryId",categoryId);
        System.out.println("总页数" + courses.getTotalPages());
        System.out.println("当前页是：" + pageNum);

        System.out.println("分页数据：省略");
        /*Iterator<Course> u = courses.iterator();
        while (u.hasNext()) {
            System.out.println(u.next().toString());
        }*/
        model.addAttribute("courses", courses);
        Lecturer lecturer=lecturerRepository.findById(principal.getName()).orElse(null);
        model.addAttribute("lecturer",lecturer);
        List<SearchKeyRanking> searchKeyRankingList=searchKeyRankingRepository.findTop8ByOrderBySearchNumDesc();
        model.addAttribute("searchKeyRankingList",searchKeyRankingList);
        model.addAttribute("lecturer", lecturerRepository.findById(principal.getName()).orElse(null));
        model.addAttribute("courseNum",courseRepository.countByLecturer(lecturer));//课程数量
        LectFollowView lectFollowView=lectFollowViewRepository.findById(principal.getName()).orElse(null);
        if (lectFollowView != null) {
            model.addAttribute("followNum",lectFollowView.getFollowNum());//粉丝数量
        }else {
            model.addAttribute("followNum",0);//粉丝数量
        }

        model.addAttribute("orderNum",ordersRepository.findByUsernameOrderByCreateTimeDesc(principal.getName()).size());
        return "teacher/teach_dataCenter";
    }

    @PostMapping("/search")
    public String searchByKeyword(String keyword,Long categoryId) throws UnsupportedEncodingException {//按关键字模糊查询
        return "redirect:/teacher/data/list?keyword="+ URLEncoder.encode(keyword, "UTF-8")+"&&categoryId="+categoryId;
    }

    @ResponseBody
    @GetMapping("/view")
    public DayDate courseDataView(Long id){//课程id，查看课程每日动态变化情况

        List<CourseDayData> courseDayDataList=courseDayDataRepository.findAllByCourseId(id);
        List<String> dates=new ArrayList<>();
        for (CourseDayData courseDayData: courseDayDataList) {
            dates.add(courseDayData.getDate());
        }
        List<Integer> dayNums=new ArrayList<>();
        for (CourseDayData courseDayData: courseDayDataList){
            dayNums.add(courseDayData.getNum());
        }
        DayDate dayDate=new DayDate(dates,dayNums);
        return dayDate;
    }

}
