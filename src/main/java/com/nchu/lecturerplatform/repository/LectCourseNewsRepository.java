package com.nchu.lecturerplatform.repository;

import com.nchu.lecturerplatform.domain.LectCourseNews;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LectCourseNewsRepository extends CrudRepository<LectCourseNews,Long> {
    List<LectCourseNews> findAllByUsername(String username);
}
