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
        }else {//第一次
            StudentCategoryWeightProcess_first(username, categoryId,weight);
        }
    }

    @Override
    public List<CourseRanking> findAllCourseRankingByUsername(String username) {
        CategoryWeight categoryWeight=categoryWeightRepository.findTopByUsernameOrderByWeightDesc(username);
        List<CourseRanking> courseRankingList=null;
        if (categoryWeight != null) {
            courseRankingList=courseRankingRepository.findAllByCategoryIdOrderByNumDesc(categoryWeight.getCategoryId());
        }else {//当用户没有访问过任何课程之前，则根据热度推荐，否则会出现bug
            courseRankingList= courseRankingRepository.findTop10ByOrderByNumDesc();
        }

        return courseRankingList;
    }

    public Double returnAvg(String username){
        Double avg=stuCourseRepository.findAvgStarsByUsername(username);//该用户对课程的平均评分
        if (avg == null) {//该用户未对任何课程评分，则默认平均分为3
            avg=3.0;
        }
        return avg;
    }

    @Override
    public List<Course> recommend(String username,Long c1) {
        List<Course> courseList=courseRepository.findByIdIsNot(c1);//其余课程
        List<CourseSim> courseSimList=new ArrayList<>();
        //1.计算c1与其余课程的相似度
        for (Course course :
                courseList) {
            Double sim=calculateSim(c1, course.getId());
            courseSimList.add(new CourseSim(course.getId(),sim));
            System.out.println(c1+"和"+course.getId()+"之间的相似度为"+sim);
        }
        Collections.sort(courseSimList);
        Collections.reverse(courseSimList);
        System.out.println("相似度排行："+courseSimList);
        List<CourseSim> coursesNearestNeighbor=new ArrayList<>();
        for (int i = 0; i < 3&&i<courseSimList.size(); i++) {//2.得到最近邻居集合
            coursesNearestNeighbor.add(courseSimList.get(i));
        }
        System.out.println("最近邻居集合 "+coursesNearestNeighbor);
        List<CoursePredScore> coursePredScoreList=new ArrayList<>();
        List<Course> coursesNotAdd=courseRepository.findByUsernameIdIsNot(username);//未参加的课程
        Double avg=returnAvg(username);
        for (Course course :
                coursesNotAdd) {//得到未参加课程的预测评分
            Double pred=pred(username,coursesNearestNeighbor,avg,course.getId());
            System.out.println("课程"+course.getId()+"预测评分为"+pred);
            coursePredScoreList.add(new CoursePredScore(course.getId(),pred));
        }
        Collections.reverse(coursePredScoreList);//排序
        List<Course> courses=new ArrayList<>();
        for (int i = 0; i <5&&i<coursePredScoreList.size(); i++) {//形成推荐课程列表
            courses.add(courseService.findCourseById(coursePredScoreList.get(i).getCourseId()));
        }
        return courses;
    }
    //计算相似度
    @Override
    public Double calculateSim(Long c1, Long c2) {
        List<UserStars> userStarsList=userStarsRepository.findByCiFirstAndCiSecond(c1,c2);//找出对c1和c2均评分过的用户集合
        List<StuCourse> stuCourseList1=stuCourseRepository.findAllByCourseIdAndEvaluationState(c1,1);//找出对c1评分过的用户集合
        List<StuCourse> stuCourseList2=stuCourseRepository.findAllByCourseIdAndEvaluationState(c2,1);//找出对c2评分过的用户集合
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
        if(userStarsList.size() == 0||sumUi==0.0||sumUj==0.0){//未有对c1和c2均评过分的用户集合或有课程从未评分过，相似度为默认为0
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
            if (stuCourse != null) {//为空表示该用户对该最近邻居未评分（出现在评分数少的情况）
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
