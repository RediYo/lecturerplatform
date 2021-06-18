package com.nchu.lecturerplatform.repository;

import com.nchu.lecturerplatform.domain.Course;
import com.nchu.lecturerplatform.domain.TopTitle;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TopTitleRepository extends CrudRepository<TopTitle, Long> {
        TopTitle findByTitleAndCourse(String title,Course course);
        void deleteByTitleAndCourse(String title, Course course);
        List<TopTitle> findAllByCourse(Course course);
}
