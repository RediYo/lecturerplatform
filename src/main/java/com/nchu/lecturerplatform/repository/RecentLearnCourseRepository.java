package com.nchu.lecturerplatform.repository;

import com.nchu.lecturerplatform.domain.RecentLearnCourse;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RecentLearnCourseRepository extends CrudRepository<RecentLearnCourse,Long> {

    List<RecentLearnCourse> findTop3ByUsernameOrderByDatetimeDesc(String username);//最近学习课程

}
