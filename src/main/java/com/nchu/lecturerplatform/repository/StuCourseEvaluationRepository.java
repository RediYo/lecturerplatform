package com.nchu.lecturerplatform.repository;

import com.nchu.lecturerplatform.domain.StuCourse;
import com.nchu.lecturerplatform.domain.StuCourseEvaluation;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StuCourseEvaluationRepository extends CrudRepository<StuCourseEvaluation,Long> {

    List<StuCourseEvaluation> findAllByCourseIdAndEvaluationState(long courseId, int evaluationState);
}
