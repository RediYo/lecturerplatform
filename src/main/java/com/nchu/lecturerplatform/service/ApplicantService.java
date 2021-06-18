package com.nchu.lecturerplatform.service;

import com.nchu.lecturerplatform.domain.Applicant;
import org.springframework.data.domain.Page;

public interface ApplicantService {

    Page<Applicant> getApplicantList(int pageNum, int pageSize);

    Page<Applicant> getApplicantListByViewState(int viewState,int pageNum, int pageSize);

    Applicant findApplicantById(long id);

    void save(Applicant applicant);

    void delete(long id);
}
