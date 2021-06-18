package com.nchu.lecturerplatform.service;

import com.nchu.lecturerplatform.domain.Applicant;
import com.nchu.lecturerplatform.domain.Feedback;
import com.nchu.lecturerplatform.repository.FeedbackRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class FeedbackServiceImpl implements FeedbackService{

    @Resource
    private FeedbackRepository feedbackRepository;

    @Override
    public Page<Feedback> getFeedbackList(int pageNum, int pageSize) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        Page<Feedback> feedbacks = feedbackRepository.findAll(pageable);
        return feedbacks;
    }

    @Override
    public Page<Feedback> getFeedbackListByViewState(int viewState, int pageNum, int pageSize) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        Page<Feedback> feedbacks = feedbackRepository.findByViewState(viewState,pageable);
        return feedbacks;
    }
}
