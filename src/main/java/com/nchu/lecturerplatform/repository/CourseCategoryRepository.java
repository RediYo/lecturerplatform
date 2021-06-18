package com.nchu.lecturerplatform.repository;

import com.nchu.lecturerplatform.domain.CourseCategory;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CourseCategoryRepository extends CrudRepository<CourseCategory,Long> {

    Long countByCategory(String category);
}
