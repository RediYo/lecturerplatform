package com.nchu.lecturerplatform.service;

import com.nchu.lecturerplatform.domain.Applicant;
import com.nchu.lecturerplatform.repository.ApplicantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ApplicantServiceImpl implements ApplicantService{

    @Resource
    private ApplicantRepository applicantRepository;

    @Override
    public Page<Applicant> getApplicantList(int pageNum, int pageSize) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        Page<Applicant> applicants = applicantRepository.findAll(pageable);
        return applicants;
    }

    @Override
    public Page<Applicant> getApplicantListByViewState(int viewState,int pageNum, int pageSize) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        Page<Applicant> applicants = applicantRepository.findByViewState(viewState,pageable);
        return applicants;
    }

    @Override
    public Applicant findApplicantById(long id) {
        return applicantRepository.findById(id).get();
    }

    @Override
    public void save(Applicant applicant) {
        applicantRepository.save(applicant);
    }


    @Override
    public void delete(long id) {
        applicantRepository.deleteById(id);
    }
}
