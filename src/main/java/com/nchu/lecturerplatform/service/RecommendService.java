package com.nchu.lecturerplatform.service;

import com.nchu.lecturerplatform.domain.Course;
import com.nchu.lecturerplatform.domain.CourseRanking;
import com.nchu.lecturerplatform.domain.StuCourse;
import com.nchu.lecturerplatform.domain.UserStars;
import com.nchu.lecturerplatform.util.CourseSim;

import java.util.List;

public interface RecommendService {


    void StudentCategoryWeightProcess_first(String username,Long categoryId,int weight);
    void StudentCategoryWeightProcess_notFirst(String username,Long categoryId,int weight);
    void StudentCategoryWeightProcess(String username,Long categoryId,int weight);
    List<CourseRanking> findAllCourseRankingByUsername(String username);
    Double returnAvg(String username);
    List<Course> recommend(String username,Long c1);
    Double calculateSim(Long c1, Long c2);
    Double pred(String username,List<CourseSim> coursesNearestNeighbor, Double avg,Long c1);

}
