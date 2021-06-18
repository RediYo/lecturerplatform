package com.nchu.lecturerplatform.repository;

import com.nchu.lecturerplatform.domain.Course;
import com.nchu.lecturerplatform.domain.Note;
import com.nchu.lecturerplatform.domain.SubTitle;
import com.nchu.lecturerplatform.domain.TopTitle;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NoteRepository extends CrudRepository<Note,Long> {

    List<Note> findByCourse(Course course);
    List<Note> findByCourseAndUsername(Course course,String username);
    Note findByCourseAndTopTitleAndSubTitle(Course course, TopTitle topTitle, SubTitle subTitle);
    Note findByCourseAndTopTitleAndSubTitleAndUsername(Course course, TopTitle topTitle, SubTitle subTitle,String username);
}
