package com.nchu.lecturerplatform.controller.admin;


import com.nchu.lecturerplatform.domain.Feedback;
import com.nchu.lecturerplatform.repository.FeedbackRepository;
import com.nchu.lecturerplatform.service.FeedbackService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.Iterator;

@Controller
@RequestMapping("/admin/feedback")
public class Admin_FeedbackController {

    @Resource
    private FeedbackRepository feedbackRepository;
    @Resource
    private FeedbackService feedbackService;

    @GetMapping("/modifyViewState")
    public String modifyViewState(Long id){//反馈信息id
        Feedback feedback=feedbackRepository.findById(id).orElse(null);
        feedback.setViewState(1);
        feedbackRepository.save(feedback);
        return "redirect:/admin/feedback/list?viewState=1";
    }

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value = "pageNum", defaultValue = "0") int pageNum, @RequestParam(value = "viewState", defaultValue = "-1")int viewState, @RequestParam(value = "pageSize", defaultValue = "5") int pageSize) {
        System.out.println("============================");
        Page<Feedback> feedbacks;
        if (viewState == -1) {
            feedbacks = feedbackService.getFeedbackList(pageNum, pageSize);
        }else {
            feedbacks = feedbackService.getFeedbackListByViewState(viewState,pageNum, pageSize);
        }
        model.addAttribute("viewState",viewState);
        System.out.println("总页数" + feedbacks.getTotalPages());
        System.out.println("当前页是：" + pageNum);

        System.out.println("分页数据：");
        Iterator<Feedback> u = feedbacks.iterator();
        while (u.hasNext()) {
            System.out.println(u.next().toString());
        }
        model.addAttribute("feedbacks", feedbacks);

        return "admin/admin_feedbacks";
    }
}
