package com.nchu.lecturerplatform.repository;

import com.nchu.lecturerplatform.domain.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CourseRepository  extends JpaRepository<Course,Long> {

    Page<Course> findAllByNameContainingAndLecturer(String name, Lecturer lecturer, Pageable pageable);//讲师可以通过username查找自己的课程
    Page<Course> findAllByNameContaining(String name, Pageable pageable);//讲师可以通过username查找自己的课程
    Page<Course> findAllByNameContainingAndLecturerAndCourseCategory(String name, Lecturer lecturer,CourseCategory courseCategory, Pageable pageable);//讲师可以通过username查找自己的课程
    Long countByCourseCategory(CourseCategory courseCategory);
    List<Course> findAllByOrderByIdDesc();
    List<Course> findTop10ByOrderByIdDesc();
    List<Course> findAllByOrderByWeekNumDesc();
    List<Course> findTop15ByOrderByWeekNumDesc();
    List<Course> findAllByNameContaining(String name);//通过课程名模糊查找
    @Query(value="select o.* from course o where o.id not in (SELECT stu_course.course_id FROM stu_course where stu_course.username=?1);", nativeQuery = true)
    List<Course> findByUsernameIdIsNot(String username);
    List<Course> findByIdIsNot(Long id);
    int countByLecturer(Lecturer lecturer);
    @Modifying
    @Query(value="delete from course where id=?1", nativeQuery = true)
    void deleteById(Long id);
}
