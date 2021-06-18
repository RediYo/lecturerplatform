package com.nchu.lecturerplatform.repository;

import com.nchu.lecturerplatform.domain.Applicant;
import com.nchu.lecturerplatform.domain.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback,Long> {

    Page<Feedback> findByViewState(int viewState, Pageable pageable);
}
