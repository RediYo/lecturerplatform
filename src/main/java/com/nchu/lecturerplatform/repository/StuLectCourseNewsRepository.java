package com.nchu.lecturerplatform.repository;

import com.nchu.lecturerplatform.domain.StuLectCourseNews;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StuLectCourseNewsRepository extends CrudRepository<StuLectCourseNews,Long> {

    List<StuLectCourseNews>  findAllByStuUsername(String stuUsername);

}
