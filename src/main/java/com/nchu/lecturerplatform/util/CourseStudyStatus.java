package com.nchu.lecturerplatform.util;

import com.nchu.lecturerplatform.domain.StuCourse;
import com.nchu.lecturerplatform.repository.StuCourseRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class CourseStudyStatus {

    @Resource
    private StuCourseRepository stuCourseRepository;

    public boolean courseExitStatus(String username,Long courseId){
        StuCourse stuCourse = stuCourseRepository.findByUsernameAndCourseId(username, courseId);
        return stuCourse == null;
    }
}
