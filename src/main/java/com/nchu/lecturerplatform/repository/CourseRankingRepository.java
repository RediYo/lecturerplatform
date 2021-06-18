package com.nchu.lecturerplatform.repository;

import com.nchu.lecturerplatform.domain.CourseRanking;
import com.nchu.lecturerplatform.domain.StarsCourseRanking;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CourseRankingRepository extends CrudRepository<CourseRanking,Long> {

    List<CourseRanking> findAllByCategoryIdOrderByNumDesc(Long categoryId);
    List<CourseRanking> findTop60ByCategoryIdOrderByNumDesc(Long categoryId);
    List<CourseRanking> findAllByCategoryIdOrderByIdDesc(Long categoryId);
    List<CourseRanking> findAllByOrderByNumDesc();
    List<CourseRanking> findTop10ByOrderByNumDesc();
    List<CourseRanking> findTop60ByOrderByNumDesc();
    List<CourseRanking> findAllByNameContainingOrderByNumDesc(String name);//根据课程名模糊查找
    @Query(value="select cr.* from course_ranking cr,course c where cr.id=c.id and c.username=?1 order by num desc ;", nativeQuery = true)
    List<CourseRanking> findCourseRankingByUsername(String username);;

}
