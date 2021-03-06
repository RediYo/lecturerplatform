package com.nchu.lecturerplatform.service;

import com.nchu.lecturerplatform.domain.*;
import com.nchu.lecturerplatform.repository.*;
import com.nchu.lecturerplatform.util.CoursePredScore;
import com.nchu.lecturerplatform.util.CourseSim;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class RecommendServiceImpl implements RecommendService{

    @Resource
    private CategoryWeightRepository categoryWeightRepository;
    @Resource
    private CourseCategoryRepository courseCategoryRepository;
    @Resource
    private StudentRepository studentRepository;
    @Resource
    private CourseRankingRepository courseRankingRepository;
    @Resource
    private UserStarsRepository userStarsRepository;
    @Resource
    private StuCourseRepository stuCourseRepository;
    @Resource
    private CourseService courseService;
    @Resource
    private CourseRepository courseRepository;


    @Override
    public void StudentCategoryWeightProcess_first(String username, Long categoryId,int weight) {

        Student student=studentRepository.findById(username).orElse(null);
        CourseCategory courseCategory=courseCategoryRepository.findById(categoryId).orElse(null);
        student.getCourseCategoryList().add(courseCategory);
        studentRepository.save(student);
        CategoryWeight categoryWeight=categoryWeightRepository.findCategoryWeightByUsernameAndCategoryId(username,categoryId);
        long weight_repo=categoryWeight.getWeight();
        categoryWeight.setWeight(weight_repo+weight);
        categoryWeightRepository.save(categoryWeight);
    }

    @Override
    public void StudentCategoryWeightProcess_notFirst(String username, Long categoryId, int weight) {

        CategoryWeight categoryWeight=categoryWeightRepository.findCategoryWeightByUsernameAndCategoryId(username,categoryId);
        long weight_repo=categoryWeight.getWeight();
        categoryWeight.setWeight(weight_repo+weight);
        categoryWeightRepository.save(categoryWeight);
    }

    @Override
    public void StudentCategoryWeightProcess(String username, Long categoryId, int weight) {
        if(categoryWeightRepository.findCategoryWeightByUsernameAndCategoryId(username,categoryId)!=null){
            StudentCategoryWeightProcess_notFirst(username, categoryId, weight);
        }else {//?????????
            StudentCategoryWeightProcess_first(username, categoryId,weight);
        }
    }

    @Override
    public List<CourseRanking> findAllCourseRankingByUsername(String username) {
        CategoryWeight categoryWeight=categoryWeightRepository.findTopByUsernameOrderByWeightDesc(username);
        List<CourseRanking> courseRankingList=null;
        if (categoryWeight != null) {
            courseRankingList=courseRankingRepository.findAllByCategoryIdOrderByNumDesc(categoryWeight.getCategoryId());
        }else {//????????????????????????????????????????????????????????????????????????????????????bug
            courseRankingList= courseRankingRepository.findTop10ByOrderByNumDesc();
        }

        return courseRankingList;
    }

    public Double returnAvg(String username){
        Double avg=stuCourseRepository.findAvgStarsByUsername(username);//?????????????????????????????????
        if (avg == null) {//?????????????????????????????????????????????????????????3
            avg=3.0;
        }
        return avg;
    }

    @Override
    public List<Course> recommend(String username,Long c1) {
        List<Course> courseList=courseRepository.findByIdIsNot(c1);//????????????
        List<CourseSim> courseSimList=new ArrayList<>();
        //1.??????c1???????????????????????????
        for (Course course :
                courseList) {
            Double sim=calculateSim(c1, course.getId());
            courseSimList.add(new CourseSim(course.getId(),sim));
            System.out.println(c1+"???"+course.getId()+"?????????????????????"+sim);
        }
        Collections.sort(courseSimList);
        Collections.reverse(courseSimList);
        System.out.println("??????????????????"+courseSimList);
        List<CourseSim> coursesNearestNeighbor=new ArrayList<>();
        for (int i = 0; i < 3&&i<courseSimList.size(); i++) {//2.????????????????????????
            coursesNearestNeighbor.add(courseSimList.get(i));
        }
        System.out.println("?????????????????? "+coursesNearestNeighbor);
        List<CoursePredScore> coursePredScoreList=new ArrayList<>();
        List<Course> coursesNotAdd=courseRepository.findByUsernameIdIsNot(username);//??????????????????
        Double avg=returnAvg(username);
        for (Course course :
                coursesNotAdd) {//????????????????????????????????????
            Double pred=pred(username,coursesNearestNeighbor,avg,course.getId());
            System.out.println("??????"+course.getId()+"???????????????"+pred);
            coursePredScoreList.add(new CoursePredScore(course.getId(),pred));
        }
        Collections.reverse(coursePredScoreList);//??????
        List<Course> courses=new ArrayList<>();
        for (int i = 0; i <5&&i<coursePredScoreList.size(); i++) {//????????????????????????
            courses.add(courseService.findCourseById(coursePredScoreList.get(i).getCourseId()));
        }
        return courses;
    }
    //???????????????
    @Override
    public Double calculateSim(Long c1, Long c2) {
        List<UserStars> userStarsList=userStarsRepository.findByCiFirstAndCiSecond(c1,c2);//?????????c1???c2???????????????????????????
        List<StuCourse> stuCourseList1=stuCourseRepository.findAllByCourseIdAndEvaluationState(c1,1);//?????????c1????????????????????????
        List<StuCourse> stuCourseList2=stuCourseRepository.findAllByCourseIdAndEvaluationState(c2,1);//?????????c2????????????????????????
        double sumUij=0.0,sumUi=0.0,sumUj=0.0;

        for (UserStars userStars:
             userStarsList) {
            Double avg=returnAvg(userStars.getU1());
            sumUij+=((userStars.getStar1()-avg)*(userStars.getStar2()-avg));
        }
        for (StuCourse stuCourse :
                stuCourseList1) {
            Double avg=returnAvg(stuCourse.getUsername());
            sumUi+=((stuCourse.getEvaluationStars()-avg)*(stuCourse.getEvaluationStars()-avg));
        }
        for (StuCourse stuCourse :
                stuCourseList2) {
            Double avg=returnAvg(stuCourse.getUsername());
            sumUj+=((stuCourse.getEvaluationStars()-avg)*(stuCourse.getEvaluationStars()-avg));
        }
        if(userStarsList.size() == 0||sumUi==0.0||sumUj==0.0){//?????????c1???c2??????????????????????????????????????????????????????????????????????????????0
            return 0.0;
        }
        return sumUij/(Math.sqrt(sumUi)*Math.sqrt(sumUj));
    }

    @Override
    public Double pred(String username,List<CourseSim> coursesNearestNeighbor,Double avg,Long c1) {
        double sumTop=0.0,sumBottom=0.0;
        for (CourseSim courseSim :
                coursesNearestNeighbor) {
            StuCourse stuCourse= stuCourseRepository.findByUsernameAndCourseIdAndEvaluationState(username,courseSim.getCourseId(),1);
            if (stuCourse != null) {//????????????????????????????????????????????????????????????????????????????????????
                int stars=stuCourse.getEvaluationStars();
                Double sim=calculateSim(c1,courseSim.getCourseId());
                sumTop+=(sim*(stars-avg));
                sumBottom+=sim;
            }
        }
        if (sumBottom == 0.0) {
            return 0.0;
        }
        return avg+sumTop/sumBottom;
    }

}
