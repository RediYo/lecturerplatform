package com.nchu.lecturerplatform.repository;

import com.nchu.lecturerplatform.domain.LectCourseCategory;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LectCourseCategoryRepository extends CrudRepository<LectCourseCategory,Long> {

    List<LectCourseCategory> findDistinctByUsername(String username);
}
