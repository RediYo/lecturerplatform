package com.nchu.lecturerplatform.repository;

import com.nchu.lecturerplatform.domain.CourseNews;
import com.nchu.lecturerplatform.domain.Lecturer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CourseNewRepository extends CrudRepository<CourseNews,Long> {

}
