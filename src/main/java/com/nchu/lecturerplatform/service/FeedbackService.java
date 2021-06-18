package com.nchu.lecturerplatform.service;

import com.nchu.lecturerplatform.domain.Applicant;
import com.nchu.lecturerplatform.domain.Feedback;
import org.springframework.data.domain.Page;

public interface FeedbackService {

    Page<Feedback> getFeedbackList(int pageNum, int pageSize);
    Page<Feedback> getFeedbackListByViewState(int viewState, int pageNum, int pageSize);
}
