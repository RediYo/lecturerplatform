package com.nchu.lecturerplatform.config;

import com.nchu.lecturerplatform.domain.CourseDayData;
import com.nchu.lecturerplatform.domain.CourseRanking;
import com.nchu.lecturerplatform.repository.CourseDayDataRepository;
import com.nchu.lecturerplatform.repository.CourseRankingRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class StaticScheduleTask {


    @Resource
    private CourseDayDataRepository courseDayDataRepository;
    @Resource
    private CourseRankingRepository courseRankingRepository;


    //3.添加定时任务
    @Scheduled(cron = "0 0 12 * * ?")//每天12点更新
    private void configureTasks() {
        List<CourseRanking> courseRankingList=(List<CourseRanking>)courseRankingRepository.findAll();
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (CourseRanking courseRanking :
                courseRankingList) {
            CourseDayData courseDayData=new CourseDayData(courseRanking.getId(),sdf.format(d),courseRanking.getNum());
            courseDayDataRepository.save(courseDayData);
        }
        System.err.println("执行静态定时任务时间: " + sdf.format(d));
    }
}
