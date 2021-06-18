package com.nchu.lecturerplatform.service;

import com.nchu.lecturerplatform.domain.Course;
import com.nchu.lecturerplatform.domain.Lecturer;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

public interface CourseService {

    Page<Course> getCourseList(int pageNum, int pageSize);

    Page<Course> getCourseListByNameAndLecturer(int pageNum, int pageSize, String name, Lecturer lecturer);
    Page<Course> getCourseListByNameContaining(int pageNum, int pageSize, String name);
    Page<Course> getCourseListByNameAndLecturerAndCategoryId(int pageNum, int pageSize, String name,Long categoryId, Lecturer lecturer);

    Course findCourseById(long id);

    void save(Course course);

    void delete(long id);
}
