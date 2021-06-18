package com.nchu.lecturerplatform.repository;

import com.nchu.lecturerplatform.domain.CourseDayData;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CourseDayDataRepository extends CrudRepository<CourseDayData,Long> {

    List<CourseDayData> findAllByCourseId(Long courseId);

}
