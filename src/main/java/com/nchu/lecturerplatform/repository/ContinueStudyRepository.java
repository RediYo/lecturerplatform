package com.nchu.lecturerplatform.repository;

import com.nchu.lecturerplatform.domain.ContinueStudy;
import com.nchu.lecturerplatform.domain.Course;
import com.nchu.lecturerplatform.domain.SubTitle;
import org.springframework.data.repository.CrudRepository;

public interface ContinueStudyRepository extends CrudRepository<ContinueStudy,Long> {

    ContinueStudy findByCourseAndUsername(Course course, String username);
    ContinueStudy findBySubTitleAndUsername(SubTitle subTitle, String username);
    ContinueStudy findByIdAndUsername(Long id,String username);
}
