package com.nchu.lecturerplatform.service;

import com.nchu.lecturerplatform.domain.Lecturer;
import com.nchu.lecturerplatform.repository.CommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class CommentServiceImpl implements CommentService{

    @Resource
    private CommentRepository commentRepository;

    @Transactional
    @Override
    public boolean modifyImageSiteByUsername(String username, String imageSite) {
        commentRepository.updateImageSiteByUsername(username,imageSite);
        return true;
    }
}
