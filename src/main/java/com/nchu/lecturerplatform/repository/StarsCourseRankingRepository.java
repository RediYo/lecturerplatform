package com.nchu.lecturerplatform.repository;

import com.nchu.lecturerplatform.domain.StarsCourseRanking;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StarsCourseRankingRepository extends CrudRepository<StarsCourseRanking,Long> {

    List<StarsCourseRanking> findTop10ByOrderByAvgDesc();
    @Query(value="select s.* from stars_course_ranking s,course c where s.id=c.id and c.username=?1 order by avg desc ;", nativeQuery = true)
    List<StarsCourseRanking> findStarsCourseRankingByUsername(String username);;
}
