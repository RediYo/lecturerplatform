package com.nchu.lecturerplatform.repository;

import com.nchu.lecturerplatform.domain.Course;
import com.nchu.lecturerplatform.domain.StuCourse;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface StuCourseRepository extends CrudRepository<StuCourse,Long> {

    List<StuCourse> findAllByCourseIdAndEvaluationState(long courseId,int evaluationState);
    StuCourse findByUsernameAndCourseId(String username,long courseId);
    StuCourse findByUsernameAndCourseIdAndEvaluationState(String username,long courseId,int evaluationState);
    @Query(value="select avg(o.evaluation_stars) from stu_course o where o.username=?1 and evaluation_state=1 group by o.username", nativeQuery = true) //基于HQL
    Double findAvgStarsByUsername(String username);;
    @Query(value="select o.* from stu_course o where o.username<>?1", nativeQuery = true) //基于HQL
    List<StuCourse> findOtherCoursesByNotUsername(String username);
    StuCourse findByCourseIdAndUsername(Long courseId,String username);
    List<StuCourse> findByCourseAndEvaluationState(Course course,int evaluationState);

}
