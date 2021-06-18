package com.nchu.lecturerplatform.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class StarsCourseRanking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private float avg;//平均分
    private int evaluationNum;


    /**
     * 课程星级排行对课程 一对一
     */
    @JoinColumn(name = "id",referencedColumnName = "id")
    @OneToOne(targetEntity = Course.class,cascade = {CascadeType.ALL},fetch = FetchType.LAZY)
    private Course course;

    public StarsCourseRanking(float avg, int evaluationNum) {
        this.avg = avg;
        this.evaluationNum = evaluationNum;
    }

    public StarsCourseRanking() {

    }
}
