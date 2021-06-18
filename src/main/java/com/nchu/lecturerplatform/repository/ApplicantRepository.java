package com.nchu.lecturerplatform.repository;

import com.nchu.lecturerplatform.domain.Applicant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicantRepository extends JpaRepository<Applicant,Long> {

    Page<Applicant> findByViewState(int viewState, Pageable pageable);

}
