package com.nchu.lecturerplatform.service;

import com.nchu.lecturerplatform.domain.Course;
import com.nchu.lecturerplatform.domain.Lecturer;
import com.nchu.lecturerplatform.repository.CourseCategoryRepository;
import com.nchu.lecturerplatform.repository.CourseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CourseServiceImpl implements CourseService {

    @Resource
    private CourseRepository courseRepository;
    @Resource
    private CourseCategoryRepository courseCategoryRepository;

    @Override
    public Page<Course> getCourseList(int pageNum, int pageSize) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        Page<Course> courses = courseRepository.findAll(pageable);
        return courses;
    }

    @Override
    public Page<Course> getCourseListByNameAndLecturer(int pageNum, int pageSize, String name, Lecturer lecturer) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        Page<Course> courses = courseRepository.findAllByNameContainingAndLecturer(name,lecturer,pageable);
        return courses;
    }

    @Override
    public Page<Course> getCourseListByNameContaining(int pageNum, int pageSize, String name) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        Page<Course> courses = courseRepository.findAllByNameContaining(name,pageable);
        return courses;
    }

    @Override
    public Page<Course> getCourseListByNameAndLecturerAndCategoryId(int pageNum, int pageSize, String name, Long categoryId, Lecturer lecturer) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        Page<Course> courses = courseRepository.findAllByNameContainingAndLecturerAndCourseCategory(name,lecturer,courseCategoryRepository.findById(categoryId).orElse(null),pageable);
        return courses;
    }

    @Override
    public Course findCourseById(long id) {
        return courseRepository.findById(id).orElse(null);
    }

    @Override
    public void save(Course course) {
        courseRepository.save(course);
    }

    @Override
    public void delete(long id) {
        courseRepository.deleteById(id);
    }
}
